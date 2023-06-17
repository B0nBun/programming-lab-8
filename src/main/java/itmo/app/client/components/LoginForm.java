package itmo.app.client.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Collection;
import java.util.LinkedList;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginForm extends JPanel {

    private Collection<Consumer<LoginEvent>> listeners = new LinkedList<>();
    private JTextField loginField = new JTextField();
    private JPasswordField passwordField = new JPasswordField();
    private JButton submitButton = new JButton("Submit");

    public LoginForm() {
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.setBorder(
                BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.black, 2),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
                )
            );
        {
            var headingLabel = new JLabel("Login/Register");
            headingLabel.setFont(
                headingLabel.getFont().deriveFont(20f).deriveFont(Font.BOLD)
            );
            this.add(headingLabel);
        }
        this.add(Box.createRigidArea(new Dimension(0, 10)));
        {
            var loginLabel = new JLabel("Username:");
            this.add(loginLabel);
        }
        {
            this.add(this.loginField);
            this.loginField.setPreferredSize(
                    new Dimension(350, this.loginField.getPreferredSize().height)
                );
        }
        this.add(Box.createRigidArea(new Dimension(0, 10)));
        {
            var passwordLabel = new JLabel("Password:");
            this.add(passwordLabel);
        }
        {
            this.add(this.passwordField);
            this.passwordField.setPreferredSize(
                    new Dimension(350, this.passwordField.getPreferredSize().height)
                );
        }
        this.add(Box.createRigidArea(new Dimension(0, 10)));
        {
            this.submitButton.addActionListener(_action -> {
                    this.notifyListeners(
                            new LoginEvent(
                                this.loginField.getText(),
                                new String(this.passwordField.getPassword())
                            )
                        );
                });
            this.add(this.submitButton);
        }
    }

    public void setLoading(boolean value) {
        if (value) {
            this.submitButton.setText("Loading...");
        } else {
            this.submitButton.setText("Submit");
        }
        this.submitButton.setEnabled(!value);
    }

    public static record LoginEvent(String login, String password) {}

    public void onSubmit(Consumer<LoginEvent> listener) {
        this.listeners.add(listener);
    }

    private void notifyListeners(LoginEvent event) {
        for (var listener : this.listeners) {
            listener.accept(event);
        }
    }
}
