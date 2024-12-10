package com.example.demo.actors.enemies;

import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.projectiles.EnemyProjectile;

/**
 * Represents an enemy spider in the game.
 *
 * <p>The EnemySpider moves horizontally across the screen and can fire projectiles
 * with a defined probability. This class extends {@link FighterSpider} to reuse
 * functionality for health management and interactions.</p>
 */
public class EnemySpider extends FighterSpider {

	// Constants for enemy spider properties
	private static final String IMAGE_NAME = "enemy.png";
	private static final int IMAGE_HEIGHT = 65;
	private static final int HORIZONTAL_VELOCITY = -6;
	private static final double PROJECTILE_X_POSITION_OFFSET = -100.0;
	private static final double PROJECTILE_Y_POSITION_OFFSET = 50.0;
	private static final int INITIAL_HEALTH = 1;
	private static final double FIRE_RATE = 0.01; // Probability of firing a projectile each frame

	/**
	 * Constructs an EnemySpider with the specified initial position.
	 *
	 * @param initialXPos The initial X position of the enemy spider.
	 * @param initialYPos The initial Y position of the enemy spider.
	 */
	public EnemySpider(double initialXPos, double initialYPos) {
		super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos, INITIAL_HEALTH);
	}

	/**
	 * Updates the position of the enemy spider by moving it horizontally.
	 */
	@Override
	public void updatePosition() {
		moveHorizontally(HORIZONTAL_VELOCITY);
	}

	/**
	 * Fires a projectile from the enemy spider, with a small probability defined by FIRE_RATE.
	 *
	 * @return An {@link EnemyProjectile} if fired, otherwise null.
	 */
	@Override
	public ActiveActorDestructible fireProjectile() {
		if (Math.random() < FIRE_RATE) {
			double projectileXPosition = getProjectileXPosition(PROJECTILE_X_POSITION_OFFSET);
			double projectileYPosition = getProjectileYPosition(PROJECTILE_Y_POSITION_OFFSET);
			return new EnemyProjectile(projectileXPosition, projectileYPosition);
		}
		return null;
	}

	/**
	 * Updates the state of the enemy spider, including its position and interactions.
	 */
	@Override
	public void updateActor() {
		updatePosition();
	}
}
