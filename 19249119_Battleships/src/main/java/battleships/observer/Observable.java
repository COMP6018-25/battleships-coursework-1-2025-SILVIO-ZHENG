package battleships.observer;

import java.beans.PropertyChangeEvent;

// Custom Observable interface: allows registering and notifying Observers on state changes
public interface Observable {

    // Registers an Observer; its update(...) will be called when the model changes
    // @param o the Observer to register
    void addObserver(Observer o);

    // Unregisters an Observer; it will no longer receive updates
    // @param o the Observer to remove
    void removeObserver(Observer o);

    // Notifies all registered Observers with a change event
    // @param evt the PropertyChangeEvent describing the update
    void notifyObservers(PropertyChangeEvent evt);
}
