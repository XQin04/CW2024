package com.example.demo.ui.gameplayUI;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;

/**
 * Represents the "Game Over" image displayed when the player loses the game.
 */
public class GameOverImage extends ImageView {

    // Path to the "Game Over" image resource
    private static final String IMAGE_NAME = "/com/example/demo/images/gameover.png";

    // Dimensions for resizing the image
    private static final double IMAGE_WIDTH = 650;
    private static final double IMAGE_HEIGHT = 600;

    /**
     * Constructs a `GameOverImage` and configures its size and position on the screen.
     */
    public GameOverImage() {
        // Load the image resource
        URL resource = getClass().getResource(IMAGE_NAME);
        if (resource == null) {
            throw new IllegalArgumentException("Image resource not found: " + IMAGE_NAME);
        }
        setImage(new Image(resource.toExternalForm()));

        // Set the dimensions of the image
        setFitWidth(IMAGE_WIDTH);
        setFitHeight(IMAGE_HEIGHT);

        // Set the image's position on the screen
        setLayoutX(280);
        setLayoutY(40);
    }
}
