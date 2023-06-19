package itmo.app.shared.clientrequest.requestbody;

import java.io.Serializable;

public class ClearRequestBody implements RequestBody<ClearRequestBody.ResponseBody> {

    public static record ResponseBody(String errorMessage) implements Serializable {}

    public ResponseBody getResponseBody(RequestBody.Context context) {
        return new ResponseBody(null);
    }

    public boolean mutating() {
        return true;
    }
}
