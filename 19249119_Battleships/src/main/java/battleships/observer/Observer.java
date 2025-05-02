package battleships.observer;

import java.beans.PropertyChangeEvent;

// Custom Observer interface: called by Observable when its state changes
public interface Observer {

    // Called when the Observable's state changes
    // @param evt the PropertyChangeEvent describing the change
    void update(PropertyChangeEvent evt);
}
