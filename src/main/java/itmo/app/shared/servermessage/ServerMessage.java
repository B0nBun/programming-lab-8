package itmo.app.shared.servermessage;

import java.io.Serializable;
import java.util.function.Consumer;
import java.util.function.Function;

public interface ServerMessage extends Serializable {
    public static <R> R match(
        ServerMessage message,
        Function<ServerResponse<Serializable>, R> responseCase,
        Function<ServerCollectionUpdate, R> collectionUpdateCase
    ) {
        if (message instanceof ServerResponse<?> response) {
            @SuppressWarnings({ "unchecked" })
            var castedResponse = (ServerResponse<Serializable>) response;
            return responseCase.apply(castedResponse);
        } else if (message instanceof ServerCollectionUpdate collectionUpdate) {
            return collectionUpdateCase.apply(collectionUpdate);
        } else {
            throw new UnsupportedOperationException(
                "ServerMessage.match doesn't support " +
                message.getClass().getName() +
                " class"
            );
        }
    }

    public static void match(
        ServerMessage message,
        Consumer<ServerResponse<Serializable>> responseCase,
        Consumer<ServerCollectionUpdate> collectionUpdateCase
    ) {
        ServerMessage.match(
            message,
            response -> {
                responseCase.accept(response);
                return null;
            },
            collectionUpdate -> {
                collectionUpdateCase.accept(collectionUpdate);
                return null;
            }
        );
    }
}
