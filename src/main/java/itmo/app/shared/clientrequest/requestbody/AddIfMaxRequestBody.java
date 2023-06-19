package itmo.app.shared.clientrequest.requestbody;

import java.io.Serializable;

public record AddIfMaxRequestBody(Void newElement)
    implements RequestBody<AddIfMaxRequestBody.ResponseBody> {
    public static record ResponseBody(String errorMessage) implements Serializable {}

    public ResponseBody getResponseBody(RequestBody.Context context) {
        return new ResponseBody(null);
    }
}
