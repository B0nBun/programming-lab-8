package itmo.app.client.components;

import itmo.app.client.components.VehiclesTable.Filters;
import itmo.app.shared.entities.Vehicle;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;

public class VehiclesTable extends JTable {

    public static class Filters {

        String id = null;
        String name = null;
        String createdBy = null;
        String coordinatesX = null;
        String coordinatesY = null;
        String creationDate = null;
        String enginePower = null;
        String vehicleType = null;
        String fuelType = null;

        public Filters withId(String id) {
            this.id = id.length() == 0 ? null : id;
            return this;
        }

        public Filters withName(String name) {
            this.name = name.length() == 0 ? null : name;
            return this;
        }

        public Filters withCreatedBy(String createdBy) {
            this.createdBy = createdBy.length() == 0 ? null : createdBy;
            return this;
        }

        public Filters withCoordinatesX(String coordinatesX) {
            this.coordinatesX = coordinatesX.length() == 0 ? null : coordinatesX;
            return this;
        }

        public Filters withCoordinatesY(String coordinatesY) {
            this.coordinatesY = coordinatesY.length() == 0 ? null : coordinatesY;
            return this;
        }

        public Filters withCreationDate(String creationDate) {
            this.creationDate = creationDate.length() == 0 ? null : creationDate;
            return this;
        }

        public Filters withEnginePower(String enginePower) {
            this.enginePower = enginePower.length() == 0 ? null : enginePower;
            return this;
        }

        public Filters withVehicleType(String vehicleType) {
            this.vehicleType = vehicleType.length() == 0 ? null : vehicleType;
            return this;
        }

        public Filters withFuelType(String fuelType) {
            this.fuelType = fuelType.length() == 0 ? null : fuelType;
            return this;
        }
    }

    public VehiclesTable(List<Vehicle> vehicles) {
        super();
        this.setModel(new VehiclesTableModel(vehicles));
        EventQueue.invokeLater(() -> {
            this.updateVehicles(vehicles);
            var model = (VehiclesTableModel) this.getModel();
            TableRowSorter<VehiclesTableModel> sorter = new TableRowSorter<VehiclesTableModel>(
                model
            );
            List<RowSorter.SortKey> sortKeys = new ArrayList<>();
            sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
            sortKeys.add(new RowSorter.SortKey(1, SortOrder.UNSORTED));
            sortKeys.add(new RowSorter.SortKey(2, SortOrder.UNSORTED));
            sortKeys.add(new RowSorter.SortKey(3, SortOrder.UNSORTED));
            sortKeys.add(new RowSorter.SortKey(4, SortOrder.UNSORTED));
            sortKeys.add(new RowSorter.SortKey(5, SortOrder.UNSORTED));
            sortKeys.add(new RowSorter.SortKey(6, SortOrder.UNSORTED));
            sortKeys.add(new RowSorter.SortKey(7, SortOrder.UNSORTED));
            sortKeys.add(new RowSorter.SortKey(8, SortOrder.UNSORTED));
            sorter.setSortKeys(sortKeys);
            this.setRowSorter(sorter);
        });
    }

    public void updateVehicles(List<Vehicle> vehicles) {
        EventQueue.invokeLater(() -> {
            var model = (VehiclesTableModel) this.getModel();
            model.setVehicles(vehicles);
        });
    }

    public void updateFilters(Filters filters) {
        EventQueue.invokeLater(() -> {
            var model = (VehiclesTableModel) this.getModel();
            model.applyFilters(filters);
        });
    }
}

class VehiclesTableModel extends AbstractTableModel {

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

    List<Vehicle> vehiclesData;
    Object[][] filteredVector;
    VehiclesTable.Filters filters = new Filters();

    public VehiclesTableModel(List<Vehicle> vehicles) {
        this.vehiclesData = vehicles;
        this.filteredVector =
            VehiclesTableModel.filteredDataFromVehicles(vehicles, this.filters);
    }

    public void applyFilters(Filters filters) {
        this.filters = filters;
        this.filteredVector =
            VehiclesTableModel.filteredDataFromVehicles(this.vehiclesData, this.filters);
        this.fireTableDataChanged();
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehiclesData = vehicles;
        this.filteredVector =
            VehiclesTableModel.filteredDataFromVehicles(vehicles, this.filters);
        this.fireTableDataChanged();
    }

    @Override
    public String getColumnName(int column) {
        return VehiclesTableModel.columnNames[column];
    }

    @Override
    public int getColumnCount() {
        return VehiclesTableModel.columnNames.length;
    }

    @Override
    public int getRowCount() {
        return this.filteredVector.length;
    }

    @Override
    public Object getValueAt(int row, int col) {
        return this.filteredVector[row][col];
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        this.filteredVector[row][col] = value;
        this.fireTableCellUpdated(row, col);
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return false;
    }

    private static String[][] filteredDataFromVehicles(
        Collection<Vehicle> vehicles,
        VehiclesTable.Filters filters
    ) {
        Stream<String[]> stream = vehicles
            .stream()
            .filter(vehicle -> {
                System.out.println(filters.vehicleType + " " + vehicle.type().toString());
                return (
                    (filters.id == null || filters.id.equals(vehicle.id().toString())) &&
                    (filters.name == null || filters.name.equals(vehicle.name())) &&
                    (
                        filters.createdBy == null ||
                        filters.createdBy.equals(vehicle.createdBy().toString())
                    ) &&
                    (
                        filters.coordinatesX == null ||
                        filters.coordinatesX.equals(vehicle.coordinates().x().toString())
                    ) &&
                    (
                        filters.coordinatesY == null ||
                        filters.coordinatesY.equals(vehicle.coordinates().y().toString())
                    ) &&
                    (
                        filters.creationDate == null ||
                        filters.creationDate.equals(vehicle.creationDate().toString())
                    ) &&
                    (
                        filters.enginePower == null ||
                        filters.enginePower.equals(vehicle.enginePower().toString())
                    ) &&
                    (
                        filters.vehicleType == null ||
                        filters.vehicleType.equals(vehicle.type().toString())
                    ) &&
                    (
                        filters.fuelType == null ||
                        filters.fuelType.equals(vehicle.fuelType().toString())
                    )
                );
            })
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
