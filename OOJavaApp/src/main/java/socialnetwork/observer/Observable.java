package socialnetwork.observer;


import socialnetwork.events.Event;

/**
 * Interface for the observable object.
 * @param <E>
 */
public interface Observable <E extends Event> {

    void addObserver(Observer<E> e);
    void removeObserver(Observer<E> e);
    void notifyObservers(E t);

}
