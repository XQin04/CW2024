package com.example.demo.projectiles;

import com.example.demo.actors.ActiveActorDestructible;

/**
 * Represents a generic projectile in the game.
 * <p>
 * This abstract class provides a base for all types of projectiles, such as those fired by enemies or the player.
 * Specific projectile types should extend this class to define their unique movement and behavior.
 * </p>
 */
public abstract class Projectile extends ActiveActorDestructible {

	/**
	 * Constructs a generic projectile with the specified properties.
	 *
	 * @param imageName   The name of the image file representing the projectile.
	 * @param imageHeight The height of the projectile image, scaled to maintain aspect ratio.
	 * @param initialXPos The initial X-coordinate of the projectile on the screen.
	 * @param initialYPos The initial Y-coordinate of the projectile on the screen.
	 */
	public Projectile(String imageName, int imageHeight, double initialXPos, double initialYPos) {
		super(imageName, imageHeight, initialXPos, initialYPos);
	}

	/**
	 * Handles damage taken by the projectile.
	 * <p>
	 * The default behavior for all projectiles is to destroy themselves when they "take damage."
	 * Subclasses can override this behavior to implement more complex responses to damage.
	 * </p>
	 */
	@Override
	public void takeDamage() {
		destroy(); // Destroy the projectile when it takes damage
	}

	/**
	 * Updates the position of the projectile each frame.
	 * <p>
	 * This method must be implemented by subclasses to define their specific movement logic,
	 * such as horizontal movement for enemy projectiles or splitting behavior for boss projectiles.
	 * </p>
	 */
	@Override
	public abstract void updatePosition();
}
