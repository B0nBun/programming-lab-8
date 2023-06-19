package itmo.app.client.pages;

import itmo.app.client.Client;
import itmo.app.client.components.TranslatedButton;
import itmo.app.client.components.TranslatedLabel;
import itmo.app.client.components.VehiclesTable;
import itmo.app.shared.clientrequest.ClientRequest;
import itmo.app.shared.clientrequest.requestbody.GetRequestBody;
import itmo.app.shared.entities.Vehicle;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class CollectionPage extends JPanel implements Page {

    VehiclesTable table = new VehiclesTable(new ArrayList<>());

    public CollectionPage(String login, String password) {
        super();
        this.setLayout(new GridBagLayout());
        this.setBackground(Color.pink);
        {
            var headerPanel = new JPanel(new GridBagLayout());
            {
                var headerConstraints = new GridBagConstraints();
                headerConstraints.fill = GridBagConstraints.HORIZONTAL;
                headerConstraints.anchor = GridBagConstraints.PAGE_START;
                headerConstraints.weightx = 1;
                headerConstraints.weighty = 0;
                headerConstraints.gridx = 0;
                headerConstraints.gridy = 0;
                headerConstraints.gridheight = 1;
                headerConstraints.gridwidth = 1;
                this.add(headerPanel, headerConstraints);
            }
            {
                var label = new TranslatedLabel("header");
                headerPanel.add(label);
            }
        }
        {
            var updateButton = new TranslatedButton("update_collection");
            updateButton.addActionListener(_action -> {
                Client.messenger.sendAndThen(
                    new ClientRequest<ArrayList<Vehicle>>(
                        login,
                        password,
                        new GetRequestBody()
                    ),
                    response -> {
                        this.table.updateRows(response.body);
                        System.out.println(response.body);
                    },
                    error -> {
                        Client.showErrorNotification("error: " + error.getMessage());
                    }
                );
            });
            {
                var buttonConstraints = new GridBagConstraints();
                buttonConstraints.fill = GridBagConstraints.HORIZONTAL;
                buttonConstraints.anchor = GridBagConstraints.PAGE_START;
                buttonConstraints.weightx = 1;
                buttonConstraints.weighty = 0;
                buttonConstraints.gridx = 0;
                buttonConstraints.gridy = 1;
                buttonConstraints.gridheight = 1;
                buttonConstraints.gridwidth = 1;
                this.add(updateButton, buttonConstraints);
            }
        }
        {
            var scrollPane = new JScrollPane(this.table);
            {
                var paneConstraints = new GridBagConstraints();
                paneConstraints.fill = GridBagConstraints.BOTH;
                paneConstraints.weightx = 1;
                paneConstraints.weighty = 1;
                paneConstraints.gridx = 0;
                paneConstraints.gridy = 2;
                paneConstraints.gridheight = 1;
                paneConstraints.gridwidth = 1;
                this.add(scrollPane, paneConstraints);
            }
        }
    }

    @Override
    public Component getComponent() {
        return this;
    }
}
