package com.example.demo.actors;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Represents an active actor in the game, which is an image-based entity
 * that can move horizontally or vertically and updates its position.
 */
public abstract class ActiveActor extends ImageView {

	// Base directory for image resources
	private static final String IMAGE_LOCATION = "/com/example/demo/images/";

	/**
	 * Constructs an ActiveActor with the specified image, size, and initial position.
	 *
	 * @param imageName   Name of the image file for the actor.
	 * @param imageHeight Height of the image to be displayed.
	 * @param initialXPos Initial X position of the actor.
	 * @param initialYPos Initial Y position of the actor.
	 */
	public ActiveActor(String imageName, int imageHeight, double initialXPos, double initialYPos) {
		// Load the image from the resources folder
		this.setImage(new Image(getClass().getResource(IMAGE_LOCATION + imageName).toExternalForm()));

		// Set the initial position and size
		this.setLayoutX(initialXPos);
		this.setLayoutY(initialYPos);
		this.setFitHeight(imageHeight);
		this.setPreserveRatio(true);
	}

	/**
	 * Updates the position of the actor. Subclasses must implement this method
	 * to define their specific movement logic.
	 */
	public abstract void updatePosition();

	/**
	 * Moves the actor horizontally by the specified distance.
	 *
	 * @param horizontalMove The distance to move horizontally (positive for right, negative for left).
	 */
	protected void moveHorizontally(double horizontalMove) {
		this.setTranslateX(getTranslateX() + horizontalMove);
	}

	/**
	 * Moves the actor vertically by the specified distance.
	 *
	 * @param verticalMove The distance to move vertically (positive for down, negative for up).
	 */
	protected void moveVertically(double verticalMove) {
		this.setTranslateY(getTranslateY() + verticalMove);
	}
}
