package itmo.app.client.pages.collectionpage;

import itmo.app.client.Client;
import itmo.app.client.components.TranslatedButton;
import itmo.app.client.components.VehiclesTable;
import itmo.app.client.pages.Page;
import itmo.app.client.pages.collectionpage.components.CreationSchemaPanel;
import itmo.app.shared.clientrequest.ClientRequest;
import itmo.app.shared.clientrequest.requestbody.AddIfMaxRequestBody;
import itmo.app.shared.clientrequest.requestbody.AddRequestBody;
import itmo.app.shared.clientrequest.requestbody.ClearRequestBody;
import itmo.app.shared.clientrequest.requestbody.GetRequestBody;
import itmo.app.shared.clientrequest.requestbody.RemoveLowerRequestBody;
import itmo.app.shared.clientrequest.requestbody.RemoveRequestBody;
import itmo.app.shared.clientrequest.requestbody.UpdateRequestBody;
import itmo.app.shared.entities.Vehicle;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Consumer;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

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
            var sidepanel = new SidePanel(table, login, password);
            this.add(sidepanel, sidepanel.constraints);
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

            this.addMouseListener(
                    new MouseListener() {
                        @Override
                        public void mouseClicked(MouseEvent event) {
                            table.clearSelection();
                        }

                        public void mouseEntered(MouseEvent arg0) {}

                        public void mouseExited(MouseEvent arg0) {}

                        public void mousePressed(MouseEvent arg0) {}

                        public void mouseReleased(MouseEvent arg0) {}
                    }
                );
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

        public SidePanel(JTable table, String login, String password) {
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
             * Commands:
             * remove
             * remove-lower
             * ? group-counting-by-id
             */
            {
                // TODO: Translate dialog title
                var addButton = new TranslatedButton("add");
                addButton.addActionListener(_action -> {
                    var dialog = new JDialog(Client.frame, "Add", true);
                    Consumer<Vehicle.CreationSchema> listener = creationSchema -> {
                        Client.messenger.sendAndThen(
                            new ClientRequest<>(
                                login,
                                password,
                                new AddRequestBody(creationSchema)
                            ),
                            response -> {
                                if (response.body.errorMessage() != null) {
                                    Client.showErrorNotification(
                                        "error: " + response.body.errorMessage()
                                    );
                                } else {
                                    Client.showSuccessNotification("success");
                                    dialog.dispose();
                                }
                            },
                            error -> {
                                Client.showErrorNotification(
                                    "error: " + error.getMessage()
                                );
                            }
                        );
                    };
                    dialog
                        .getContentPane()
                        .add(
                            new CreationSchemaPanel(
                                dialog,
                                login,
                                password,
                                listener,
                                Optional.empty()
                            )
                        );
                    dialog.pack();
                    dialog.setVisible(true);
                });
                this.add(addButton, new ButtonConstraints());
            }
            {
                var addIfMaxButton = new TranslatedButton("add_if_max");
                addIfMaxButton.addActionListener(_action -> {
                    var dialog = new JDialog(Client.frame, "Add", true);
                    Consumer<Vehicle.CreationSchema> listener = creationSchema -> {
                        Client.messenger.sendAndThen(
                            new ClientRequest<>(
                                login,
                                password,
                                new AddIfMaxRequestBody(creationSchema)
                            ),
                            response -> {
                                if (response.body.errorMessage() != null) {
                                    Client.showErrorNotification(
                                        "error: " + response.body.errorMessage()
                                    );
                                } else {
                                    Client.showSuccessNotification("success");
                                    dialog.dispose();
                                }
                            },
                            error -> {
                                Client.showErrorNotification(
                                    "error: " + error.getMessage()
                                );
                            }
                        );
                    };
                    dialog
                        .getContentPane()
                        .add(
                            new CreationSchemaPanel(
                                dialog,
                                login,
                                password,
                                listener,
                                Optional.empty()
                            )
                        );
                    dialog.pack();
                    dialog.setVisible(true);
                });
                this.add(addIfMaxButton, new ButtonConstraints());
            }
            {
                var updateButton = new TranslatedButton("update");
                updateButton.setEnabled(false);
                table
                    .getSelectionModel()
                    .addListSelectionListener(selectionEvent -> {
                        int row = table.getSelectedRow();
                        if (row == -1) {
                            updateButton.setEnabled(false);
                        } else {
                            Vehicle selected = Vehicle.fromTable(table, row);
                            if (selected.createdBy().equals(login)) {
                                updateButton.setEnabled(true);
                            } else {
                                updateButton.setEnabled(false);
                            }
                        }
                    });

                updateButton.addActionListener(_action -> {
                    var dialog = new JDialog(Client.frame, "Update", true);
                    Vehicle selectedVehicle = Vehicle.fromTable(
                        table,
                        table.getSelectedRow()
                    );
                    Consumer<Vehicle.CreationSchema> listener = creationSchema -> {
                        Client.messenger.sendAndThen(
                            new ClientRequest<>(
                                login,
                                password,
                                new UpdateRequestBody(
                                    Vehicle.fromCreationSchema(
                                        selectedVehicle.id(),
                                        selectedVehicle.createdBy(),
                                        selectedVehicle.creationDate(),
                                        creationSchema
                                    )
                                )
                            ),
                            response -> {
                                if (response.body.errorMessage() != null) {
                                    Client.showErrorNotification(
                                        "error: " + response.body.errorMessage()
                                    );
                                } else {
                                    Client.showSuccessNotification("success");
                                    dialog.dispose();
                                }
                            },
                            error -> {
                                Client.showErrorNotification(
                                    "error: " + error.getMessage()
                                );
                            }
                        );
                    };
                    dialog
                        .getContentPane()
                        .add(
                            new CreationSchemaPanel(
                                dialog,
                                login,
                                password,
                                listener,
                                Optional.of(selectedVehicle)
                            )
                        );
                    dialog.pack();
                    dialog.setVisible(true);
                });
                this.add(updateButton, new ButtonConstraints());
            }
            {
                var removeButton = new TranslatedButton("remove");
                removeButton.setEnabled(false);
                removeButton.addActionListener(_action -> {
                    var dialog = new JDialog(Client.frame, "Update", true);
                    Vehicle selectedVehicle = Vehicle.fromTable(
                        table,
                        table.getSelectedRow()
                    );
                    int id = selectedVehicle.id();
                    Client.messenger.sendAndThen(
                        new ClientRequest<>(login, password, new RemoveRequestBody(id)),
                        response -> {
                            if (response.body.errorMessage() != null) {
                                Client.showErrorNotification(
                                    "error: " + response.body.errorMessage()
                                );
                            } else {
                                Client.showSuccessNotification("success");
                                dialog.dispose();
                            }
                        },
                        error -> {
                            Client.showErrorNotification("error: " + error.getMessage());
                        }
                    );
                });
                table
                    .getSelectionModel()
                    .addListSelectionListener(selectionEvent -> {
                        int row = table.getSelectedRow();
                        if (row == -1) {
                            removeButton.setEnabled(false);
                        } else {
                            Vehicle selected = Vehicle.fromTable(table, row);
                            if (selected.createdBy().equals(login)) {
                                removeButton.setEnabled(true);
                            } else {
                                removeButton.setEnabled(false);
                            }
                        }
                    });
                this.add(removeButton, new ButtonConstraints());
            }
            {
                var removeLowerButton = new TranslatedButton("remove-lower");
                removeLowerButton.addActionListener(_action -> {
                    var dialog = new JDialog(Client.frame, "Add", true);
                    Consumer<Vehicle.CreationSchema> listener = creationSchema -> {
                        Client.messenger.sendAndThen(
                            new ClientRequest<>(
                                login,
                                password,
                                new RemoveLowerRequestBody(creationSchema)
                            ),
                            response -> {
                                if (response.body.errorMessage() != null) {
                                    Client.showErrorNotification(
                                        "error: " + response.body.errorMessage()
                                    );
                                } else {
                                    Client.showSuccessNotification("success");
                                    dialog.dispose();
                                }
                            },
                            error -> {
                                Client.showErrorNotification(
                                    "error: " + error.getMessage()
                                );
                            }
                        );
                    };
                    dialog
                        .getContentPane()
                        .add(
                            new CreationSchemaPanel(
                                dialog,
                                login,
                                password,
                                listener,
                                Optional.empty()
                            )
                        );
                    dialog.pack();
                    dialog.setVisible(true);
                });
                this.add(removeLowerButton, new ButtonConstraints());
            }
            {
                var clearButton = new TranslatedButton("clear");
                clearButton.addActionListener(_action -> {
                    Client.messenger.sendAndThen(
                        new ClientRequest<>(login, password, new ClearRequestBody()),
                        response -> {
                            String error = response.body.errorMessage();
                            if (error != null) Client.showErrorNotification(error);
                        },
                        error -> {
                            Client.showErrorNotification("error: " + error.getMessage());
                        }
                    );
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
