package com.example.demo.projectiles;

import com.example.demo.gameplay.LevelParent;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a projectile fired by the boss.
 * This projectile travels horizontally, slows down, and explodes into smaller fragments.
 */
public class BossProjectile extends Projectile {

	private static final String IMAGE_NAME = "bossweb.png"; // Image for the boss projectile
	private static final int IMAGE_HEIGHT = 75;
	private static final int HORIZONTAL_VELOCITY = -5; // Horizontal movement speed
	private static final int INITIAL_X_POSITION = 950;

	private boolean exploded = false;
	private final LevelParent levelParent;

	/**
	 * Constructor for creating a boss projectile.
	 *
	 * @param initialYPos  The initial Y-coordinate of the projectile.
	 * @param levelParent  The reference to the LevelParent for interactions.
	 */
	public BossProjectile(double initialYPos, LevelParent levelParent) {
		super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, initialYPos);
		this.levelParent = levelParent;
	}

	/**
	 * Updates the position of the projectile.
	 * Moves horizontally until it reaches a specific position, then triggers an explosion.
	 */
	@Override
	public void updatePosition() {
		moveHorizontally(HORIZONTAL_VELOCITY);

		if (getTranslateX() < 300 && !exploded) {
			exploded = true; // Avoid multiple explosions
			scheduleExplosion(); // Trigger an explosion after a delay
		}
	}

	/**
	 * Updates the behavior of the projectile each frame.
	 */
	@Override
	public void updateActor() {
		updatePosition();
	}

	/**
	 * Schedules the explosion of the projectile after a delay.
	 */
	private void scheduleExplosion() {
		Timeline explosionTimer = new Timeline(
				new KeyFrame(Duration.seconds(1), e -> explode())
		);
		explosionTimer.setCycleCount(1);
		explosionTimer.play();
	}

	/**
	 * Handles the explosion of the boss projectile.
	 * Spawns smaller fragments and destroys the original projectile.
	 */
	private void explode() {
		spawnExplodingFragments(); // Create fragments upon explosion
		destroy(); // Remove the original projectile
	}

	/**
	 * Spawns smaller fragments as a result of the explosion.
	 */
	private void spawnExplodingFragments() {
		double currentX = getLayoutX() + getTranslateX();
		double currentY = getLayoutY() + getTranslateY();

		// Generate smaller fragments with randomized velocities
		List<Projectile> fragments = new ArrayList<>();
		for (int i = 0; i < 3; i++) { // Spawn 3 fragments
			double randomHorizontalVelocity = -3 + (Math.random() * -20); // Velocity range [-3, -20]
			double randomVerticalVelocity = -10 + (Math.random() * 25);   // Velocity range [-10, 15]

			fragments.add(new Fragment(currentX, currentY, randomHorizontalVelocity, randomVerticalVelocity));
		}

		// Add fragments to the level and set their lifetime
		fragments.forEach(fragment -> {
			levelParent.addProjectile(fragment);
			setFragmentLifetime((Fragment) fragment);
		});
	}

	/**
	 * Sets the lifetime of a fragment. Removes it from the scene after a delay.
	 *
	 * @param fragment The fragment whose lifetime is to be set.
	 */
	private void setFragmentLifetime(Fragment fragment) {
		Timeline fragmentLifeTimer = new Timeline(
				new KeyFrame(Duration.seconds(2), e -> {
					fragment.destroy(); // Destroy the fragment after its lifetime ends
					levelParent.getRoot().getChildren().remove(fragment); // Remove from the scene
				})
		);
		fragmentLifeTimer.setCycleCount(1);
		fragmentLifeTimer.play();
	}

	/**
	 * Represents the smaller fragments created upon explosion.
	 */
	private class Fragment extends Projectile {

		private static final String FRAGMENT_IMAGE_NAME = "enemyweb.png"; // Image for the fragment
		private final double horizontalVelocity;
		private final double verticalVelocity;

		/**
		 * Constructor for creating a fragment.
		 *
		 * @param initialX            The initial X-coordinate of the fragment.
		 * @param initialY            The initial Y-coordinate of the fragment.
		 * @param horizontalVelocity  The horizontal velocity of the fragment.
		 * @param verticalVelocity    The vertical velocity of the fragment.
		 */
		public Fragment(double initialX, double initialY, double horizontalVelocity, double verticalVelocity) {
			super(FRAGMENT_IMAGE_NAME, 60, initialX, initialY);
			this.horizontalVelocity = horizontalVelocity;
			this.verticalVelocity = verticalVelocity;
		}

		/**
		 * Updates the position of the fragment.
		 * Moves based on its horizontal and vertical velocities.
		 */
		@Override
		public void updatePosition() {
			moveHorizontally(horizontalVelocity);
			moveVertically(verticalVelocity);
		}

		/**
		 * Updates the behavior of the fragment each frame.
		 * Handles collisions with the user superman.
		 */
		@Override
		public void updateActor() {
			updatePosition();

			// Check for collisions with the user superman
			if (getBoundsInParent().intersects(levelParent.getUser().getBoundsInParent())) {
				levelParent.getUser().takeDamage(); // Inflict damage to the user
				destroy(); // Destroy the fragment after collision
			}
		}
	}
}
