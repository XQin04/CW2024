package com.example.demo.projectiles;

import com.example.demo.gameplay.LevelParent;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a projectile fired by the boss in the game.
 * <p>
 * This projectile moves horizontally across the screen, slows down upon reaching a certain position,
 * and then explodes into smaller fragments that spread out in different directions.
 * </p>
 */
public class BossProjectile extends Projectile {

    private static final String IMAGE_NAME = "bossweb.png"; // Image for the boss projectile
    private static final int IMAGE_HEIGHT = 75;            // Height of the projectile image
    private static final int HORIZONTAL_VELOCITY = -5;     // Speed of horizontal movement
    private static final int INITIAL_X_POSITION = 950;     // Initial X-coordinate of the projectile
    private final LevelParent levelParent;                // Reference to the current level for interactions
    private boolean exploded = false;                     // Tracks whether the projectile has exploded

    /**
     * Constructs a boss projectile with the specified initial position and level reference.
     *
     * @param initialYPos The initial Y-coordinate of the projectile.
     * @param levelParent The reference to the {@link LevelParent} managing this projectile.
     */
    public BossProjectile(double initialYPos, LevelParent levelParent) {
        super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, initialYPos);
        this.levelParent = levelParent;
    }

    /**
     * Updates the position of the boss projectile.
     * <p>
     * The projectile moves horizontally and triggers an explosion when it reaches a specific position.
     * </p>
     */
    @Override
    public void updatePosition() {
        moveHorizontally(HORIZONTAL_VELOCITY);

        if (getTranslateX() < 300 && !exploded) {
            exploded = true; // Ensure the projectile only explodes once
            scheduleExplosion(); // Trigger the explosion
        }
    }

    /**
     * Updates the behavior of the boss projectile each frame.
     */
    @Override
    public void updateActor() {
        updatePosition();
    }

    /**
     * Schedules the explosion of the boss projectile after a delay.
     * <p>
     * This creates a timed event that triggers the explosion sequence after 1 second.
     * </p>
     */
    private void scheduleExplosion() {
        Timeline explosionTimer = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> explode())
        );
        explosionTimer.setCycleCount(1);
        explosionTimer.play();
    }

    /**
     * Triggers the explosion of the boss projectile.
     * <p>
     * Spawns smaller fragments that spread out in different directions and removes the original projectile.
     * </p>
     */
    private void explode() {
        spawnExplodingFragments(); // Generate fragments from the explosion
        destroy();                 // Destroy the original projectile
    }

    /**
     * Spawns smaller fragments as a result of the explosion.
     * <p>
     * These fragments are created at the current position of the boss projectile and move in random directions.
     * </p>
     */
    private void spawnExplodingFragments() {
        double currentX = getLayoutX() + getTranslateX();
        double currentY = getLayoutY() + getTranslateY();

        // Generate 3 smaller fragments with randomized velocities
        List<Projectile> fragments = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            double randomHorizontalVelocity = -3 + (Math.random() * -20); // Range: [-3, -20]
            double randomVerticalVelocity = -10 + (Math.random() * 25);   // Range: [-10, 15]

            fragments.add(new Fragment(currentX, currentY, randomHorizontalVelocity, randomVerticalVelocity));
        }

        // Add fragments to the level and set their lifetime
        fragments.forEach(fragment -> {
            levelParent.addProjectile(fragment);
            setFragmentLifetime((Fragment) fragment);
        });
    }

    /**
     * Sets the lifetime of a fragment and removes it from the scene after the specified time.
     *
     * @param fragment The fragment whose lifetime is to be set.
     */
    private void setFragmentLifetime(Fragment fragment) {
        Timeline fragmentLifeTimer = new Timeline(
                new KeyFrame(Duration.seconds(2), e -> {
                    fragment.destroy(); // Destroy the fragment after its lifetime
                    levelParent.getRoot().getChildren().remove(fragment); // Remove from the scene
                })
        );
        fragmentLifeTimer.setCycleCount(1);
        fragmentLifeTimer.play();
    }

    /**
     * Represents the smaller fragments created upon the explosion of the boss projectile.
     */
    private class Fragment extends Projectile {

        private static final String FRAGMENT_IMAGE_NAME = "enemyweb.png"; // Image for the fragment
        private final double horizontalVelocity;                          // Horizontal velocity of the fragment
        private final double verticalVelocity;                            // Vertical velocity of the fragment

        /**
         * Constructs a fragment with specified initial position and velocities.
         *
         * @param initialX           The initial X-coordinate of the fragment.
         * @param initialY           The initial Y-coordinate of the fragment.
         * @param horizontalVelocity The horizontal velocity of the fragment.
         * @param verticalVelocity   The vertical velocity of the fragment.
         */
        public Fragment(double initialX, double initialY, double horizontalVelocity, double verticalVelocity) {
            super(FRAGMENT_IMAGE_NAME, 60, initialX, initialY);
            this.horizontalVelocity = horizontalVelocity;
            this.verticalVelocity = verticalVelocity;
        }

        /**
         * Updates the position of the fragment based on its velocities.
         */
        @Override
        public void updatePosition() {
            moveHorizontally(horizontalVelocity);
            moveVertically(verticalVelocity);
        }

        /**
         * Updates the behavior of the fragment each frame, including collision handling.
         * <p>
         * If the fragment collides with the user's superman, it inflicts damage and is destroyed.
         * </p>
         */
        @Override
        public void updateActor() {
            updatePosition();

            // Handle collision with the user's superman
            if (getBoundsInParent().intersects(levelParent.getUser().getBoundsInParent())) {
                levelParent.getUser().takeDamage(); // Inflict damage
                destroy(); // Remove the fragment
            }
        }
    }
}
