package itmo.app.shared.clientrequest.requestbody;

import java.io.Serializable;
import java.util.UUID;

public record DeleteRequestBody(UUID vehicleUUID)
    implements RequestBody<DeleteRequestBody.ResponseBody> {
    public static record ResponseBody(String errorMessage) implements Serializable {}

    public ResponseBody getResponseBody() {
        return new ResponseBody(null);
    }
}
