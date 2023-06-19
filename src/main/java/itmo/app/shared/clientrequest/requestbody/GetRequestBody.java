package itmo.app.shared.clientrequest.requestbody;

import itmo.app.server.DataSource;
import itmo.app.shared.entities.Vehicle;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class GetRequestBody implements RequestBody<ArrayList<Vehicle>> {

    @Override
    public ArrayList<Vehicle> getResponseBody(RequestBody.Context context) {
        return DataSource.Vehicles
            .stream()
            .collect(Collectors.toCollection(ArrayList::new));
    }
}
