package itmo.app.shared.clientrequest.requestbody;

import itmo.app.server.DataSource;
import java.io.Serializable;
import java.sql.SQLException;

public class ClearRequestBody implements RequestBody<ClearRequestBody.ResponseBody> {

    public static record ResponseBody(String errorMessage) implements Serializable {}

    public ResponseBody getResponseBody(RequestBody.Context context) {
        try {
            DataSource.Vehicles.clear(context.login());
            return new ResponseBody(null);
        } catch (SQLException err) {
            return new ResponseBody("error: " + err.getMessage());
        }
    }

    public boolean mutating() {
        return true;
    }
}
