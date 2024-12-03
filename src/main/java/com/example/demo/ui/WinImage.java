package com.example.demo.ui;

/**
 * Represents the "You Win" image displayed when the player wins the game.
 */
public class WinImage extends GameImage {

	private static final String IMAGE_PATH = "/com/example/demo/images/youwin.png";
	private static final double WIDTH = 600;
	private static final double HEIGHT = 500;

	/**
	 * Constructs a WinImage and positions it at the center of the screen.
	 *
	 * @param screenWidth  The width of the screen.
	 * @param screenHeight The height of the screen.
	 */
	public WinImage(double screenWidth, double screenHeight) {
		// Calculate the center position and pass to the parent class
		super(IMAGE_PATH,
				(screenWidth - WIDTH) / 2,  // Center horizontally
				(screenHeight - HEIGHT) / 2, // Center vertically
				WIDTH,
				HEIGHT);
	}
}
