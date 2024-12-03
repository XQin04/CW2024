package com.example.demo.ui;

/**
 * Represents the "Game Over" image displayed when the game ends.
 */
public class GameOverImage extends GameImage {

	private static final String IMAGE_PATH = "/com/example/demo/images/gameover.png";
	private static final double WIDTH = 650;
	private static final double HEIGHT = 600;

	/**
	 * Constructs a GameOverImage and positions it at the center of the screen.
	 *
	 * @param screenWidth  The width of the screen.
	 * @param screenHeight The height of the screen.
	 */
	public GameOverImage(double screenWidth, double screenHeight) {
		// Calculate the center position and pass to the parent class
		super(IMAGE_PATH,
				(screenWidth - WIDTH) / 2,  // Center horizontally
				(screenHeight - HEIGHT) / 2, // Center vertically
				WIDTH,
				HEIGHT);
	}
}
