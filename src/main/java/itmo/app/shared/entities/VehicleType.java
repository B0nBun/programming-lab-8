package itmo.app.shared.entities;

import itmo.app.client.LocaleService;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

public enum VehicleType {
    DRONE,
    SHIP,
    BICYCLE;

    public static VehicleType fromString(String string) {
        if (string == null) return null;
        for (var value : VehicleType.values()) {
            if (value.toString().toLowerCase().equals(string.toLowerCase())) {
                return value;
            }
        }
        return null;
    }

    public static String showIndexedList(String joiner) {
        var names = Stream.of(VehicleType.values()).map(t -> t.name()).toList();
        String result = "";
        for (int i = 0; i < names.size(); i++) {
            result +=
                (i + 1) + ". " + names.get(i) + (i == names.size() - 1 ? "" : joiner);
        }
        return result;
    }

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }

    public static class Combo extends JComboBox<VehicleType> {

        public Combo(boolean allowEmpty) {
            super(allowEmpty ? valuesWithNull() : VehicleType.values());
            this.setRenderer(new VehicleTypeRenderer());

            LocaleService.onLocaleChange((locale, rb) -> {
                this.repaint();
            });
        }

        private static VehicleType[] valuesWithNull() {
            var values = new ArrayList<>(Arrays.asList(VehicleType.values()));
            values.add(0, null);
            VehicleType[] array = new VehicleType[values.size()];
            values.toArray(array);
            return array;
        }
    }
}

class VehicleTypeRenderer extends BasicComboBoxRenderer {

    @Override
    public Component getListCellRendererComponent(
        JList<?> list,
        Object value,
        int index,
        boolean isSelected,
        boolean cellHasFocus
    ) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        this.setText(value == null ? "" : LocaleService.translate(value.toString()));

        return this;
    }
}
