package itmo.app.client.pages;

import itmo.app.client.Client;
import java.awt.Component;
import java.awt.GridBagLayout;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class CollectionPage extends JPanel implements Page {

    public CollectionPage() {
        super();
        this.setLayout(new GridBagLayout());
        {
            var panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            {
                var label = new JLabel("This will be the page with collection table");
                panel.add(label);
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
