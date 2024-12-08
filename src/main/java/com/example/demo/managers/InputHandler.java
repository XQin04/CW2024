package com.example.demo.managers;

import com.example.demo.actors.UserSuperman;
import com.example.demo.gameplay.GameStateManager;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class InputHandler {

    private final UserSuperman player;
    private final GameStateManager gameStateManager;

    public InputHandler(UserSuperman player, GameStateManager gameStateManager) {
        this.player = player;
        this.gameStateManager = gameStateManager;
    }

    public void handleKeyPress(KeyEvent event) {
        if (!gameStateManager.isPlaying()) {
            return; // Ignore input if the game is not in the PLAYING state
        }

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

    public void handleKeyRelease(KeyEvent event) {
        if (!gameStateManager.isPlaying()) {
            return; // Ignore input if the game is not in the PLAYING state
        }

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
