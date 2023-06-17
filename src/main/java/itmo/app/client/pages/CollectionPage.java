package itmo.app.client.pages;

import itmo.app.client.Client;
import itmo.app.client.LocaleService;
import itmo.app.client.components.TranslatedLabel;
import java.awt.Component;
import java.awt.GridBagLayout;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

public class CollectionPage extends JPanel implements Page {

    public CollectionPage() {
        super();
        this.setLayout(new GridBagLayout());
        {
            var panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            {
                var label = new TranslatedLabel("test_string");
                panel.add(label);
            }
            {
                var languageList = new JComboBox<>(
                    LocaleService.getLanguages().toArray()
                );
                languageList.setSelectedIndex(0);
                languageList.addActionListener(_action -> {
                    var selected = (LocaleService.Language) languageList.getSelectedItem();
                    LocaleService.changeLocale(selected.locale);
                });
                panel.add(languageList);
            }
            {
                var button = new JButton("Sign out");
                button.addActionListener(_action -> {
                    Client.setPage(new LoginAndRegistrationPage());
                });
                panel.add(button);
            }
            this.add(panel);
        }
    }

    @Override
    public Component getComponent() {
        return this;
    }
}
