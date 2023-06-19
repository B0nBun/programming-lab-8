package itmo.app.shared.servermessage;

import itmo.app.shared.entities.Vehicle;
import java.util.Collection;

public class ServerCollectionUpdate implements ServerMessage {

    public final Collection<Vehicle> newCollection;

    public ServerCollectionUpdate(Collection<Vehicle> newCollection) {
        this.newCollection = newCollection;
    }
}
