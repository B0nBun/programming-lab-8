package itmo.app.shared.clientrequest.requestbody;

import java.io.Serializable;

public record AddRequestBody(Void newElement)
    implements RequestBody<AddRequestBody.ResponseBody> {
    public static record ResponseBody(String errorMessage) implements Serializable {}

    public ResponseBody getResponseBody() {
        return new ResponseBody(null);
    }
}
