package itmo.app.shared.servermessage;

import itmo.app.shared.entities.Vehicle;
import java.util.List;

public class ServerCollectionUpdate implements ServerMessage {

    public final List<Vehicle> newCollection;

    public ServerCollectionUpdate(List<Vehicle> newCollection) {
        this.newCollection = newCollection;
    }
}
