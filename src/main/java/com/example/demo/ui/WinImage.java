package com.example.demo.ui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Represents the "You Win" image displayed when the player wins the game.
 * <p>
 * This class is responsible for:
 * <ul>
 *     <li>Loading the "You Win" image from the resource folder.</li>
 *     <li>Setting the image's size and position on the game screen.</li>
 *     <li>Managing the visibility of the image.</li>
 * </ul>
 * </p>
 */
public class WinImage extends ImageView {

	// Path to the "You Win" image resource
	private static final String IMAGE_NAME = "/com/example/demo/images/youwin.png";

	// Dimensions for resizing the image
	private static final int HEIGHT = 500; // Height of the "You Win" image
	private static final int WIDTH = 600;  // Width of the "You Win" image

	/**
	 * Constructs a WinImage instance and initializes its position, size, and visibility.
	 * <p>
	 * The image is loaded from the specified resource path and positioned on the screen
	 * based on the provided X and Y coordinates. The image is initially hidden and
	 * can be displayed when required (e.g., when the player wins the game).
	 * </p>
	 *
	 * @param xPosition The X coordinate of the image's layout position.
	 * @param yPosition The Y coordinate of the image's layout position.
	 */
	public WinImage(double xPosition, double yPosition) {
		// Load the "You Win" image from the resource path
		this.setImage(new Image(getClass().getResource(IMAGE_NAME).toExternalForm()));

		// Set initial properties
		this.setVisible(false); // Initially hidden
		this.setFitHeight(HEIGHT); // Set the height of the image
		this.setFitWidth(WIDTH);   // Set the width of the image
		this.setLayoutX(xPosition); // Set the horizontal position
		this.setLayoutY(yPosition); // Set the vertical position
	}

	/**
	 * Displays the "You Win" image on the screen.
	 * <p>
	 * This method makes the image visible, typically called when the player achieves victory in the game.
	 * </p>
	 */
	public void showWinImage() {
		this.setVisible(true); // Display the "You Win" image
	}
}
