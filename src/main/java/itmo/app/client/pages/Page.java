package itmo.app.client.pages;

import java.awt.Component;

public interface Page {
    public Component getComponent();

    public default void beforeRemoved() {}
}
