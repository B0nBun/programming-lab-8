package itmo.app.shared.clientrequest.requestbody;

import java.io.Serializable;

public interface RequestBody<R extends Serializable> extends Serializable {
    public static record Context(String login, String password) {}

    public R getResponseBody(Context context);

    public boolean mutating();
}
