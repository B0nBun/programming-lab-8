package itmo.app.client.pages;

import itmo.app.client.Client;
import itmo.app.client.components.LoginForm;
import itmo.app.client.components.LoginForm.LoginEvent;
import itmo.app.shared.AuthResult;
import itmo.app.shared.clientrequest.ClientRequest;
import itmo.app.shared.clientrequest.requestbody.LoginRequestBody;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagLayout;
import javax.swing.JPanel;

public class LoginAndRegistrationPage extends JPanel implements Page {

    public LoginAndRegistrationPage() {
        super();
        this.setLayout(new GridBagLayout());
        {
            var formPanel = new LoginForm();

            formPanel.onSubmit(event ->
                LoginAndRegistrationPage.handleSubmit(formPanel, event)
            );
            this.add(formPanel);
        }
    }

    private static void handleSubmit(LoginForm form, LoginEvent event) {
        if (event.login().length() == 0 || event.password().length() == 0) {
            Client.showNotification("Login and password can't be empty", Color.pink);
            return;
        }
        form.setLoading(true);
        Client.messenger.sendAndThen(
            new ClientRequest<>(event.login(), event.password(), new LoginRequestBody()),
            response -> {
                if (response.body.error() != null) {
                    Client.showErrorNotification("error: " + response.body.error());
                    return;
                }
                AuthResult authResult = response.body.result();
                form.setLoading(false);
                if (authResult == AuthResult.REJECTED) {
                    Client.showErrorNotification("unauthorized");
                } else {
                    if (authResult == AuthResult.LOGGEDIN) {
                        Client.showSuccessNotification("logged_in");
                    } else if (authResult == AuthResult.REGISTERED) {
                        Client.showSuccessNotification("registered");
                    }
                    Client.setPage(new CollectionPage(event.login(), event.password()));
                }
            },
            error -> {
                Client.showErrorNotification("error: " + error.getMessage());
            }
        );
    }

    @Override
    public Component getComponent() {
        return this;
    }
}
