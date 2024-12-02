package com.example.demo.ui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Represents the "You Win" image displayed when the player wins the game.
 * This class handles loading the image, setting its size and position, and toggling its visibility.
 */
public class WinImage extends ImageView {

	// Path to the "You Win" image resource
	private static final String IMAGE_NAME = "/com/example/demo/images/youwin.png";

	// Dimensions for resizing the image
	private static final int HEIGHT = 500; // Height of the "You Win" image
	private static final int WIDTH = 600;  // Width of the "You Win" image

	/**
	 * Constructs a WinImage and sets its position, size, and visibility.
	 *
	 * @param xPosition the X coordinate of the image's layout position
	 * @param yPosition the Y coordinate of the image's layout position
	 */
	public WinImage(double xPosition, double yPosition) {
		// Load the "You Win" image from the resource path
		this.setImage(new Image(getClass().getResource(IMAGE_NAME).toExternalForm()));

		// Set initial properties: hidden by default, with specified size and position
		this.setVisible(false); // Initially hidden
		this.setFitHeight(HEIGHT); // Set the height
		this.setFitWidth(WIDTH);   // Set the width
		this.setLayoutX(xPosition); // Set the horizontal position
		this.setLayoutY(yPosition); // Set the vertical position
	}

	/**
	 * Makes the "You Win" image visible.
	 */
	public void showWinImage() {
		this.setVisible(true); // Display the "You Win" image
	}
}
