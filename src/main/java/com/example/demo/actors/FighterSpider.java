package com.example.demo.actors;

/**
 * Represents a fighter spider in the game, capable of firing projectiles and taking damage.
 *
 * <p>This abstract class serves as a base for both player and enemy spiders, providing
 * common functionalities such as health management and projectile firing positions.</p>
 *
 * <p>Classes extending {@link FighterSpider} must implement the {@link #fireProjectile()} method
 * to define their specific projectile firing behavior.</p>
 */
public abstract class FighterSpider extends ActiveActorDestructible {

	private int health; // Current health of the fighter spider

	/**
	 * Constructs a FighterSpider with the specified image, size, position, and health.
	 *
	 * @param imageName   Name of the image file for the fighter spider (e.g., "player.png").
	 * @param imageHeight Height of the image to be displayed, in pixels.
	 * @param initialXPos Initial X-coordinate position of the spider.
	 * @param initialYPos Initial Y-coordinate position of the spider.
	 * @param health      Initial health of the spider.
	 */
	public FighterSpider(String imageName, int imageHeight, double initialXPos, double initialYPos, int health) {
		super(imageName, imageHeight, initialXPos, initialYPos);
		this.health = health;
	}

	/**
	 * Fires a projectile from the fighter spider.
	 *
	 * @return A new {@link ActiveActorDestructible} projectile if fired, otherwise null.
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
	 * @return The X-coordinate for the projectile.
	 */
	protected double getProjectileXPosition(double xPositionOffset) {
		return getLayoutX() + getTranslateX() + xPositionOffset;
	}

	/**
	 * Calculates the Y position from which a projectile will be fired.
	 *
	 * @param yPositionOffset Offset to adjust the firing position.
	 * @return The Y-coordinate for the projectile.
	 */
	protected double getProjectileYPosition(double yPositionOffset) {
		return getLayoutY() + getTranslateY() + yPositionOffset;
	}

	/**
	 * Checks if the fighter spider's health is zero.
	 *
	 * @return True if health is zero or below, otherwise false.
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
