package com.example.demo.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * The Observable class to be extended by classes that want to notify observers.
 */
public class Observable {
    private final List<Observer> observers = new ArrayList<>();
    private boolean changed = false;

    /**
     * Adds an observer to the list of observers.
     *
     * @param observer The observer to be added.
     */
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    /**
     * Removes an observer from the list of observers.
     *
     * @param observer The observer to be removed.
     */
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    /**
     * Marks this Observable object as having been changed.
     */
    public void setChanged() {
        this.changed = true;
    }

    /**
     * Notifies all registered observers if the state has changed.
     *
     * @param arg An argument to pass to the observers.
     */
    public void notifyObservers(Object arg) {
        if (changed) {
            for (Observer observer : observers) {
                observer.update(arg);
            }
            changed = false; // Reset the changed flag
        }
    }
    public void deleteObserver(Observer observer) {
        observers.remove(observer);
        System.out.println("Observer removed: " + observer);
    }

}
