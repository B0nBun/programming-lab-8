package itmo.app.shared.servermessage;

import java.util.Collection;

public class ServerCollectionUpdate implements ServerMessage {

    public final Collection<Void> newCollection;

    public ServerCollectionUpdate(Collection<Void> newCollection) {
        this.newCollection = newCollection;
    }
}
