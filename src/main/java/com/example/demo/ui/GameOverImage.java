package com.example.demo.ui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Represents the "Game Over" image displayed when the game ends.
 * This class handles loading the image, resizing it, and positioning it on the screen.
 */
public class GameOverImage extends ImageView {

	// Path to the "Game Over" image resource
	private static final String IMAGE_NAME = "/com/example/demo/images/gameover.png";

	// Dimensions for resizing the image
	private static final double IMAGE_WIDTH = 650; // The width of the image
	private static final double IMAGE_HEIGHT = 600; // The height of the image

	/**
	 * Constructs a GameOverImage and sets its position and size.
	 *
	 * @param xPosition the X coordinate for the image's layout position
	 * @param yPosition the Y coordinate for the image's layout position
	 */
	public GameOverImage(double xPosition, double yPosition) {
		// Load the "Game Over" image
		setImage(new Image(getClass().getResource(IMAGE_NAME).toExternalForm()));

		// Set the image's dimensions
		setFitWidth(IMAGE_WIDTH);
		setFitHeight(IMAGE_HEIGHT);

		// Set the image's position on the screen
		setLayoutX(280); // Explicit horizontal position
		setLayoutY(40);  // Explicit vertical position
	}
}
