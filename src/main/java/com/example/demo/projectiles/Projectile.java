package com.example.demo.projectiles;

import com.example.demo.actors.ActiveActorDestructible;

/**
 * Represents a generic projectile in the game.
 * All specific projectile types (e.g., EnemyProjectile, BossProjectile) should extend this class.
 */
public abstract class Projectile extends ActiveActorDestructible {

	/**
	 * Constructor for creating a projectile.
	 *
	 * @param imageName    The name of the image file representing the projectile.
	 * @param imageHeight  The height of the projectile image.
	 * @param initialXPos  The initial X-coordinate of the projectile.
	 * @param initialYPos  The initial Y-coordinate of the projectile.
	 */
	public Projectile(String imageName, int imageHeight, double initialXPos, double initialYPos) {
		super(imageName, imageHeight, initialXPos, initialYPos);
	}

	/**
	 * Handles the destruction of the projectile when it "takes damage."
	 * Default behavior is to destroy the projectile.
	 */
	@Override
	public void takeDamage() {
		destroy();
	}

	/**
	 * Updates the position of the projectile each frame.
	 * This method must be implemented by subclasses to define their specific movement behavior.
	 */
	@Override
	public abstract void updatePosition();
}
