package com.example.demo.ui;

import javafx.scene.Group;

/**
 * Represents the visual elements of a game level, including hearts for player health,
 * a "Win" image, and a "Game Over" image.
 * Manages the display and removal of these elements.
 */
public class LevelView {

	// Position constants for the heart display
	private static final double HEART_DISPLAY_X_POSITION = 5;   // X position for heart display
	private static final double HEART_DISPLAY_Y_POSITION = 25;  // Y position for heart display

	// Position constants for the "Win" image
	private static final int WIN_IMAGE_X_POSITION = 355;  // X position for "Win" image
	private static final int WIN_IMAGE_Y_POSITION = 175;  // Y position for "Win" image

	// Position constants for the "Game Over" image
	private static final int LOSS_SCREEN_X_POSITION = -160;   // X position for "Game Over" image
	private static final int LOSS_SCREEN_Y_POSISITION = -375; // Y position for "Game Over" image

	private final Group root; // Root group where all visual elements are added
	private final WinImage winImage; // "Win" image to display when the level is won
	private final GameOverImage gameOverImage; // "Game Over" image to display when the level is lost
	private final HeartDisplay heartDisplay; // Heart display for player health

	/**
	 * Constructs a LevelView with the specified root group and number of hearts.
	 *
	 * @param root            the root group to which the visual elements are added
	 * @param heartsToDisplay the initial number of hearts to display
	 */
	public LevelView(Group root, int heartsToDisplay) {
		this.root = root;
		this.heartDisplay = new HeartDisplay(HEART_DISPLAY_X_POSITION, HEART_DISPLAY_Y_POSITION, heartsToDisplay);
		this.winImage = new WinImage(WIN_IMAGE_X_POSITION, WIN_IMAGE_Y_POSITION);
		this.gameOverImage = new GameOverImage(LOSS_SCREEN_X_POSITION, LOSS_SCREEN_Y_POSISITION);
	}

	/**
	 * Displays the heart display on the screen.
	 */
	public void showHeartDisplay() {
		root.getChildren().add(heartDisplay.getContainer());
	}

	/**
	 * Displays the "Win" image on the screen and makes it visible.
	 */
	public void showWinImage() {
		root.getChildren().add(winImage);
		winImage.showWinImage();
	}

	/**
	 * Displays the "Game Over" image on the screen.
	 */
	public void showGameOverImage() {
		root.getChildren().add(gameOverImage);
	}

	/**
	 * Removes the "Win" image from the screen if it is currently displayed.
	 */
	public void removeWinImage() {
		if (root.getChildren().contains(winImage)) {
			root.getChildren().remove(winImage);
		}
	}

	/**
	 * Removes the "Game Over" image from the screen if it is currently displayed.
	 */
	public void removeGameOverImage() {
		if (root.getChildren().contains(gameOverImage)) {
			root.getChildren().remove(gameOverImage);
		}
	}

	/**
	 * Updates the heart display by removing the appropriate number of hearts.
	 *
	 * @param heartsRemaining the number of hearts that should remain displayed
	 */
	public void removeHearts(int heartsRemaining) {
		int currentNumberOfHearts = heartDisplay.getContainer().getChildren().size();
		for (int i = 0; i < currentNumberOfHearts - heartsRemaining; i++) {
			heartDisplay.removeHeart();
		}
	}
}
