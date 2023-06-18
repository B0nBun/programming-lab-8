package itmo.app.server;

import itmo.app.shared.Utils;
import itmo.app.shared.clientrequest.ClientRequest;
import itmo.app.shared.servermessage.ServerResponse;
import java.io.EOFException;
import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String... args) throws IOException {
        @SuppressWarnings({ "resource" })
        var server = new ServerSocket(2000);
        while (true) {
            Socket client = server.accept();
            new Thread(new ClientHandlingRunnable(client)).start();
        }
    }
}

class ClientHandlingRunnable implements Runnable {

    final Socket client;

    public ClientHandlingRunnable(Socket client) {
        this.client = client;
    }

    private <T extends Serializable> ServerResponse<T> handleRequest(
        ClientRequest<T> request
    ) {
        return new ServerResponse<T>(request.uuid, request.body.getResponseBody());
    }

    @Override
    public void run() {
        while (true) {
            try {
                var request = (ClientRequest<?>) Utils.readObjectFromInputStream(
                    this.client.getInputStream()
                );
                var response = this.handleRequest(request);
                Utils.writeObjectToOutputStream(response, this.client.getOutputStream());
            } catch (EOFException err) {
                System.out.println(
                    "Client disconnected: " +
                    this.client.getInetAddress() +
                    ":" +
                    this.client.getPort()
                );
                break;
            } catch (IOException | ClassNotFoundException err) {
                System.out.println("Couldn't read the request: " + err.getMessage());
                err.printStackTrace();
            }
        }
    }
}
