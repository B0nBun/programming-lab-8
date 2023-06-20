package itmo.app.client.pages;

import itmo.app.client.Client;
import itmo.app.client.components.TranslatedButton;
import itmo.app.client.components.VehiclesTable;
import itmo.app.shared.clientrequest.ClientRequest;
import itmo.app.shared.clientrequest.requestbody.GetRequestBody;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

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
            var sidepanel = new SidePanel(login, password);
            this.add(sidepanel, sidepanel.constraints);
        }
    }

    private static class VehiclesScrollPane extends JScrollPane {

        GridBagConstraints constraints = new GridBagConstraints();

        public VehiclesScrollPane(VehiclesTable table) {
            super(table);
            table.setModel(
                new DefaultTableModel() {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                }
            );

            this.constraints.fill = GridBagConstraints.BOTH;
            this.constraints.weightx = 1;
            this.constraints.weighty = 1;
            this.constraints.gridx = 0;
            this.constraints.gridy = 0;
            this.constraints.gridheight = 1;
            this.constraints.gridwidth = 1;
        }
    }

    private static class SidePanel extends JPanel {

        private class ButtonConstraints extends GridBagConstraints {

            private static int counter = 0;

            public ButtonConstraints() {
                super();
                this.fill = GridBagConstraints.HORIZONTAL;
                this.anchor = GridBagConstraints.PAGE_START;
                this.weightx = 0;
                this.weighty = 0;
                this.gridx = 0;
                this.gridy = counter;
                counter++;
                this.gridheight = 1;
                this.gridwidth = 1;
            }
        }

        public final GridBagConstraints constraints = new GridBagConstraints();

        public SidePanel(String login, String password) {
            super();
            this.constraints.fill = GridBagConstraints.HORIZONTAL;
            this.constraints.anchor = GridBagConstraints.PAGE_START;
            this.constraints.weightx = 0;
            this.constraints.weighty = 0;
            this.constraints.gridx = 1;
            this.constraints.gridy = 0;
            this.constraints.gridheight = 1;
            this.constraints.gridwidth = 1;
            this.setLayout(new GridBagLayout());
            /**
             * add
             * add-if-max
             * update
             * remove
             * remove-lower
             * clear
             * ? group-counting-by-id
             */
            {
                var addButton = new TranslatedButton("add");
                addButton.addActionListener(_action -> {
                    System.out.println("add button");
                });
                this.add(addButton, new ButtonConstraints());
            }
            {
                var addIfMaxButton = new TranslatedButton("add_if_max");
                addIfMaxButton.addActionListener(_action -> {
                    System.out.println("add-if-max");
                });
                this.add(addIfMaxButton, new ButtonConstraints());
            }
            {
                var updateButton = new TranslatedButton("update");
                updateButton.addActionListener(_action -> {
                    System.out.println("update");
                });
                this.add(updateButton, new ButtonConstraints());
            }
            {
                var removeButton = new TranslatedButton("remove");
                removeButton.addActionListener(_action -> {
                    System.out.println("remove");
                });
                this.add(removeButton, new ButtonConstraints());
            }
            {
                var removeLowerButton = new TranslatedButton("remove-lower");
                removeLowerButton.addActionListener(_action -> {
                    System.out.println("remove-lower");
                });
                this.add(removeLowerButton, new ButtonConstraints());
            }
            {
                var clearButton = new TranslatedButton("clear");
                clearButton.addActionListener(_action -> {
                    System.out.println("clear");
                });
                this.add(clearButton, new ButtonConstraints());
            }
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
