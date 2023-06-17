package itmo.app.shared.clientrequest;

import itmo.app.shared.clientrequest.requestbody.RequestBody;
import java.io.Serializable;
import java.util.UUID;

public class ClientRequest implements Serializable {

    public final UUID uuid;
    public final String login;
    public final String password;
    public final RequestBody body;

    public ClientRequest(String login, String password, RequestBody body) {
        this.uuid = UUID.randomUUID();
        this.login = login;
        this.password = password;
        this.body = body;
    }
}
