package com.example.demo.ui;

import com.example.demo.utils.UIPositionHelper;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * Represents a display for heart icons, used to indicate player lives or health in a game.
 * Hearts are displayed in a horizontal box (HBox) and can be removed dynamically.
 */
public class HeartDisplay {

	private static final String HEART_IMAGE_NAME = "/com/example/demo/images/heart.png";
	private static final int HEART_HEIGHT = 50;
	private static final int INDEX_OF_FIRST_ITEM = 0;

	private final HBox container;
	private final int initialHeartCount;

	/**
	 * Constructs a HeartDisplay with the specified position and number of hearts.
	 *
	 * @param xPosition       X-coordinate for the heart display.
	 * @param yPosition       Y-coordinate for the heart display.
	 * @param heartsToDisplay Initial number of hearts to display.
	 */
	public HeartDisplay(double xPosition, double yPosition, int heartsToDisplay) {
		this.initialHeartCount = heartsToDisplay;
		this.container = new HBox();

		// Set the position of the heart container
		UIPositionHelper.setPosition(container, xPosition, yPosition);

		// Initialize hearts
		initializeHearts(heartsToDisplay);
	}

	/**
	 * Initializes the container with the specified number of heart icons.
	 *
	 * @param count Number of hearts to display initially.
	 */
	private void initializeHearts(int count) {
		for (int i = 0; i < count; i++) {
			container.getChildren().add(createHeartIcon());
		}
	}

	/**
	 * Creates a heart icon as an ImageView.
	 *
	 * @return A configured ImageView representing a heart.
	 */
	private ImageView createHeartIcon() {
		ImageView heart = new ImageView(new Image(getClass().getResource(HEART_IMAGE_NAME).toExternalForm()));
		heart.setFitHeight(HEART_HEIGHT);
		heart.setPreserveRatio(true);
		return heart;
	}

	/**
	 * Removes one heart from the display.
	 * If no hearts are left, the method does nothing.
	 */
	public void removeHeart() {
		if (!container.getChildren().isEmpty()) {
			container.getChildren().remove(INDEX_OF_FIRST_ITEM);
		}
	}

	/**
	 * Resets the heart display to its initial state.
	 */
	public void resetHearts() {
		container.getChildren().clear();
		initializeHearts(initialHeartCount);
	}

	/**
	 * Returns the HBox container holding the hearts.
	 *
	 * @return The container with heart icons.
	 */
	public HBox getContainer() {
		return container;
	}
}
