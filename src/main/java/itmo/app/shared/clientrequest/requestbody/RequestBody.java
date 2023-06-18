package itmo.app.shared.clientrequest.requestbody;

import java.io.Serializable;

public interface RequestBody<R extends Serializable> extends Serializable {
    public R getResponseBody();
}
