package itmo.app.client.pages;

import itmo.app.client.Client;
import itmo.app.client.components.TranslatedLabel;
import itmo.app.shared.clientrequest.ClientRequest;
import itmo.app.shared.clientrequest.requestbody.GetRequestBody;
import itmo.app.shared.entities.Vehicle;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class CollectionPage extends JPanel implements Page {

    public CollectionPage(String login, String password) {
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
                var button = new JButton("Load collection");
                button.addActionListener(_action -> {
                    try {
                        Client.messenger.sendAndThen(
                            new ClientRequest<ArrayList<Vehicle>>(
                                login,
                                password,
                                new GetRequestBody()
                            ),
                            response -> {
                                System.out.println(response.body);
                            }
                        );
                    } catch (IOException err) {
                        Client.showNotification(err.getMessage(), Color.pink);
                    }
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
