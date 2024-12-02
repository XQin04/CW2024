package com.example.demo.projectiles;

/**
 * Represents a projectile fired by the user superman.
 * Moves horizontally to the right at a constant velocity.
 */
public class UserProjectile extends Projectile {

	private static final String IMAGE_NAME = "userfire.png"; // Image for the projectile
	private static final int IMAGE_HEIGHT = 125;            // Height of the projectile image
	private static final int HORIZONTAL_VELOCITY = 15;      // Speed of the projectile

	/**
	 * Constructor for creating a user projectile.
	 *
	 * @param initialXPos The initial X-coordinate of the projectile.
	 * @param initialYPos The initial Y-coordinate of the projectile.
	 */
	public UserProjectile(double initialXPos, double initialYPos) {
		super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos);
	}

	/**
	 * Updates the position of the projectile.
	 * Moves the projectile horizontally to the right.
	 */
	@Override
	public void updatePosition() {
		moveHorizontally(HORIZONTAL_VELOCITY);
	}

	/**
	 * Updates the behavior of the projectile each frame.
	 */
	@Override
	public void updateActor() {
		updatePosition();
	}
}
