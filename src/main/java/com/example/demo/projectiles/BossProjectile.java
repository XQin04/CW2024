package com.example.demo.projectiles;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.*;
import javafx.util.Duration;
import com.example.demo.gameplay.LevelParent;

public class BossProjectile extends Projectile {

	private static final String IMAGE_NAME = "bossweb.png"; // Image for web bomb
	private static final int IMAGE_HEIGHT = 75;
	private static final int HORIZONTAL_VELOCITY = -5; // Reduced velocity to slow down the projectile
	private static final int INITIAL_X_POSITION = 950;

	private boolean exploded = false;
	private LevelParent levelParent;

	public BossProjectile(double initialYPos, LevelParent levelParent) {
		super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, initialYPos);
		this.levelParent = levelParent;
	}

	@Override
	public void updatePosition() {
		moveHorizontally(HORIZONTAL_VELOCITY);

		// Update the position until a certain distance, then trigger explosion
		if (getTranslateX() < 300 && !exploded) {
			exploded = true; // Set exploded to true to avoid multiple explosions
			scheduleExplosion(); // Schedule the explosion after a delay
		}
	}

	@Override
	public void updateActor() {
		updatePosition();
	}

	private void scheduleExplosion() {
		Timeline explosionTimer = new Timeline(
				new KeyFrame(Duration.seconds(1), e -> explode()) // Delay the explosion by 1 second
		);
		explosionTimer.setCycleCount(1);
		explosionTimer.play();
	}

	private void explode() {
		// Create smaller web fragments and add them to the level
		spawnExplodingWebFragments();

		// Destroy this projectile after the explosion
		destroy();
	}

	private void spawnExplodingWebFragments() {
		double currentX = getLayoutX() + getTranslateX();
		double currentY = getLayoutY() + getTranslateY();

		// Create multiple smaller fragments with more widely spread random velocities
		List<Projectile> fragments = new ArrayList<>();
		for (int i = 0; i < 3; i++) { // Create 5 fragments for a wider spread
			double randomHorizontalVelocity = -3 + (Math.random() * -20); // Random value between -3 and -20
			double randomVerticalVelocity = -10 + (Math.random() * 25);   // Random value between -10 and 15 for more vertical spread

			fragments.add(new Fragment(currentX, currentY, randomHorizontalVelocity, randomVerticalVelocity));
		}

		// Add fragments to the scene and make them fade out after a short duration
		for (Projectile fragment : fragments) {
			levelParent.addProjectile(fragment);
			setFragmentLifetime((Fragment) fragment);
		}
	}

	private void setFragmentLifetime(Fragment fragment) {
		Timeline fragmentLifeTimer = new Timeline(
				new KeyFrame(Duration.seconds(2), e -> {
					fragment.destroy();  // Ensure fragment is destroyed
					levelParent.getRoot().getChildren().remove(fragment);
				})
		);
		fragmentLifeTimer.setCycleCount(1);
		fragmentLifeTimer.play();
	}

	private class Fragment extends Projectile {

		private static final String FRAGMENT_IMAGE_NAME = "shoot.png"; // Image for fragments
		private double horizontalVelocity;
		private double verticalVelocity;

		public Fragment(double initialX, double initialY, double horizontalVelocity, double verticalVelocity) {
			super(FRAGMENT_IMAGE_NAME, 60, initialX, initialY);
			this.horizontalVelocity = horizontalVelocity;
			this.verticalVelocity = verticalVelocity;
		}

		@Override
		public void updatePosition() {
			moveHorizontally(horizontalVelocity);
			moveVertically(verticalVelocity);
		}

		@Override
		public void updateActor() {
			updatePosition();

			// Enhanced collision detection to ensure partial collisions can hurt the user
			if (getBoundsInParent().intersects(levelParent.getUser().getBoundsInParent())) {
				levelParent.getUser().takeDamage(); // Reduce user health when collision occurs
				destroy(); // Destroy the fragment after hitting the user
			}
		}
	}
}
