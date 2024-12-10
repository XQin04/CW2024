package com.example.demo.managers;

import com.example.demo.actors.UserSuperman;
import com.example.demo.gameplay.GameStateManager;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Handles user input for controlling the player's character and game state.
 * <p>
 * The {@code InputHandler} class listens to key press and release events and translates them
 * into actions for the {@link UserSuperman} player character or game-related commands.
 * </p>
 */
public class InputHandler {

    private final UserSuperman player; // The player's character
    private final GameStateManager gameStateManager; // Manages the current state of the game

    /**
     * Constructs an {@code InputHandler} with the specified player character and game state manager.
     *
     * @param player           The {@link UserSuperman} player character to control.
     * @param gameStateManager The {@link GameStateManager} to check the current game state.
     */
    public InputHandler(UserSuperman player, GameStateManager gameStateManager) {
        this.player = player;
        this.gameStateManager = gameStateManager;
    }

    /**
     * Handles key press events and translates them into player actions or game state commands.
     * <p>
     * Supported controls:
     * <ul>
     *     <li>{@code UP}: Move the player upward.</li>
     *     <li>{@code DOWN}: Move the player downward.</li>
     *     <li>{@code LEFT}: Move the player to the left.</li>
     *     <li>{@code RIGHT}: Move the player to the right.</li>
     *     <li>{@code SPACE}: Fire a projectile.</li>
     * </ul>
     * </p>
     *
     * @param event The {@link KeyEvent} triggered by a key press.
     */
    public void handleKeyPress(KeyEvent event) {
        // Ignore input if the game is not in the PLAYING state
        if (!gameStateManager.isPlaying()) {
            return;
        }

        KeyCode keyCode = event.getCode();

        // Map key presses to player actions
        switch (keyCode) {
            case UP -> player.moveUp(); // Move up
            case DOWN -> player.moveDown(); // Move down
            case LEFT -> player.moveLeft(); // Move left
            case RIGHT -> player.moveRight(); // Move right
            case SPACE -> player.fireProjectile(); // Fire a projectile
            default -> {
                // Optional: Handle other keys if needed
            }
        }
    }

    /**
     * Handles key release events and stops the player's movement in the released direction.
     * <p>
     * Supported controls:
     * <ul>
     *     <li>{@code UP}/{@code DOWN}: Stops vertical movement.</li>
     *     <li>{@code LEFT}/{@code RIGHT}: Stops horizontal movement.</li>
     * </ul>
     * </p>
     *
     * @param event The {@link KeyEvent} triggered by a key release.
     */
    public void handleKeyRelease(KeyEvent event) {
        // Ignore input if the game is not in the PLAYING state
        if (!gameStateManager.isPlaying()) {
            return;
        }

        KeyCode keyCode = event.getCode();

        // Map key releases to stopping movement
        switch (keyCode) {
            case UP, DOWN -> player.stopVertical(); // Stop vertical movement
            case LEFT, RIGHT -> player.stopHorizontal(); // Stop horizontal movement
            default -> {
                // Optional: Handle other keys if needed
            }
        }
    }
}
