package com.example.demo.managers;

import com.example.demo.JavaFXInitializer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameStateManagerTest {

    @BeforeAll
    static void initializeJavaFX() {
        JavaFXInitializer.initialize();
    }


    @BeforeEach
    void setUp() {
        // Reset the GameStateManager's state before each test
        GameStateManager.getInstance().setCurrentState(GameStateManager.GameState.INITIALIZING);
    }


    @Test
    void getInstance() {
        // Get two instances of GameStateManager
        GameStateManager instance1 = GameStateManager.getInstance();
        GameStateManager instance2 = GameStateManager.getInstance();

        // Verify that both references point to the same instance
        assertSame(instance1, instance2, "GameStateManager should return the same instance");
    }

    @Test
    void setCurrentState() {
        // Arrange
        GameStateManager manager = GameStateManager.getInstance();

        // Act: Set the current state to PLAYING
        manager.setCurrentState(GameStateManager.GameState.PLAYING);

        // Assert: Verify the state is updated correctly
        assertEquals(GameStateManager.GameState.PLAYING, manager.getCurrentState(), "The game state should be PLAYING");
    }


    @Test
    void isNotPlaying() {
        // Arrange
        GameStateManager manager = GameStateManager.getInstance();

        // Act & Assert: Verify `isNotPlaying` returns true for non-PLAYING states
        manager.setCurrentState(GameStateManager.GameState.PAUSED);
        assertTrue(manager.isNotPlaying(), "The game should not be playing in PAUSED state");

        manager.setCurrentState(GameStateManager.GameState.GAME_OVER);
        assertTrue(manager.isNotPlaying(), "The game should not be playing in GAME_OVER state");

        // Act & Assert: Verify `isNotPlaying` returns false for PLAYING state
        manager.setCurrentState(GameStateManager.GameState.PLAYING);
        assertFalse(manager.isNotPlaying(), "The game should be playing in PLAYING state");
    }

}