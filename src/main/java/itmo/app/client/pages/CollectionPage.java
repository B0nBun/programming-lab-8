package itmo.app.client.pages;

import itmo.app.client.Client;
import itmo.app.client.components.TranslatedButton;
import itmo.app.client.components.VehiclesTable;
import itmo.app.shared.clientrequest.ClientRequest;
import itmo.app.shared.clientrequest.requestbody.ClearRequestBody;
import itmo.app.shared.clientrequest.requestbody.GetRequestBody;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class CollectionPage extends JPanel implements Page {

    VehiclesTable table = new VehiclesTable(new ArrayList<>());

    Runnable unsubscribeFromCollectionUpdate;

    public CollectionPage(String login, String password) {
        super();
        this.setLayout(new GridBagLayout());
        this.setBackground(Color.pink);

        {
            var headerPanel = new JPanel(new GridBagLayout());
            headerPanel.setPreferredSize(new Dimension(-1, 100));
            {
                var updateButton = new TranslatedButton("send update request");
                updateButton.addActionListener(_action -> {
                    Client.messenger.sendAndThen(
                        new ClientRequest<>(login, password, new ClearRequestBody()),
                        response -> {},
                        error -> {
                            Client.showErrorNotification("error: " + error.getMessage());
                        }
                    );
                });
                headerPanel.add(updateButton);
            }
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
            var scrollPane = new CollectionPage.VehiclesScrollPane(this.table);
            Client.messenger.sendAndThen(
                new ClientRequest<>(login, password, new GetRequestBody()),
                response -> {
                    this.table.updateRows(response.body);
                },
                error -> {
                    Client.showErrorNotification("error: " + error.getMessage());
                }
            );
            this.unsubscribeFromCollectionUpdate =
                Client.messenger.onCollectionUpdate(message -> {
                    System.out.println("Update");
                    this.table.updateRows(message.newCollection);
                });
            this.add(scrollPane, scrollPane.constraints);
        }
    }

    private static class VehiclesScrollPane extends JScrollPane {

        GridBagConstraints constraints = new GridBagConstraints();

        public VehiclesScrollPane(VehiclesTable table) {
            super(table);
            this.constraints.fill = GridBagConstraints.BOTH;
            this.constraints.weightx = 1;
            this.constraints.weighty = 1;
            this.constraints.gridx = 0;
            this.constraints.gridy = 2;
            this.constraints.gridheight = 1;
            this.constraints.gridwidth = 1;
        }
    }

    @Override
    public void beforeRemoved() {
        this.unsubscribeFromCollectionUpdate.run();
    }

    @Override
    public Component getComponent() {
        return this;
    }
}
