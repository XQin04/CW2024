package com.example.demo.actors.enemies;

import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.gameplay.LevelParent;
import com.example.demo.projectiles.BossProjectile;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents the BossSpider enemy in the game.
 *
 * <p>The BossSpider is a powerful enemy with unique behaviors, such as firing projectiles,
 * activating shields, and following a predefined movement pattern. This class
 * extends {@link FighterSpider} to reuse base functionality while adding
 * boss-specific features.</p>
 */
public class BossSpider extends FighterSpider {

    // Constants for BossSpider properties
    private static final String IMAGE_NAME = "enemyboss.png";
    private static final double INITIAL_X_POSITION = 1000.0;
    private static final double INITIAL_Y_POSITION = 400.0;
    private static final double PROJECTILE_Y_POSITION_OFFSET = 75.0;
    private static final double BOSS_FIRE_RATE = 0.015;
    private static final double BOSS_SHIELD_PROBABILITY = 0.003;
    private static final int IMAGE_HEIGHT = 200;
    private static final int VERTICAL_VELOCITY = 8;
    private static final int HEALTH = 25;
    private static final int MOVE_FREQUENCY_PER_CYCLE = 5;
    private static final int ZERO = 0;
    private static final int MAX_FRAMES_WITH_SAME_MOVE = 10;
    private static final int MAX_FRAMES_WITH_SHIELD = 250;

    // Movement and shield properties
    private final List<Integer> movePattern = new ArrayList<>();
    // Visual effects
    private final DropShadow shieldGlowEffect;
    // References
    private final LevelParent levelParent;
    private final Label shieldAlert;
    private boolean isShielded;
    private int consecutiveMovesInSameDirection = 0;
    private int indexOfCurrentMove = 0;
    private int framesWithShieldActivated = 0;

    /**
     * Constructs a BossSpider instance with the specified parent level and alert label.
     *
     * @param levelParent The parent level managing the BossSpider.
     * @param shieldAlert A label to display shield activation messages.
     */
    public BossSpider(LevelParent levelParent, Label shieldAlert) {
        super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, INITIAL_Y_POSITION, HEALTH);
        this.levelParent = levelParent;
        this.shieldAlert = shieldAlert;

        initializeMovePattern();

