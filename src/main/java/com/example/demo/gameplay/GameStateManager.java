package com.example.demo.gameplay;

/**
 * Manages the current state of the game.
 * Provides methods to transition between states and check the current state.
 */
public class GameStateManager {

    /**
     * Enum representing the possible game states.
     */
    public enum GameState {
        INITIALIZING, // When the game or level is being set up
        PLAYING,      // When the game is running
        PAUSED,       // When the game is paused
        GAME_OVER,    // When the player has lost
        WIN,          // When the player has won
        LOADING       // When transitioning between levels
    }

    private GameState currentState;

    /**
     * Constructor to initialize the game state manager with a default state.
     */
    public GameStateManager() {
        this.currentState = GameState.INITIALIZING; // Default state is INITIALIZING
    }

    /**
     * Sets the current state of the game.
     *
     * @param newState The new state to set.
     */
    public void setCurrentState(GameState newState) {
        this.currentState = newState;
    }

    /**
     * Retrieves the current state of the game.
     *
     * @return The current game state.
     */
    public GameState getCurrentState() {
        return currentState;
    }

    /**
     * Checks if the current state is PLAYING.
     *
     * @return True if the current state is PLAYING, otherwise false.
     */
    public boolean isPlaying() {
        return currentState == GameState.PLAYING;
    }

    /**
     * Checks if the current state is PAUSED.
     *
     * @return True if the current state is PAUSED, otherwise false.
     */
    public boolean isPaused() {
        return currentState == GameState.PAUSED;
    }

    /**
     * Checks if the current state is GAME_OVER.
     *
     * @return True if the current state is GAME_OVER, otherwise false.
     */
    public boolean isGameOver() {
        return currentState == GameState.GAME_OVER;
    }

    /**
     * Checks if the current state is WIN.
     *
     * @return True if the current state is WIN, otherwise false.
     */
    public boolean isWin() {
        return currentState == GameState.WIN;
    }

    /**
     * Checks if the current state is LOADING.
     *
     * @return True if the current state is LOADING, otherwise false.
     */
    public boolean isLoading() {
        return currentState == GameState.LOADING;
    }

    /**
     * Checks if the current state is INITIALIZING.
     *
     * @return True if the current state is INITIALIZING, otherwise false.
     */
    public boolean isInitializing() {
        return currentState == GameState.INITIALIZING;
    }
}
