package com.example.demo.projectiles;

/**
 * Represents a projectile fired by enemy spiders in the game.
 * <p>
 * The projectile moves horizontally from its initial position towards the user at a constant velocity.
 * If it goes out of bounds or collides with an object, it will be destroyed.
 * </p>
 */
public class EnemyProjectile extends Projectile {

	private static final String IMAGE_NAME = "enemyweb.png"; // Path to the image representing the projectile
	private static final int IMAGE_HEIGHT = 50;             // Height of the projectile's image
	private static final int HORIZONTAL_VELOCITY = -10;     // Speed at which the projectile moves horizontally

	/**
	 * Constructs an enemy projectile with the specified initial position.
	 *
	 * @param initialXPos The initial X-coordinate of the projectile.
	 * @param initialYPos The initial Y-coordinate of the projectile.
	 */
	public EnemyProjectile(double initialXPos, double initialYPos) {
		super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos);
	}

	/**
	 * Updates the position of the projectile.
	 * <p>
	 * The projectile moves leftward at a constant velocity. If additional behavior is required,
	 * such as checking for collisions or going out of bounds, it can be added here.
	 * </p>
	 */
	@Override
	public void updatePosition() {
		moveHorizontally(HORIZONTAL_VELOCITY); // Move leftward by a fixed velocity
	}

	/**
	 * Updates the behavior of the projectile on each frame.
	 * <p>
	 * This method ensures the projectile updates its position and can include additional
	 * functionality like handling destruction or interactions.
	 * </p>
	 */
	@Override
	public void updateActor() {
		updatePosition();
	}
}
