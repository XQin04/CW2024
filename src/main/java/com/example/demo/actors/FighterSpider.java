package com.example.demo.actors;

/**
 * Represents a fighter spider in the game, capable of firing projectiles and taking damage.
 * This class serves as a base for both player and enemy spider.
 */
public abstract class FighterSpider extends ActiveActorDestructible {

	private int health; // Current health of the fighter spider

	/**
	 * Constructs a FighterSpider with the specified image, size, position, and health.
	 *
	 * @param imageName   Name of the image file for the fighter spider.
	 * @param imageHeight Height of the image to be displayed.
	 * @param initialXPos Initial X position of the fighter spider.
	 * @param initialYPos Initial Y position of the fighter spider.
	 * @param health      Initial health of the fighter spider.
	 */
	public FighterSpider(String imageName, int imageHeight, double initialXPos, double initialYPos, int health) {
		super(imageName, imageHeight, initialXPos, initialYPos);
		this.health = health;
	}

	/**
	 * Fires a projectile from the fighter spider.
	 * Subclasses must implement the specific behavior for firing projectiles.
	 *
	 * @return A new projectile if fired, otherwise null.
	 */
	public abstract ActiveActorDestructible fireProjectile();

	/**
	 * Reduces the health of the fighter spider by one. If health reaches zero, the spider is destroyed.
	 */
	@Override
	public void takeDamage() {
		health--;
		if (isHealthAtZero()) {
			this.destroy();
		}
	}

	/**
	 * Calculates the X position from which a projectile will be fired.
	 *
	 * @param xPositionOffset Offset to adjust the firing position.
	 * @return The X position for the projectile.
	 */
	protected double getProjectileXPosition(double xPositionOffset) {
		return getLayoutX() + getTranslateX() + xPositionOffset;
	}

	/**
	 * Calculates the Y position from which a projectile will be fired.
	 *
	 * @param yPositionOffset Offset to adjust the firing position.
	 * @return The Y position for the projectile.
	 */
	protected double getProjectileYPosition(double yPositionOffset) {
		return getLayoutY() + getTranslateY() + yPositionOffset;
	}

	/**
	 * Checks if the fighter spider's health is zero.
	 *
	 * @return True if health is zero, otherwise false.
	 */
	private boolean isHealthAtZero() {
		return health <= 0;
	}

	/**
	 * Gets the current health of the fighter spider.
	 *
	 * @return The current health value.
	 */
	public int getHealth() {
		return health;
	}
}
