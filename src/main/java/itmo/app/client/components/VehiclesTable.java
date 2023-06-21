package itmo.app.client.components;

import itmo.app.shared.entities.Vehicle;
import java.awt.EventQueue;
import java.util.Collection;
import java.util.stream.Stream;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class VehiclesTable extends JTable {

    private static String[] columnNames = {
        "id",
        "name",
        "created_by",
        "coordinates.x",
        "coordinates.y",
        "creation_date",
        "engine_power",
        "vehicle_type",
        "fuel_type",
    };

    public VehiclesTable(Collection<Vehicle> vehicles) {
        super();
        EventQueue.invokeLater(() -> {
            this.updateRows(vehicles);
        });
    }

    public void updateRows(Collection<Vehicle> vehicles) {
        this.updateRows(VehiclesTable.tableDataFromVehicleCollection(vehicles));
    }

    public void updateRows(String[][] tabledata) {
        EventQueue.invokeLater(() -> {
            var model = (DefaultTableModel) this.getModel();
            model.setRowCount(tabledata.length);
            model.setDataVector(tabledata, columnNames);
        });
    }

    private static String[][] tableDataFromVehicleCollection(
        Collection<Vehicle> vehicles
    ) {
        Stream<String[]> stream = vehicles
            .stream()
            .map(vehicle ->
                new String[] {
                    vehicle.id().toString(),
                    vehicle.name(),
                    vehicle.createdBy(),
                    vehicle.coordinates().x().toString(),
                    vehicle.coordinates().y().toString(),
                    vehicle.creationDate().toString(),
                    vehicle.enginePower().toString(),
                    vehicle.type().toString(),
                    vehicle.fuelType().toString(),
                }
            );
        return stream.toArray(String[][]::new);
    }
}
