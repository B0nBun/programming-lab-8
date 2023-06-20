package itmo.app.client.pages.collectionpage.components;

import itmo.app.client.components.TranslatedLabel;
import itmo.app.shared.entities.Coordinates;
import itmo.app.shared.entities.FuelType;
import itmo.app.shared.entities.Vehicle;
import itmo.app.shared.entities.VehicleType;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AddPanel extends JPanel {

    private JTextField nameField = new JTextField();
    private JTextField coordinatesXField = new JTextField();
    private JTextField coordinatesYField = new JTextField();
    private JTextField enginePowerField = new JTextField();
    private JComboBox<VehicleType> vehicleTypeCombo = new VehicleType.Combo();
    private JComboBox<FuelType> fuelTypeCombo = new FuelType.Combo();

    public AddPanel(
        JDialog dialog,
        String login,
        String password,
        Consumer<Vehicle.CreationSchema> listener
    ) {
        super();
        this.setLayout(new GridBagLayout());
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        this.add(new TranslatedLabel("name"), new InnerConstraints());
        this.add(this.nameField, new InnerConstraints());
        this.add(Box.createRigidArea(new Dimension(0, 10)), new InnerConstraints());

        this.add(new TranslatedLabel("coordinates.x"), new InnerConstraints());
        this.add(this.coordinatesXField, new InnerConstraints());
        this.add(Box.createRigidArea(new Dimension(0, 10)), new InnerConstraints());

        this.add(new TranslatedLabel("coordinatex.y"), new InnerConstraints());
        this.add(this.coordinatesYField, new InnerConstraints());
        this.add(Box.createRigidArea(new Dimension(0, 10)), new InnerConstraints());

        this.add(new TranslatedLabel("engine_power"), new InnerConstraints());
        this.add(this.enginePowerField, new InnerConstraints());
        this.add(Box.createRigidArea(new Dimension(0, 10)), new InnerConstraints());

        this.add(new TranslatedLabel("vehicle_type"), new InnerConstraints());
        this.add(this.vehicleTypeCombo, new InnerConstraints());
        this.add(Box.createRigidArea(new Dimension(0, 10)), new InnerConstraints());

        this.add(new TranslatedLabel("fuel_type"), new InnerConstraints());
        this.add(this.fuelTypeCombo, new InnerConstraints());
        this.add(Box.createRigidArea(new Dimension(0, 10)), new InnerConstraints());

        var button = new JButton("add");
        button.addActionListener(_action -> {
            String name = this.nameField.getText();
            Coordinates coordinates = new Coordinates(
                Integer.parseInt(this.coordinatesXField.getText()),
                Float.parseFloat(this.coordinatesYField.getText())
            );
            Integer enginePower = Integer.parseInt(this.enginePowerField.getText());
            VehicleType type = (VehicleType) this.vehicleTypeCombo.getSelectedItem();
            FuelType fuelType = (FuelType) this.fuelTypeCombo.getSelectedItem();
            var vehicleSchema = new Vehicle.CreationSchema(
                name,
                coordinates,
                enginePower,
                type,
                fuelType
            );
            listener.accept(vehicleSchema);
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
