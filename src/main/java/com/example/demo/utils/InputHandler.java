package com.example.demo.utils;

import com.example.demo.actors.UserSuperman;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Handles user input (key presses and releases) during the game.
 */
public class InputHandler {

    private final UserSuperman player;

    public InputHandler(UserSuperman player) {
        this.player = player;
    }

    /**
     * Handles key press events.
     *
     * @param event The KeyEvent triggered by the user.
     */
    public void handleKeyPress(KeyEvent event) {
        KeyCode keyCode = event.getCode();

        switch (keyCode) {
            case UP -> player.moveUp();
            case DOWN -> player.moveDown();
            case LEFT -> player.moveLeft();
            case RIGHT -> player.moveRight();
            case SPACE -> player.fireProjectile();
            default -> {
                // Optional: Handle other keys if needed.
            }
        }
    }

    /**
     * Handles key release events.
     *
     * @param event The KeyEvent triggered by the user.
     */
    public void handleKeyRelease(KeyEvent event) {
        KeyCode keyCode = event.getCode();

        switch (keyCode) {
            case UP, DOWN -> player.stopVertical();
            case LEFT, RIGHT -> player.stopHorizontal();
            default -> {
                // Optional: Handle other keys if needed.
            }
        }
    }
}
