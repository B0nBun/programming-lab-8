package itmo.app.client.components;

import itmo.app.client.LocaleService;
import java.util.MissingResourceException;
import javax.swing.JButton;

public class TranslatedButton extends JButton {

    private String key;

    public TranslatedButton(String key) {
        super();
        this.key = key;
        this.setText(key);
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
