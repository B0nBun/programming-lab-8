package itmo.app.client.components;

import itmo.app.client.LocaleService;
import javax.swing.JButton;

public class TranslatedButton extends JButton {

    private String key;

    public TranslatedButton(String key) {
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
        super.setText(LocaleService.translate(key));
    }
}
