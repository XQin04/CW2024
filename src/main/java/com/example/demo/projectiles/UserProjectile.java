package com.example.demo.projectiles;

/**
 * Represents a projectile fired by the user superman.
 * <p>
 * This projectile moves horizontally to the right at a constant velocity
 * and is used as the player's primary attack mechanism.
 * </p>
 */
public class UserProjectile extends Projectile {

	// Constants defining the projectile's properties
	private static final String IMAGE_NAME = "userfire.png"; // The image file representing the projectile
	private static final int IMAGE_HEIGHT = 125;            // Height of the projectile image (aspect ratio preserved)
	private static final int HORIZONTAL_VELOCITY = 18;      // Speed at which the projectile moves horizontally

	/**
	 * Constructs a projectile fired by the user superman.
	 *
	 * @param initialXPos The initial X-coordinate of the projectile on the screen.
	 * @param initialYPos The initial Y-coordinate of the projectile on the screen.
	 */
	public UserProjectile(double initialXPos, double initialYPos) {
		super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos);
	}

	/**
	 * Updates the position of the projectile each frame.
	 * <p>
	 * The projectile moves horizontally to the right at a constant velocity.
	 * </p>
	 */
	@Override
	public void updatePosition() {
		moveHorizontally(HORIZONTAL_VELOCITY); // Moves the projectile to the right
	}

	/**
	 * Updates the behavior of the projectile on each frame.
	 * <p>
	 * This method is called as part of the game loop and triggers the movement logic
	 * defined in {@link #updatePosition()}.
	 * </p>
	 */
	@Override
	public void updateActor() {
		updatePosition(); // Update the position of the projectile
	}
}
