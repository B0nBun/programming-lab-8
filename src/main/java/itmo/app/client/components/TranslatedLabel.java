package itmo.app.client.components;

import itmo.app.client.LocaleService;
import java.util.MissingResourceException;
import javax.swing.JLabel;

public class TranslatedLabel extends JLabel {

    private String key;

    public TranslatedLabel(String key) {
        super();
        this.key = key;
        this.setText(key);
        // Memory leak on component's removal, which I won't bother with
        // because Swing is a wonderful library, which doesn't allow shit
        LocaleService.onLocaleChange((l, r) -> {
            this.setText(this.key);
        });
    }

    @Override
    public void setText(String key) {
        this.key = key;
        try {
            super.setText(LocaleService.translate(key));
        } catch (MissingResourceException err) {
            super.setText(key);
        }
    }
}