        // Initialize the shield's visual glow effect
        shieldGlowEffect = new DropShadow();
        shieldGlowEffect.setColor(Color.YELLOW);
        shieldGlowEffect.setSpread(0.5); // Intense glow effect
    }

    /**
     * Updates the position of the BossSpider based on its movement pattern and boundaries.
     * Ensures the spider stays within the screen limits.
     */
    @Override
    public void updatePosition() {
        moveVertically(getNextMove());

        // Ensure BossSpider stays within the vertical and horizontal screen bounds
        double currentY = getLayoutY() + getTranslateY();
        double currentX = getLayoutX() + getTranslateX();

        if (currentY < 0) {
            setTranslateY(-getLayoutY());
        } else if (currentY + getBoundsInParent().getHeight() > levelParent.getScreenHeight()) {
            setTranslateY(levelParent.getScreenHeight() - getLayoutY() - getBoundsInParent().getHeight());
        }

        if (currentX < 0) {
            setTranslateX(-getLayoutX());
        } else if (currentX + getBoundsInParent().getWidth() > levelParent.getScreenWidth()) {
            setTranslateX(levelParent.getScreenWidth() - getLayoutX() - getBoundsInParent().getWidth());
        }
    }

    /**
     * Updates the state of the BossSpider, including its position and shield status.
     */
    @Override
    public void updateActor() {
        updatePosition();
        updateShield();
    }

    /**
     * Fires a projectile from the BossSpider with a predefined probability.
     *
     * @return A {@link BossProjectile} if fired, otherwise null.
     */
    @Override
    public ActiveActorDestructible fireProjectile() {
        if (bossFiresInCurrentFrame()) {
            return new BossProjectile(getProjectileInitialPosition(), levelParent);
        }
        return null;
    }

    /**
     * Handles damage taken by the BossSpider. Damage is ignored if the shield is active.
     */
    @Override
    public void takeDamage() {
        if (!isShielded) {
            super.takeDamage();
        }
    }

    /**
     * Creates a custom hit box for the BossSpider to allow more precise collision detection.
     *
     * @return Adjusted hit box bounds.
     */
    public javafx.geometry.Bounds getCustomHitbox() {
        javafx.geometry.Bounds originalBounds = super.getBoundsInParent();
        double paddingX = 80;
        double paddingY = 80;
        return new javafx.geometry.BoundingBox(
                originalBounds.getMinX() + paddingX,
                originalBounds.getMinY() + paddingY,
                originalBounds.getWidth() - 2 * paddingX,
                originalBounds.getHeight() - 2 * paddingY
        );
    }

    /**
     * Initializes the movement pattern for the BossSpider.
     * The pattern alternates between moving up, down, and staying still.
     */
    private void initializeMovePattern() {
        for (int i = 0; i < MOVE_FREQUENCY_PER_CYCLE; i++) {
            movePattern.add(VERTICAL_VELOCITY);
            movePattern.add(-VERTICAL_VELOCITY);
            movePattern.add(ZERO);
        }
        Collections.shuffle(movePattern);
    }

    /**
     * Updates the shield's activation status and visual effects.
     * Activates or deactivates the shield based on predefined conditions.
     */
    private void updateShield() {
        if (isShielded) {
            framesWithShieldActivated++;
            if (framesWithShieldActivated >= MAX_FRAMES_WITH_SHIELD) {
                deactivateShield();
            }
        } else if (shieldShouldBeActivated()) {
            activateShield();
        }
    }

    /**
     * Determines the next move in the movement pattern.
     *
     * @return The next vertical velocity for movement.
     */
    private int getNextMove() {
        int currentMove = movePattern.get(indexOfCurrentMove);
        consecutiveMovesInSameDirection++;
        if (consecutiveMovesInSameDirection >= MAX_FRAMES_WITH_SAME_MOVE) {
            Collections.shuffle(movePattern);
            consecutiveMovesInSameDirection = 0;
            indexOfCurrentMove++;
        }
        if (indexOfCurrentMove >= movePattern.size()) {
            indexOfCurrentMove = 0;
        }
        return currentMove;
    }

    /**
     * Checks if the BossSpider should fire a projectile in the current frame.
     *
     * @return True if the BossSpider fires, otherwise false.
     */
    private boolean bossFiresInCurrentFrame() {
        return Math.random() < BOSS_FIRE_RATE;
    }

    /**
     * Calculates the initial position for a projectile fired by the BossSpider.
     *
     * @return The Y-coordinate for the projectile's initial position.
     */
    private double getProjectileInitialPosition() {
        return getLayoutY() + getTranslateY() + PROJECTILE_Y_POSITION_OFFSET;
    }

    /**
     * Determines if the shield should be activated.
     *
     * @return True if the shield activates, otherwise false.
     */
    private boolean shieldShouldBeActivated() {
        return Math.random() < BOSS_SHIELD_PROBABILITY;
    }

    /**
     * Activates the shield, applying visual effects and notifying the player.
     */
    protected void activateShield() {
        isShielded = true;
        framesWithShieldActivated = 0;
        setEffect(shieldGlowEffect);

        if (shieldAlert != null) {
            shieldAlert.setText("BossSpider is shielded!");
            shieldAlert.setVisible(true);
            shieldAlert.toFront();
        }
    }

    /**
     * Deactivates the shield, removing visual effects and hiding the alert.
     */
    protected void deactivateShield() {
        isShielded = false;
        framesWithShieldActivated = 0;
        setEffect(null);

        if (shieldAlert != null) {
            shieldAlert.setVisible(false);
        }
    }
}
