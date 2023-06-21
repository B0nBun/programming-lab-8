package itmo.app.shared.clientrequest.requestbody;

import itmo.app.server.DataSource;
import itmo.app.shared.entities.Vehicle;
import java.io.Serializable;
import java.sql.SQLException;

public record UpdateRequestBody(Vehicle updatedElement)
    implements RequestBody<UpdateRequestBody.ResponseBody> {
    public static record ResponseBody(String errorMessage) implements Serializable {}

    public ResponseBody getResponseBody(RequestBody.Context context) {
        try {
            DataSource.Vehicles.update(
                context.login(),
                updatedElement.id(),
                Vehicle.CreationSchema.fromVehicle(updatedElement)
            );
            return new ResponseBody(null);
        } catch (SQLException err) {
            return new ResponseBody(err.getMessage());
        }
    }

    public boolean mutating() {
        return true;
    }
}
