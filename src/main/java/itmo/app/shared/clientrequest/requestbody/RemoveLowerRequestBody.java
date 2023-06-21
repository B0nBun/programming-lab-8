package itmo.app.shared.clientrequest.requestbody;

import itmo.app.server.DataSource;
import itmo.app.shared.entities.Vehicle;
import java.io.Serializable;
import java.sql.SQLException;

public record RemoveLowerRequestBody(Vehicle.CreationSchema checkingElement)
    implements RequestBody<RemoveLowerRequestBody.ResponseBody> {
    public static record ResponseBody(String errorMessage) implements Serializable {}

    public ResponseBody getResponseBody(RequestBody.Context context) {
        try {
            DataSource.Vehicles.removeLower(context.login(), checkingElement);
            return new ResponseBody(null);
        } catch (SQLException err) {
            return new ResponseBody(err.getMessage());
        }
    }

    public boolean mutating() {
        return true;
    }
}
