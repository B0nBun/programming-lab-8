package itmo.app.shared.servermessage;

import java.io.Serializable;
import java.util.UUID;

public class ServerResponse<T extends Serializable> implements ServerMessage {

    public final UUID uuid;
    public final T body;

    public ServerResponse(UUID uuid, T body) {
        this.uuid = uuid;
        this.body = body;
    }
}
