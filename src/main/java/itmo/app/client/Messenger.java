package itmo.app.client;

import itmo.app.shared.Utils;
import itmo.app.shared.clientrequest.ClientRequest;
import itmo.app.shared.servermessage.ServerCollectionUpdate;
import itmo.app.shared.servermessage.ServerMessage;
import itmo.app.shared.servermessage.ServerResponse;
import java.io.IOException;
import java.io.Serializable;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Collection;
import java.util.LinkedList;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

public class Messenger implements AutoCloseable {

    private SocketChannel channel;
    private Collection<Consumer<ServerCollectionUpdate>> listeners = new LinkedList<>();
    private ConcurrentMap<UUID, Consumer<ServerResponse<Serializable>>> responseHandlers = new ConcurrentHashMap<>();
    Thread selectionThread;

    public Messenger(SocketAddress addr) throws IOException {
        this.channel = SocketChannel.open(addr);
        this.channel.configureBlocking(false);
        this.selectionThread =
            new Thread(new SelectionRunnable(channel, responseHandlers, listeners));
        this.selectionThread.start();
    }

    public void onCollectionUpdate(Consumer<ServerCollectionUpdate> listener) {
        this.listeners.add(listener);
    }

    public void sendAndThen(
        ClientRequest request,
        Consumer<ServerResponse<Serializable>> handler
    ) throws IOException {
        Utils.writeObjectToCahnnel(channel, request);
        this.responseHandlers.put(request.uuid, handler);
    }

    @Override
    public void close() throws Exception {
        this.channel.close();
        this.selectionThread.interrupt();
    }
}

// TODO: Logging instead of System.out.println

class SelectionRunnable implements Runnable {

    final SocketChannel channel;
    final ConcurrentMap<UUID, Consumer<ServerResponse<Serializable>>> responseHandlers;
    private Collection<Consumer<ServerCollectionUpdate>> collectionUpdateListeners;

    public SelectionRunnable(
        SocketChannel channel,
        ConcurrentMap<UUID, Consumer<ServerResponse<Serializable>>> responseHandlers,
        Collection<Consumer<ServerCollectionUpdate>> listeners
    ) {
        this.channel = channel;
        this.responseHandlers = responseHandlers;
        this.collectionUpdateListeners = listeners;
    }

    @Override
    public void run() {
        try (Selector selector = Selector.open();) {
            this.channel.register(selector, SelectionKey.OP_READ);
            while (true) {
                selector.select();
                var keys = selector.selectedKeys().iterator();
                while (keys.hasNext()) {
                    try {
                        SelectionKey selectedKey = keys.next();
                        var message = (ServerMessage) Utils.readObjectFromChannel(
                            (SocketChannel) selectedKey.channel()
                        );
                        ServerMessage.match(
                            message,
                            response -> {
                                Consumer<ServerResponse<Serializable>> consumer =
                                    this.responseHandlers.get(response.uuid);
                                if (consumer != null) {
                                    consumer.accept(response);
                                    this.responseHandlers.remove(response.uuid);
                                }
                            },
                            collectionUpdate -> {
                                this.notifyCollectionUpdateListeners(collectionUpdate);
                            }
                        );
                        keys.remove();
                    } catch (ClassNotFoundException | IOException err) {
                        System.out.println(
                            "Exception during the reading of response: " +
                            err.getMessage()
                        );
                    }
                }
            }
        } catch (IOException err) {
            System.out.println(
                "IOException during in the selectionThread: " + err.getMessage()
            );
        }
    }

    private void notifyCollectionUpdateListeners(
        ServerCollectionUpdate collectionUpdate
    ) {
        for (var listener : this.collectionUpdateListeners) {
            listener.accept(collectionUpdate);
        }
    }
}
