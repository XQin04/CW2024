package com.example.demo.ui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Represents the "Game Over" image displayed when the player loses the game.
 * <p>
 * This class handles loading the image, setting its size and position, and ensuring it integrates seamlessly into the game's UI.
 * </p>
 */
public class GameOverImage extends ImageView {

	// Path to the "Game Over" image resource
	private static final String IMAGE_NAME = "/com/example/demo/images/gameover.png";

	// Dimensions for resizing the image
	private static final double IMAGE_WIDTH = 650; // Width of the "Game Over" image
	private static final double IMAGE_HEIGHT = 600; // Height of the "Game Over" image

	/**
	 * Constructs a `GameOverImage` and configures its size and position on the screen.
	 *
	 * @param xPosition The X coordinate for positioning the image on the screen.
	 * @param yPosition The Y coordinate for positioning the image on the screen.
	 */
	public GameOverImage(double xPosition, double yPosition) {
		// Load the "Game Over" image resource
		setImage(new Image(getClass().getResource(IMAGE_NAME).toExternalForm()));

		// Set the dimensions of the image
		setFitWidth(IMAGE_WIDTH);
		setFitHeight(IMAGE_HEIGHT);

		// Set the image's position on the screen
		setLayoutX(280); // Explicit horizontal position
		setLayoutY(40);  // Explicit vertical position
	}
}
