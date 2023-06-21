package itmo.app.client.pages.collectionpage.components;

import itmo.app.client.Client;
import itmo.app.client.components.TranslatedLabel;
import itmo.app.shared.entities.Coordinates;
import itmo.app.shared.entities.FuelType;
import itmo.app.shared.entities.Vehicle;
import itmo.app.shared.entities.VehicleType;
import itmo.app.shared.exceptions.ParsingException;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Optional;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AddOrEditPanel extends JPanel {

    public AddOrEditPanel(
        JDialog dialog,
        String login,
        String password,
        Consumer<Vehicle.CreationSchema> listener,
        Optional<Vehicle> currentVehicle
    ) {
        super();
        this.setLayout(new GridBagLayout());
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField nameField = new JTextField(
            currentVehicle.map(Vehicle::name).orElse("")
        );
        this.add(new TranslatedLabel("name"), new InnerConstraints());
        this.add(nameField, new InnerConstraints());
        this.add(Box.createRigidArea(new Dimension(0, 10)), new InnerConstraints());

        JTextField coordinatesXField = new JTextField(
            currentVehicle.map(v -> v.coordinates().x().toString()).orElse("")
        );
        this.add(new TranslatedLabel("coordinates.x"), new InnerConstraints());
        this.add(coordinatesXField, new InnerConstraints());
        this.add(Box.createRigidArea(new Dimension(0, 10)), new InnerConstraints());

        JTextField coordinatesYField = new JTextField(
            currentVehicle.map(v -> v.coordinates().y().toString()).orElse("")
        );
        this.add(new TranslatedLabel("coordinatex.y"), new InnerConstraints());
        this.add(coordinatesYField, new InnerConstraints());
        this.add(Box.createRigidArea(new Dimension(0, 10)), new InnerConstraints());

        JTextField enginePowerField = new JTextField(
            currentVehicle.map(v -> v.enginePower().toString()).orElse("")
        );
        this.add(new TranslatedLabel("engine_power"), new InnerConstraints());
        this.add(enginePowerField, new InnerConstraints());
        this.add(Box.createRigidArea(new Dimension(0, 10)), new InnerConstraints());

        JComboBox<VehicleType> vehicleTypeCombo = new VehicleType.Combo();
        this.add(new TranslatedLabel("vehicle_type"), new InnerConstraints());
        this.add(vehicleTypeCombo, new InnerConstraints());
        this.add(Box.createRigidArea(new Dimension(0, 10)), new InnerConstraints());

        JComboBox<FuelType> fuelTypeCombo = new FuelType.Combo();
        this.add(new TranslatedLabel("fuel_type"), new InnerConstraints());
        this.add(fuelTypeCombo, new InnerConstraints());
        this.add(Box.createRigidArea(new Dimension(0, 10)), new InnerConstraints());

        var button = new JButton(currentVehicle.isEmpty() ? "add" : "update");
        button.addActionListener(_action -> {
            try {
                String name = Vehicle.fields.name.fromString(nameField.getText(), "name");
                Coordinates coordinates = new Coordinates(
                    Coordinates.fields.x.fromString(
                        coordinatesXField.getText(),
                        "coordinates.x"
                    ),
                    Coordinates.fields.y.fromString(
                        coordinatesYField.getText(),
                        "coordinates.y"
                    )
                );
                Integer enginePower = Vehicle.fields.enginePower.fromString(
                    enginePowerField.getText(),
                    "engine_power"
                );
                VehicleType type = (VehicleType) vehicleTypeCombo.getSelectedItem();
                FuelType fuelType = (FuelType) fuelTypeCombo.getSelectedItem();
                var vehicleSchema = new Vehicle.CreationSchema(
                    name,
                    coordinates,
                    enginePower,
                    type,
                    fuelType
                );
                listener.accept(vehicleSchema);
            } catch (ParsingException err) {
                Client.showErrorNotification(err.getMessage());
            }
        });
        this.add(button, new InnerConstraints());
    }

    private class InnerConstraints extends GridBagConstraints {

        private static int counter = 0;

        public InnerConstraints() {
            super();
            this.fill = GridBagConstraints.HORIZONTAL;
            this.gridheight = 1;
            this.gridwidth = 1;
            this.gridx = 0;
            this.gridy = InnerConstraints.counter;
            InnerConstraints.counter++;
            this.weightx = 1;
            this.weighty = 0;
        }
    }
}
