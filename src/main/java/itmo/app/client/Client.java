package itmo.app.client;

import itmo.app.client.components.LoginForm;
import itmo.app.shared.clientrequest.ClientRequest;
import itmo.app.shared.clientrequest.requestbody.AddRequestBody;
import itmo.app.shared.clientrequest.requestbody.RequestBody;
import java.awt.GridBagLayout;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Arrays;
import java.util.Enumeration;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.plaf.FontUIResource;

public class Client {

    @SuppressWarnings("unused")
    private static void mesengerTest() throws IOException {
        SocketAddress addr = new InetSocketAddress("127.0.0.1", 2000);
        @SuppressWarnings({ "resource" })
        var messenger = new Messenger(addr);
        messenger.onCollectionUpdate(collectionUpdate -> {
            System.out.println("Collection update, listener 1");
        });
        messenger.onCollectionUpdate(collectionUpdate -> {
            System.out.println(
                "Collection: " + Arrays.toString(collectionUpdate.newCollection.toArray())
            );
        });
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException err) {}
            messenger.sendAndThen(
                new ClientRequest("login", "password", new RequestBody() {}),
                response -> {
                    System.out.println("Client got the response, handling...");
                    System.out.println("Handled response: " + response.body.toString());
                }
            );
        }
    }

    public static Messenger messenger = null;

    // TODO: Use EventQueue.invokeLater
    public static void main(String... args) {
        try {
            Client.messenger = new Messenger(new InetSocketAddress("127.0.0.1", 2000));
        } catch (IOException err) {
            // TODO: Handling
            err.printStackTrace();
        }
        {
            Enumeration<Object> keys = UIManager.getDefaults().keys();
            while (keys.hasMoreElements()) {
                var key = keys.nextElement();
                Object value = UIManager.get(key);
                if (value instanceof FontUIResource defaultFont) {
                    UIManager.put(key, defaultFont.deriveFont(16f));
                }
            }
        }

        var frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(700, 500);
        {
            var gridWrapper = new JPanel(new GridBagLayout());
            {
                var formPanel = new LoginForm();
                formPanel.onSubmit(event -> {
                    formPanel.setLoading(true);
                    try {
                        messenger.sendAndThen(
                            new ClientRequest("", "", new AddRequestBody(null)),
                            response -> {
                                formPanel.setLoading(false);
                            }
                        );
                    } catch (IOException err) {
                        // TODO: Handle
                        err.printStackTrace();
                    }
                });
                gridWrapper.add(formPanel);
            }
            frame.add(gridWrapper);
        }
        frame.setVisible(true);
    }
}
