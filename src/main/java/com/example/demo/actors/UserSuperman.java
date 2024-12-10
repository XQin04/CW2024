package com.example.demo.actors;

import com.example.demo.gameplay.levels.LevelParent;
import com.example.demo.projectiles.UserProjectile;
import com.example.demo.managers.ProjectileManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the player's character in the game, "UserSuperman."
 *
 * <p>The UserSuperman can move within defined screen boundaries, fire projectiles,
 * and use power-ups like spreadshot. It interacts with the game through its parent
 * level, leveraging game managers for sound, projectiles, and interactions.</p>
 *
 * <p>This class extends {@link FighterSpider} to utilize health management
 * and interaction capabilities.</p>
 */
public class UserSuperman extends FighterSpider {

	// Constants for the user's superman properties
	private static final String IMAGE_NAME = "user.png";
	private static final double Y_UPPER_BOUND = 0;
	private static final double Y_LOWER_BOUND = 630.0;
	private static final double X_LEFT_BOUND = 0.0;
	private static final double X_RIGHT_BOUND = 900.0;
	private static final double INITIAL_X_POSITION = 5.0;
	private static final double INITIAL_Y_POSITION = 300.0;
	private static final int IMAGE_HEIGHT = 80;
	private static final int VERTICAL_VELOCITY = 8;
	private static final int HORIZONTAL_VELOCITY = 8;

	// Movement control
	private int verticalVelocityMultiplier = 0;
	private int horizontalVelocityMultiplier = 0;

	// Game-specific properties
	private int numberOfKills = 0; // Number of kills by the player
	private int spreadshotCount = 0; // Counter for spreadshot power-ups

	private final LevelParent levelParent; // Reference to the LevelParent for scene interactions

	/**
	 * Constructs a UserSuperman with the specified level parent and initial health.
	 *
	 * @param levelParent   The parent level managing this superman.
	 * @param initialHealth Initial health of the user's superman.
	 */
	public UserSuperman(LevelParent levelParent, int initialHealth) {
		super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, INITIAL_Y_POSITION, initialHealth);
		this.levelParent = levelParent;
	}

	/**
	 * Updates the position of the user's superman based on velocity and boundaries.
	 */
	@Override
	public void updatePosition() {
		// Update vertical position
		if (isMovingVertically()) {
			double newYPosition = getLayoutY() + getTranslateY() + (VERTICAL_VELOCITY * verticalVelocityMultiplier);
			if (newYPosition >= Y_UPPER_BOUND && newYPosition <= Y_LOWER_BOUND) {
				moveVertically(VERTICAL_VELOCITY * verticalVelocityMultiplier);
			}
		}

		// Update horizontal position
		if (isMovingHorizontally()) {
			double newXPosition = getLayoutX() + getTranslateX() + (HORIZONTAL_VELOCITY * horizontalVelocityMultiplier);
			if (newXPosition >= X_LEFT_BOUND && newXPosition <= X_RIGHT_BOUND) {
				moveHorizontally(HORIZONTAL_VELOCITY * horizontalVelocityMultiplier);
			}
		}
	}

	/**
	 * Updates the state of the user's superman, including position and interactions.
	 */
	@Override
	public void updateActor() {
		updatePosition();
	}

	/**
	 * Fires a projectile. If a spreadshot power-up is active, fires multiple projectiles.
	 *
	 * @return The center projectile (or one of the spreadshot projectiles for compatibility).
	 */
	@Override
	public ActiveActorDestructible fireProjectile() {
		levelParent.getSoundManager().playShootSound(); // Play shoot sound

		double currentX = getLayoutX() + getTranslateX();
		double currentY = getLayoutY() + getTranslateY();

		ProjectileManager projectileManager = levelParent.getProjectileManager();

		if (spreadshotCount > 0) {
			// Create and add spreadshot projectiles
			List<ActiveActorDestructible> spreadshotProjectiles = getSpreadshotProjectiles();
			for (ActiveActorDestructible projectile : spreadshotProjectiles) {
				projectileManager.addUserProjectile(projectile);
			}
			spreadshotCount--; // Decrease the spreadshot count
			return spreadshotProjectiles.get(spreadshotProjectiles.size() / 2); // Return center projectile
		} else {
			// Create and add a single projectile
			ActiveActorDestructible projectile = new UserProjectile(currentX + 100, currentY);
			projectileManager.addUserProjectile(projectile);
			return projectile;
		}
	}

	/**
	 * Activates a one-time spreadshot power-up, enabling the firing of multiple projectiles.
	 */
	public void activateOneTimeSpreadshot() {
		spreadshotCount++;
	}

	/**
	 * Creates and returns a list of spreadshot projectiles.
	 *
	 * @return A list of spreadshot projectiles fired in an arrow-like pattern.
	 */
	public List<ActiveActorDestructible> getSpreadshotProjectiles() {
		List<ActiveActorDestructible> projectiles = new ArrayList<>();

		double currentX = getLayoutX() + getTranslateX();
		double currentY = getLayoutY() + getTranslateY();

		// Create spreadshot projectiles in an arrow-like pattern
		projectiles.add(new UserProjectile(currentX + 75, currentY - 30)); // Left (Upwards)
		projectiles.add(new UserProjectile(currentX + 85, currentY - 15)); // Slightly Left (Upwards)
		projectiles.add(new UserProjectile(currentX + 100, currentY));     // Center (Straight)
		projectiles.add(new UserProjectile(currentX + 85, currentY + 15)); // Slightly Right (Downwards)
		projectiles.add(new UserProjectile(currentX + 75, currentY + 30)); // Right (Downwards)

		return projectiles;
	}

	/**
	 * Checks if the user's superman is moving vertically.
	 *
	 * @return True if moving vertically, otherwise false.
	 */
	private boolean isMovingVertically() {
		return verticalVelocityMultiplier != 0;
	}

	/**
	 * Checks if the user's superman is moving horizontally.
	 *
	 * @return True if moving horizontally, otherwise false.
	 */
	private boolean isMovingHorizontally() {
		return horizontalVelocityMultiplier != 0;
	}

	// Movement controls

	/**
	 * Starts moving the user's superman upward.
	 */
	public void moveUp() {
		verticalVelocityMultiplier = -1;
	}

	/**
	 * Starts moving the user's superman downward.
	 */
	public void moveDown() {
		verticalVelocityMultiplier = 1;
	}

	/**
	 * Starts moving the user's superman to the left.
	 */
	public void moveLeft() {
		horizontalVelocityMultiplier = -1;
	}

	/**
	 * Starts moving the user's superman to the right.
	 */
	public void moveRight() {
		horizontalVelocityMultiplier = 1;
	}

	/**
	 * Stops the vertical movement of the user's superman.
	 */
	public void stopVertical() {
		verticalVelocityMultiplier = 0;
	}

	/**
	 * Stops the horizontal movement of the user's superman.
	 */
	public void stopHorizontal() {
		horizontalVelocityMultiplier = 0;
	}

	/**
	 * Increments the kill count for the user's superman.
	 */
	public void incrementKillCount() {
		numberOfKills++;
	}
}
