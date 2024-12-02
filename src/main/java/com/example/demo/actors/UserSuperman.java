package com.example.demo.actors;

import com.example.demo.gameplay.LevelParent;
import com.example.demo.projectiles.UserProjectile;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the user's superman in the game, capable of moving,
 * firing projectiles, and collecting power-ups like spreadshot.
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
	private int numberOfKills = 0;
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
	 * @return The center projectile or one of the spreadshot projectiles for compatibility.
	 */
	@Override
	public ActiveActorDestructible fireProjectile() {
		levelParent.getSoundManager().playShootSound(); // Play shoot sound

		double currentX = getLayoutX() + getTranslateX();
		double currentY = getLayoutY() + getTranslateY();

		if (spreadshotCount > 0) {
			// Create and add spreadshot projectiles
			List<ActiveActorDestructible> spreadshotProjectiles = getSpreadshotProjectiles();
			for (ActiveActorDestructible projectile : spreadshotProjectiles) {
				levelParent.addProjectile(projectile);
			}
			spreadshotCount--; // Decrease the spreadshot count
			return spreadshotProjectiles.get(spreadshotProjectiles.size() / 2); // Return center projectile
		} else {
			// Create and add a single projectile
			ActiveActorDestructible projectile = new UserProjectile(currentX + 100, currentY);
			levelParent.addProjectile(projectile);
			return projectile;
		}
	}

	/**
	 * Activates a one-time spreadshot power-up.
	 */
	public void activateOneTimeSpreadshot() {
		spreadshotCount++;
	}


	/**
	 * Creates and returns a list of spreadshot projectiles.
	 *
	 * @return A list of spreadshot projectiles.
	 */
	public List<ActiveActorDestructible> getSpreadshotProjectiles() {
		List<ActiveActorDestructible> projectiles = new ArrayList<>();

		double currentX = getLayoutX() + getTranslateX();
		double currentY = getLayoutY() + getTranslateY();

		// Create spreadshot projectiles
		projectiles.add(new UserProjectile(currentX + 100, currentY - 30)); // Left
		projectiles.add(new UserProjectile(currentX + 100, currentY - 15)); // Left-mid
		projectiles.add(new UserProjectile(currentX + 100, currentY));      // Center
		projectiles.add(new UserProjectile(currentX + 100, currentY + 15)); // Right-mid
		projectiles.add(new UserProjectile(currentX + 100, currentY + 30)); // Right

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
	public void moveUp() {
		verticalVelocityMultiplier = -1;
	}

	public void moveDown() {
		verticalVelocityMultiplier = 1;
	}

	public void moveLeft() {
		horizontalVelocityMultiplier = -1;
	}

	public void moveRight() {
		horizontalVelocityMultiplier = 1;
	}

	public void stopVertical() {
		verticalVelocityMultiplier = 0;
	}

	public void stopHorizontal() {
		horizontalVelocityMultiplier = 0;
	}

	public void incrementKillCount() {
		numberOfKills++;
	}
}
