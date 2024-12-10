package com.example.demo.observer;

/**
 * The Observer interface should be implemented by any class
 * that wants to receive updates from an Observable object.
 */
public interface Observer {
    /**
     * This method is called whenever the observed object is changed.
     *
     * @param arg An argument passed to the notifyObservers method.
     */
    void update(Object arg);
}
