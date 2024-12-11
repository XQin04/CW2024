package com.example.demo.managers;

import com.example.demo.observer.Observable;

/**
 * Manages the current state of the game using Singleton and Observer patterns.
 */
public class GameStateManager extends Observable {

    private static GameStateManager instance;
    private GameState currentState;
    private GameStateManager() {
        this.currentState = GameState.INITIALIZING;
    }

    public static GameStateManager getInstance() {
        if (instance == null) {
            instance = new GameStateManager();
        }
        return instance;
    }

    public void setCurrentState(GameState newState) {
        this.currentState = newState;
        notifyObservers(newState);
    }

    public GameState getCurrentState() {
        return currentState;
    }

    public boolean isNotPlaying() {
        return currentState != GameState.PLAYING;
    }


    public enum GameState {
        INITIALIZING,
        PLAYING,
        PAUSED,
        GAME_OVER,
        WIN,
        LOADING
    }
}
