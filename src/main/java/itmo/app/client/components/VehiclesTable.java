package itmo.app.client.components;

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

    public VehiclesTable(List<Vehicle> vehicles) {
        super();
        this.setModel(new VehicleTableModel(vehicles));
        EventQueue.invokeLater(() -> {
            this.updateRows(vehicles);
            var model = (VehicleTableModel) this.getModel();
            TableRowSorter<VehicleTableModel> sorter = new TableRowSorter<VehicleTableModel>(
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

    public void updateRows(List<Vehicle> vehicles) {
        EventQueue.invokeLater(() -> {
            var model = (VehicleTableModel) this.getModel();
            model.setDataVector(vehicles);
        });
    }

    public void updateRows(String[][] tabledata) {
        EventQueue.invokeLater(() -> {
            var model = (VehicleTableModel) this.getModel();
            model.setDataVector(tabledata);
        });
    }
}

class VehicleTableModel extends AbstractTableModel {

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

    Object[][] dataVector;

    public VehicleTableModel(List<Vehicle> vehicles) {
        this.dataVector = VehicleTableModel.tableDataFromVehicleCollection(vehicles);
    }

    public void setDataVector(List<Vehicle> vehicles) {
        this.dataVector = VehicleTableModel.tableDataFromVehicleCollection(vehicles);
        this.fireTableDataChanged();
    }

    public void setDataVector(Object[][] dataVector) {
        this.dataVector = dataVector;
        this.fireTableDataChanged();
    }

    @Override
    public String getColumnName(int column) {
        return VehicleTableModel.columnNames[column];
    }

    @Override
    public int getColumnCount() {
        return VehicleTableModel.columnNames.length;
    }

    @Override
    public int getRowCount() {
        return this.dataVector.length;
    }

    @Override
    public Object getValueAt(int row, int col) {
        return this.dataVector[row][col];
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        this.dataVector[row][col] = value;
        this.fireTableCellUpdated(row, col);
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return false;
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
