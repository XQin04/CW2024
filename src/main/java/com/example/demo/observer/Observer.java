package com.example.demo.observer;

/**
 * Defines the Observer interface to be implemented by any class
 * that wishes to receive updates from an {@link Observable} object.
 * <p>
 * This interface is a core component of the Observer design pattern,
 * enabling a one-to-many dependency between objects, where multiple
 * observers can be notified of changes in the state of an observable object.
 * </p>
 */
public interface Observer {

    /**
     * Invoked whenever the observed object is updated.
     * <p>
     * This method is called by the {@link Observable} object's
     * {@code notifyObservers()} method to propagate changes.
     * </p>
     *
     * @param arg An argument passed to the {@link Observable#notifyObservers(Object)} method.
     *            This can be used to convey information about the nature of the change.
     */
    void update(Object arg);
}
