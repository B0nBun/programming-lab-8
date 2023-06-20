package itmo.app.client.pages;

import itmo.app.client.Client;
import itmo.app.client.components.TranslatedButton;
import itmo.app.client.components.VehiclesTable;
import itmo.app.shared.clientrequest.ClientRequest;
import itmo.app.shared.clientrequest.requestbody.ClearRequestBody;
import itmo.app.shared.clientrequest.requestbody.GetRequestBody;
import java.awt.Component;
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
                    this.table.updateRows(message.newCollection);
                });
            this.add(scrollPane, scrollPane.constraints);
        }
        {
            var sidePanel = new JPanel(new GridBagLayout());
            // sidePanel.setPreferredSize(new Dimension(100, -1));
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
                var buttonConstraints = new GridBagConstraints();
                buttonConstraints.fill = GridBagConstraints.HORIZONTAL;
                buttonConstraints.anchor = GridBagConstraints.PAGE_START;
                buttonConstraints.weightx = 0;
                buttonConstraints.weighty = 0;
                buttonConstraints.gridx = 0;
                buttonConstraints.gridy = 0;
                buttonConstraints.gridheight = 1;
                buttonConstraints.gridwidth = 1;
                sidePanel.add(updateButton, buttonConstraints);
            }
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
                var buttonConstraints = new GridBagConstraints();
                buttonConstraints.fill = GridBagConstraints.HORIZONTAL;
                buttonConstraints.anchor = GridBagConstraints.PAGE_START;
                buttonConstraints.weightx = 0;
                buttonConstraints.weighty = 0;
                buttonConstraints.gridx = 0;
                buttonConstraints.gridy = 1;
                buttonConstraints.gridheight = 1;
                buttonConstraints.gridwidth = 1;
                sidePanel.add(updateButton, buttonConstraints);
            }

            var sidePanelConstraints = new GridBagConstraints();
            sidePanelConstraints.fill = GridBagConstraints.HORIZONTAL;
            sidePanelConstraints.anchor = GridBagConstraints.PAGE_START;
            sidePanelConstraints.weightx = 0;
            sidePanelConstraints.weighty = 0;
            sidePanelConstraints.gridx = 1;
            sidePanelConstraints.gridy = 0;
            sidePanelConstraints.gridheight = 1;
            sidePanelConstraints.gridwidth = 1;
            this.add(sidePanel, sidePanelConstraints);
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
            this.constraints.gridy = 0;
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
