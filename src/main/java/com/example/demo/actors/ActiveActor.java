package com.example.demo.actors;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Represents an active actor in the game, which is an image-based entity
 * capable of movement and position updates.
 *
 * <p>The {@code ActiveActor} class is designed to be extended by specific game actors
 * such as enemies, projectiles, or the player character. It uses Java Fx {@link ImageView}
 * for rendering and supports basic movement functionality.</p>
 */
public abstract class ActiveActor extends ImageView {

	// Base directory for image resources
	private static final String IMAGE_LOCATION = "/com/example/demo/images/";

	/**
	 * Constructs an {@code ActiveActor} with the specified image, size, and initial position.
	 * This constructor loads the actor's image from the resources folder and initializes its position and size.
	 *
	 * @param imageName   The name of the image file for the actor (e.g., "player.png").
	 * @param imageHeight The height of the image to be displayed, in pixels.
	 * @param initialXPos The initial X-coordinate position of the actor on the screen.
	 * @param initialYPos The initial Y-coordinate position of the actor on the screen.
	 *
	 * @throws IllegalArgumentException If the image resource cannot be found in the resources folder.
	 *
	 * <p><b>Example Usage:</b></p>
	 * <pre>{@code
	 * ActiveActor player = new PlayerActor("player.png", 50, 100, 200);
	 * }</pre>
	 */
	public ActiveActor(String imageName, int imageHeight, double initialXPos, double initialYPos) {
		// Load the image from the resources folder
		String resourcePath = IMAGE_LOCATION + imageName;
		var resourceUrl = getClass().getResource(resourcePath);

		if (resourceUrl == null) {
			throw new IllegalArgumentException("Resource not found: " + resourcePath);
		}

		this.setImage(new Image(resourceUrl.toExternalForm()));

		// Set the initial position and size
		this.setLayoutX(initialXPos);
		this.setLayoutY(initialYPos);
		this.setFitHeight(imageHeight);
		this.setPreserveRatio(true);
	}


	/**
	 * Updates the position of the actor.
	 *
	 */
	public abstract void updatePosition();

	/**
	 * Moves the actor horizontally by a specified distance.
	 *
	 * @param horizontalMove The distance to move horizontally, in pixels. Positive values
	 *                       move the actor to the right, and negative values move it to the left.
	 */
	protected void moveHorizontally(double horizontalMove) {
		this.setTranslateX(getTranslateX() + horizontalMove);
	}

	/**
	 * Moves the actor vertically by a specified distance.
	 *
	 * @param verticalMove The distance to move vertically, in pixels. Positive values
	 *                     move the actor downward, and negative values move it upward.
	 */
	protected void moveVertically(double verticalMove) {
		this.setTranslateY(getTranslateY() + verticalMove);
	}
}
