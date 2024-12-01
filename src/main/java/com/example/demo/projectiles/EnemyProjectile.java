package com.example.demo.projectiles;

/**
 * Represents a projectile fired by enemy planes.
 * Moves horizontally at a constant velocity towards the user.
 */
public class EnemyProjectile extends Projectile {

	private static final String IMAGE_NAME = "shoot.png"; // Image for the projectile
	private static final int IMAGE_HEIGHT = 50;          // Height of the projectile image
	private static final int HORIZONTAL_VELOCITY = -10;  // Speed at which the projectile moves

	/**
	 * Constructor for creating an enemy projectile.
	 *
	 * @param initialXPos The initial X-coordinate of the projectile.
	 * @param initialYPos The initial Y-coordinate of the projectile.
	 */
	public EnemyProjectile(double initialXPos, double initialYPos) {
		super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos);
	}

	/**
	 * Updates the position of the projectile.
	 * Moves the projectile horizontally at a constant velocity.
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
