package itmo.app.client.pages;

import itmo.app.client.Client;
import itmo.app.client.components.LoginForm;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.Timer;

public class LoginAndRegistrationPage extends JPanel implements Page {

    public LoginAndRegistrationPage() {
        super();
        this.setLayout(new GridBagLayout());
        {
            var formPanel = new LoginForm();
            formPanel.onSubmit(event -> {
                // TODO: Uncomment later
                // if (event.login().length() == 0 || event.password().length() == 0) {
                //     Client.showNotification(
                //         "Login and password can't be empty",
                //         Color.pink
                //     );
                //     return;
                // }
                formPanel.setLoading(true);
                var t = new Timer(
                    200,
                    _action -> {
                        formPanel.setLoading(false);
                        Client.showNotification("Logged in!", Color.green);
                        Client.setPage(new CollectionPage());
                    }
                );
                t.setRepeats(false);
                t.start();
            });
            this.add(formPanel);
        }
    }

    @Override
    public Component getComponent() {
        return this;
    }
}
