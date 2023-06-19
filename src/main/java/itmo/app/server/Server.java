package itmo.app.server;

import io.github.cdimascio.dotenv.Dotenv;
import itmo.app.shared.Utils;
import itmo.app.shared.clientrequest.ClientRequest;
import itmo.app.shared.clientrequest.requestbody.RequestBody;
import itmo.app.shared.servermessage.ServerResponse;
import java.io.EOFException;
import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Server {

    public static final Logger logger = LoggerFactory.getLogger("itmo.app.server.logger");

    public static void main(String... args) throws IOException {
        Optional<String> psqlUrl = Server.loadDotenvDatabaseUrl();
        if (args.length >= 1) {
            psqlUrl = Optional.of(args[0]);
        }
        if (psqlUrl.isEmpty()) {
            Server.logger.warn(
                "Url to database is unknown. Either set the VEHICLES_DATABASE_URL environment variable or provide the url in the command line arguments"
            );
            return;
        }
        try {
            DataSource.instantiateDatabase(psqlUrl.get());
            Server.logger.info("Connected to the database: {}", psqlUrl);
        } catch (SQLException err) {
            Server.logger.error("Error in database instantiation: {}", err.getMessage());
            return;
        }

        @SuppressWarnings({ "resource" })
        var server = new ServerSocket(2000);
        Server.logger.info("Server started");
        while (true) {
            Socket client = server.accept();
            Server.logger.info(
                "Client connected: " + client.getInetAddress() + ":" + client.getPort()
            );
            new Thread(new ClientHandlingRunnable(client)).start();
        }
    }

    private static Optional<String> loadDotenvDatabaseUrl() {
        try {
            return Optional.of(Dotenv.load().get("VEHICLES_DATABASE_URL"));
            // There is a DotenvException thrown actually, but for some reason java doesn't see it
            // so I had to use Throwable
        } catch (Throwable err) {
            Server.logger.error("Couldn't load dotenv file: {}", err.getMessage());
            return Optional.empty();
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
        return new ServerResponse<T>(
            request.uuid,
            request.body.getResponseBody(
                new RequestBody.Context(request.login, request.password)
            )
        );
    }

    @Override
    public void run() {
        while (true) {
            try {
                var request = (ClientRequest<?>) Utils.readObjectFromInputStream(
                    this.client.getInputStream()
                );
                Server.logger.info(
                    "Got request from " + client.getInetAddress() + ":" + client.getPort()
                );
                var response = this.handleRequest(request);
                Utils.writeObjectToOutputStream(response, this.client.getOutputStream());
            } catch (EOFException err) {
                Server.logger.info(
                    "Client disconnected: " +
                    this.client.getInetAddress() +
                    ":" +
                    this.client.getPort()
                );
                break;
            } catch (IOException | ClassNotFoundException err) {
                Server.logger.error("Couldn't read the request: " + err.getMessage());
                err.printStackTrace();
            }
        }
    }
}
