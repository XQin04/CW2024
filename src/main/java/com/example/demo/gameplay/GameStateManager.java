package com.example.demo.gameplay;

/**
 * Manages the current state of the game.
 * Implements the Singleton pattern to ensure a single instance.
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

    // Singleton instance
    private static GameStateManager instance;

    private GameState currentState;

    /**
     * Private constructor to prevent direct instantiation (Singleton pattern).
     */
    private GameStateManager() {
        this.currentState = GameState.INITIALIZING; // Default state is INITIALIZING
    }

    /**
     * Provides access to the single instance of GameStateManager.
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
