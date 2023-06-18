package itmo.app.shared.clientrequest.requestbody;

import java.io.Serializable;
import java.util.UUID;

public record UpdateRequestBody(UUID vehicleUUID, Void updatedElement)
    implements RequestBody<UpdateRequestBody.ResponseBody> {
    public static record ResponseBody(String errorMessage) implements Serializable {}

    public ResponseBody getResponseBody() {
        return new ResponseBody(null);
    }
}
