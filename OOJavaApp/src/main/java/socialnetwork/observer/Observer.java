package socialnetwork.observer;


import socialnetwork.events.Event;

/**
 * Interface for the Observer object.
 * @param <E>
 */
public interface Observer <E extends Event> {

    void update(E e);

}
