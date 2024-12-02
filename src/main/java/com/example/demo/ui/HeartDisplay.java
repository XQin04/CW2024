package com.example.demo.ui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * Represents a display for heart icons, used to indicate player lives or health in a game.
 * Hearts are displayed in a horizontal box (HBox) and can be removed dynamically.
 */
public class HeartDisplay {

	// Path to the heart image resource
	private static final String HEART_IMAGE_NAME = "/com/example/demo/images/heart.png";

	// Default height for each heart image
	private static final int HEART_HEIGHT = 50;

	// Index of the first item in the HBox container, used for removing hearts
	private static final int INDEX_OF_FIRST_ITEM = 0;

	private HBox container; // HBox to hold the heart icons
	private double containerXPosition; // X position of the heart display
	private double containerYPosition; // Y position of the heart display
	private int numberOfHeartsToDisplay; // Number of hearts to display initially

	/**
	 * Constructs a HeartDisplay at the specified position with the given number of hearts.
	 *
	 * @param xPosition       the X coordinate of the heart display
	 * @param yPosition       the Y coordinate of the heart display
	 * @param heartsToDisplay the initial number of hearts to display
	 */
	public HeartDisplay(double xPosition, double yPosition, int heartsToDisplay) {
		this.containerXPosition = xPosition;
		this.containerYPosition = yPosition;
		this.numberOfHeartsToDisplay = heartsToDisplay;

		initializeContainer(); // Initialize the container (HBox)
		initializeHearts();    // Populate the container with hearts
	}

	/**
	 * Initializes the HBox container for the heart display.
	 * Sets the position of the container on the screen.
	 */
	private void initializeContainer() {
		container = new HBox();
		container.setLayoutX(containerXPosition); // Set horizontal position
		container.setLayoutY(containerYPosition); // Set vertical position
	}

	/**
	 * Populates the HBox container with heart images.
	 * Each heart is loaded from the specified image resource and resized.
	 */
	private void initializeHearts() {
		for (int i = 0; i < numberOfHeartsToDisplay; i++) {
			// Create a new ImageView for each heart
			ImageView heart = new ImageView(new Image(getClass().getResource(HEART_IMAGE_NAME).toExternalForm()));

			// Set the height and maintain the aspect ratio
			heart.setFitHeight(HEART_HEIGHT);
			heart.setPreserveRatio(true);

			// Add the heart to the container
			container.getChildren().add(heart);
		}
	}

	/**
	 * Removes one heart from the display.
	 * If the container is empty, this method does nothing.
	 */
	public void removeHeart() {
		if (!container.getChildren().isEmpty()) {
			container.getChildren().remove(INDEX_OF_FIRST_ITEM); // Remove the first heart
		}
	}

	/**
	 * Returns the HBox container holding the hearts.
	 * This allows external components to add the heart display to their layout.
	 *
	 * @return the HBox container with the heart icons
	 */
	public HBox getContainer() {
		return container;
	}
}
