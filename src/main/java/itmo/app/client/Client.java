package itmo.app.client;

import itmo.app.client.pages.LoginAndRegistrationPage;
import itmo.app.client.pages.Page;
import itmo.app.shared.clientrequest.ClientRequest;
import itmo.app.shared.clientrequest.requestbody.RequestBody;
import java.awt.Color;
import java.awt.Point;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Arrays;
import java.util.Enumeration;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.plaf.FontUIResource;

public class Client {

    @SuppressWarnings("unused")
    private static void mesengerTest() throws IOException {
        SocketAddress addr = new InetSocketAddress("127.0.0.1", 2000);
        @SuppressWarnings({ "resource" })
        var messenger = new Messenger(addr);
        messenger.onCollectionUpdate(collectionUpdate -> {
            System.out.println("Collection update, listener 1");
        });
        messenger.onCollectionUpdate(collectionUpdate -> {
            System.out.println(
                "Collection: " + Arrays.toString(collectionUpdate.newCollection.toArray())
            );
        });
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException err) {}
            messenger.sendAndThen(
                new ClientRequest("login", "password", new RequestBody() {}),
                response -> {
                    System.out.println("Client got the response, handling...");
                    System.out.println("Handled response: " + response.body.toString());
                }
            );
        }
    }

    public static final JFrame frame = new JFrame();
    public static Page currentPage = null;

    // TODO: Use EventQueue.invokeLater
    public static void main(String... args) {
        {
            Enumeration<Object> keys = UIManager.getDefaults().keys();
            while (keys.hasMoreElements()) {
                var key = keys.nextElement();
                Object value = UIManager.get(key);
                if (value instanceof FontUIResource defaultFont) {
                    UIManager.put(key, defaultFont.deriveFont(16f));
                }
            }
        }

        Client.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Client.frame.setSize(700, 500);
        Client.setPage(new LoginAndRegistrationPage());
        Client.frame.setVisible(true);
    }

    public static void setPage(Page page) {
        if (Client.currentPage != null) {
            Client.frame.remove(Client.currentPage.getComponent());
        }
        Client.currentPage = page;
        Client.frame.add(page.getComponent());
        Client.frame.revalidate();
        Client.frame.repaint();
    }

    private static Point getPositionForPopup() {
        var framel = Client.frame.getLocation();
        return new Point(framel.x + 10, framel.y + 50);
    }

    private static Timer currentPopupTimer = null;
    private static final int POPUP_TIMING = 2000;

    public static void showNotification(String message, Color bgColor) {
        var pf = PopupFactory.getSharedInstance();
        var panel = new JPanel();
        panel.setBackground(bgColor);
        {
            var label = new JLabel(message);
            panel.add(label);
        }
        Point location = Client.getPositionForPopup();
        Popup popup = pf.getPopup(Client.frame, panel, location.x, location.y);
        popup.show();
        if (Client.currentPopupTimer != null && Client.currentPopupTimer.isRunning()) {
            for (var listener : Client.currentPopupTimer.getActionListeners()) {
                listener.actionPerformed(null);
            }
        }
        Client.currentPopupTimer =
            new Timer(Client.POPUP_TIMING, _action -> popup.hide());
        Client.currentPopupTimer.setRepeats(false);
        Client.currentPopupTimer.start();
    }
}
