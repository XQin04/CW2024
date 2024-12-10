package com.example.demo.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an Observable object that maintains a list of observers
 * and notifies them of any changes in its state.
 * <p>
 * This class is used to implement the Observer design pattern, where
 * multiple observer objects can subscribe to an observable object and
 * be notified of state changes.
 * </p>
 */
public class Observable {

    // List to store registered observers
    private final List<Observer> observers = new ArrayList<>();
    private boolean changed = false; // Indicates whether the observable's state has changed

    /**
     * Adds an observer to the list of observers.
     * <p>
     * Observers are notified of changes in the state of this observable object.
     * </p>
     *
     * @param observer The observer to be added to the list.
     */
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    /**
     * Removes an observer from the list of observers.
     * <p>
     * The observer will no longer be notified of state changes.
     * </p>
     *
     * @param observer The observer to be removed from the list.
     */
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    /**
     * Marks this observable object as having been changed.
     * <p>
     * This must be called before calling {@link #notifyObservers(Object)}
     * to ensure observers are notified.
     * </p>
     */
    public void setChanged() {
        this.changed = true;
    }

    /**
     * Notifies all registered observers of a change in state, if the state has changed.
     * <p>
     * The `arg` parameter can be used to pass specific information about the change.
     * After notification, the `changed` flag is reset to {@code false}.
     * </p>
     *
     * @param arg An argument to pass to the observers, providing details about the change.
     */
    public void notifyObservers(Object arg) {
        if (changed) { // Notify only if the state has changed
            for (Observer observer : observers) {
                observer.update(arg); // Notify each observer
            }
            changed = false; // Reset the changed flag after notifying observers
        }
    }

    /**
     * Removes a specific observer from the list of observers.
     * <p>
     * Useful when an observer no longer wants to listen to state changes.
     * </p>
     *
     * @param observer The observer to be removed.
     */
    public void deleteObserver(Observer observer) {
        observers.remove(observer);
        System.out.println("Observer removed: " + observer);
    }
}
