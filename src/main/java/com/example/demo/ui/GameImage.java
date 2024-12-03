package com.example.demo.ui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Base class for game images such as "Win" or "Game Over".
 * Handles loading, sizing, positioning, and visibility.
 */
public abstract class GameImage extends ImageView {

    /**
     * Constructs a GameImage with the specified properties.
     *
     * @param imagePath  The file path of the image.
     * @param xPosition  The X coordinate for positioning the image.
     * @param yPosition  The Y coordinate for positioning the image.
     * @param width      The width of the image.
     * @param height     The height of the image.
     */
    public GameImage(String imagePath, double xPosition, double yPosition, double width, double height) {
        // Load the image
        this.setImage(new Image(getClass().getResource(imagePath).toExternalForm()));

        // Set the image's size and position
        this.setFitWidth(width);
        this.setFitHeight(height);
        this.setLayoutX(xPosition);
        this.setLayoutY(yPosition);

        // Initially hidden
        this.setVisible(false);
    }

    /**
     * Makes the image visible.
     */
    public void show() {
        this.setVisible(true);
    }

    /**
     * Hides the image.
     */
    public void hide() {
        this.setVisible(false);
    }
}
