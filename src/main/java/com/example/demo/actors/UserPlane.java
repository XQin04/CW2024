package com.example.demo.actors;

import java.util.ArrayList;
import java.util.List;
import com.example.demo.projectiles.UserProjectile;
import com.example.demo.gameplay.LevelParent;
public class UserPlane extends FighterPlane {

	private static final String IMAGE_NAME = "user.png";
	private static final double Y_UPPER_BOUND = 0;
	private static final double Y_LOWER_BOUND = 630.0;
	private static final double X_LEFT_BOUND = 0.0; // Define left boundary
	private static final double X_RIGHT_BOUND = 1000.0; // Define right boundary
	private static final double INITIAL_X_POSITION = 5.0;
	private static final double INITIAL_Y_POSITION = 300.0;
	private static final int IMAGE_HEIGHT = 80;
	private static final int VERTICAL_VELOCITY = 8;
	private static final int HORIZONTAL_VELOCITY = 8; // Add horizontal velocity
	private static final int PROJECTILE_X_POSITION_OFFSET = 110; // Offset from current position
	private static final int PROJECTILE_Y_POSITION_OFFSET = 20; // Offset from current position
	private int verticalVelocityMultiplier = 0;
	private int horizontalVelocityMultiplier = 0; // Horizontal velocity multiplier
	private int numberOfKills = 0;

	// Firepower level
	private int spreadshotCount = 0; // Counter for spreadshot power-ups

	private LevelParent levelParent; // Reference to the LevelParent

	public UserPlane(LevelParent levelParent, int initialHealth) {
		super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, INITIAL_Y_POSITION, initialHealth);
		this.levelParent = levelParent;
	}

	@Override
	public void updatePosition() {
		// Update vertical position
		if (isMovingVertically()) {
			double newYPosition = getLayoutY() + getTranslateY() + (VERTICAL_VELOCITY * verticalVelocityMultiplier);
			if (newYPosition >= Y_UPPER_BOUND && newYPosition <= Y_LOWER_BOUND) {
				this.moveVertically(VERTICAL_VELOCITY * verticalVelocityMultiplier);
			}
		}

		// Update horizontal position
		if (isMovingHorizontally()) {
			double newXPosition = getLayoutX() + getTranslateX() + (HORIZONTAL_VELOCITY * horizontalVelocityMultiplier);
			if (newXPosition >= X_LEFT_BOUND && newXPosition <= X_RIGHT_BOUND) {
				this.moveHorizontally(HORIZONTAL_VELOCITY * horizontalVelocityMultiplier);
			}
		}
	}

	@Override
	public void updateActor() {
		updatePosition();
	}

	@Override
	public ActiveActorDestructible fireProjectile() {
		levelParent.getSoundManager().playShootSound(); // Play shoot sound with AudioClip

		double currentX = getLayoutX() + getTranslateX();
		double currentY = getLayoutY() + getTranslateY();

		if (spreadshotCount > 0) {
			// Create spreadshot projectiles
			ActiveActorDestructible leftProjectile = new UserProjectile(currentX + 100, currentY - 30);
			ActiveActorDestructible leftmidProjectile = new UserProjectile(currentX + 100, currentY - 15);
			ActiveActorDestructible centerProjectile = new UserProjectile(currentX + 100, currentY);
			ActiveActorDestructible rightmidProjectile = new UserProjectile(currentX + 100, currentY + 15);
			ActiveActorDestructible rightProjectile = new UserProjectile(currentX + 100, currentY + 30);

			// Add all projectiles to the scene via LevelParent
			levelParent.addProjectile(leftProjectile);
			levelParent.addProjectile(leftmidProjectile);
			levelParent.addProjectile(centerProjectile);
			levelParent.addProjectile(rightmidProjectile);
			levelParent.addProjectile(rightProjectile);


			spreadshotCount--; // Decrease the spreadshot count after firing
			return centerProjectile; // Return the center projectile for compatibility
		} else {
			// Single shot
			ActiveActorDestructible projectile = new UserProjectile(currentX + 100, currentY);
			levelParent.addProjectile(projectile); // Add to LevelParent
			return projectile;
		}
	}

	public void activateOneTimeSpreadshot() {
		spreadshotCount++; // Increment the spreadshot count when power-up is collected
	}

	public boolean isSpreadshotActive() {
		return spreadshotCount > 0;
	}

	public List<ActiveActorDestructible> getSpreadshotProjectiles() {
		List<ActiveActorDestructible> projectiles = new ArrayList<>();

		double currentX = getLayoutX() + getTranslateX();
		double currentY = getLayoutY() + getTranslateY();

		// Create spreadshot projectiles
		projectiles.add(new UserProjectile(currentX + 100, currentY - 10)); // Left projectile
		projectiles.add(new UserProjectile(currentX + 100, currentY));      // Center projectile
		projectiles.add(new UserProjectile(currentX + 100, currentY + 10)); // Right projectile

		return projectiles;
	}

	private boolean isMovingVertically() {
		return verticalVelocityMultiplier != 0;
	}

	private boolean isMovingHorizontally() {
		return horizontalVelocityMultiplier != 0;
	}

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

	public int getNumberOfKills() {
		return numberOfKills;
	}

	public void incrementKillCount() {
		numberOfKills++;
	}
}

