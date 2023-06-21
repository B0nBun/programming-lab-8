package itmo.app.shared.clientrequest.requestbody;

import itmo.app.server.DataSource;
import itmo.app.shared.entities.Vehicle;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.Optional;

public record AddIfMaxRequestBody(Vehicle.CreationSchema newElement)
    implements RequestBody<AddIfMaxRequestBody.ResponseBody> {
    public static record ResponseBody(String errorMessage) implements Serializable {}

    public ResponseBody getResponseBody(RequestBody.Context context) {
        try {
            Optional<Vehicle> max = DataSource.Vehicles.stream().max(Vehicle::compareTo);
            boolean shouldAdd = max
                .map(m -> m.compareToCreationSchema(newElement) < 0)
                .orElse(true);
            if (shouldAdd) {
                DataSource.Vehicles.add(context.login(), newElement);
                return new ResponseBody(null);
            } else {
                return new ResponseBody("element is not max");
            }
        } catch (SQLException err) {
            return new ResponseBody(err.getMessage());
        }
    }

    public boolean mutating() {
        return true;
    }
}
