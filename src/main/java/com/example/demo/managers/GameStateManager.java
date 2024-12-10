package com.example.demo.managers;

import com.example.demo.observer.Observable;

/**
 * Manages the current state of the game.
 * <p>
 * This class implements the Singleton pattern to ensure a single instance of the game state manager.
 * It also uses the Observer pattern to notify registered observers about state changes.
 * </p>
 */
public class GameStateManager extends Observable {

    /**
     * Enum representing the possible game states.
     * <ul>
     * <li><b>INITIALIZING:</b> The game or level is being set up.</li>
     * <li><b>PLAYING:</b> The game is actively running.</li>
     * <li><b>PAUSED:</b> The game is temporarily halted.</li>
     * <li><b>GAME_OVER:</b> The player has lost the game.</li>
     * <li><b>WIN:</b> The player has won the game.</li>
     * <li><b>LOADING:</b> The game is transitioning between levels or scenes.</li>
     * </ul>
     */
    public enum GameState {
        INITIALIZING,
        PLAYING,
        PAUSED,
        GAME_OVER,
        WIN,
        LOADING
    }

    // Singleton instance of GameStateManager
    private static GameStateManager instance;

    // Current state of the game
    private GameState currentState;

    /**
     * Private constructor to enforce Singleton pattern.
     * The initial state of the game is set to {@code INITIALIZING}.
     */
    private GameStateManager() {
        this.currentState = GameState.INITIALIZING;
    }

    /**
     * Provides access to the single instance of GameStateManager.
     * <p>
     * If the instance does not already exist, it is created.
     * </p>
     *
     * @return The Singleton instance of GameStateManager.
     */
    public static GameStateManager getInstance() {
        if (instance == null) {
            instance = new GameStateManager();
        }
        return instance;
    }

    /**
     * Sets the current state of the game.
     * <p>
     * This method also notifies all registered observers about the state change.
     * </p>
     *
     * @param newState The new state to set.
     */
    public void setCurrentState(GameState newState) {
        this.currentState = newState; // Update the current state
        notifyObservers(newState); // Notify all observers
    }

    /**
     * Retrieves the current state of the game.
     *
     * @return The current game state as a {@link GameState} enum.
     */
    public GameState getCurrentState() {
        return currentState;
    }

    /**
     * Checks if the current game state is {@code PLAYING}.
     *
     * @return {@code true} if the current state is {@code PLAYING}, otherwise {@code false}.
     */
    public boolean isPlaying() {
        return currentState == GameState.PLAYING;
    }

    /**
     * Checks if the current game state is {@code PAUSED}.
     *
     * @return {@code true} if the current state is {@code PAUSED}, otherwise {@code false}.
     */
    public boolean isPaused() {
        return currentState == GameState.PAUSED;
    }

    /**
     * Checks if the current game state is {@code GAME_OVER}.
     *
     * @return {@code true} if the current state is {@code GAME_OVER}, otherwise {@code false}.
     */
    public boolean isGameOver() {
        return currentState == GameState.GAME_OVER;
    }

    /**
     * Checks if the current game state is {@code WIN}.
     *
     * @return {@code true} if the current state is {@code WIN}, otherwise {@code false}.
     */
    public boolean isWin() {
        return currentState == GameState.WIN;
    }

    /**
     * Checks if the current game state is {@code LOADING}.
     *
     * @return {@code true} if the current state is {@code LOADING}, otherwise {@code false}.
     */
    public boolean isLoading() {
        return currentState == GameState.LOADING;
    }

    /**
     * Checks if the current game state is {@code INITIALIZING}.
     *
     * @return {@code true} if the current state is {@code INITIALIZING}, otherwise {@code false}.
     */
    public boolean isInitializing() {
        return currentState == GameState.INITIALIZING;
    }
}
