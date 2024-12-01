package com.example.demo.actors;

import com.example.demo.gameplay.LevelParent;
import com.example.demo.projectiles.BossProjectile;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Boss extends FighterPlane {

	private static final String IMAGE_NAME = "enemyboss.png";
	private static final double INITIAL_X_POSITION = 1000.0;
	private static final double INITIAL_Y_POSITION = 400;
	private static final double PROJECTILE_Y_POSITION_OFFSET = 75.0;
	private static final double BOSS_FIRE_RATE = .015;
	private static final double BOSS_SHIELD_PROBABILITY = 0.003;
	private static final int IMAGE_HEIGHT = 200;
	private static final int VERTICAL_VELOCITY = 8;
	private static final int HEALTH = 60;
	private static final int MOVE_FREQUENCY_PER_CYCLE = 5;
	private static final int ZERO = 0;
	private static final int MAX_FRAMES_WITH_SAME_MOVE = 10;
	private static final int MAX_FRAMES_WITH_SHIELD = 250;

	private final List<Integer> movePattern;
	private boolean isShielded;
	private int consecutiveMovesInSameDirection;
	private int indexOfCurrentMove;
	private int framesWithShieldActivated;
	private DropShadow shieldGlowEffect; // Effect for glow

	private LevelParent levelParent; // Reference to the LevelParent
	private final Label shieldAlert; // Alert Label for notifying the user


	public Boss(LevelParent levelParent, Label shieldAlert) {
		super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, INITIAL_Y_POSITION, HEALTH);
		this.levelParent = levelParent; // Assign LevelParent reference
		this.shieldAlert = shieldAlert; // Assign the alert Label
		movePattern = new ArrayList<>();
		consecutiveMovesInSameDirection = 0;
		indexOfCurrentMove = 0;
		framesWithShieldActivated = 0;
		isShielded = false;
		initializeMovePattern();

		// Initialize the glow effect
		shieldGlowEffect = new DropShadow();
		shieldGlowEffect.setColor(Color.YELLOW); // Blue glow for the shield
		shieldGlowEffect.setSpread(0.5);      // Spread makes the glow more intense
	}

	@Override
	public void updatePosition() {
		double initialTranslateY = getTranslateY();
		moveVertically(getNextMove());

		// Get the current position
		double currentY = getLayoutY() + getTranslateY();
		double currentX = getLayoutX() + getTranslateX();

		// Check vertical bounds
		if (currentY < 0) { // Top boundary
			setTranslateY(-getLayoutY());
		} else if (currentY + getBoundsInParent().getHeight() > levelParent.getScreenHeight()) { // Bottom boundary
			setTranslateY(levelParent.getScreenHeight() - getLayoutY() - getBoundsInParent().getHeight());
		}

		// Check horizontal bounds (if needed)
		if (currentX < 0) { // Left boundary
			setTranslateX(-getLayoutX());
		} else if (currentX + getBoundsInParent().getWidth() > levelParent.getScreenWidth()) { // Right boundary
			setTranslateX(levelParent.getScreenWidth() - getLayoutX() - getBoundsInParent().getWidth());
		}
	}


	@Override
	public void updateActor() {
		updatePosition();
		updateShield();
	}

	@Override
	public ActiveActorDestructible fireProjectile() {
		return bossFiresInCurrentFrame() ? new BossProjectile(getProjectileInitialPosition(), levelParent) : null;
	}

	@Override
	public void takeDamage() {
		if (!isShielded) {
			super.takeDamage();
		}
	}

	public javafx.geometry.Bounds getCustomHitbox() {
		// Get the default bounds of the Boss
		javafx.geometry.Bounds originalBounds = super.getBoundsInParent();

		// Adjust the bounds to make the hitbox more precise
		double paddingX = 80; // Horizontal padding
		double paddingY = 80; // Vertical padding
		return new javafx.geometry.BoundingBox(
				originalBounds.getMinX() + paddingX, // Adjust left boundary
				originalBounds.getMinY() + paddingY, // Adjust top boundary
				originalBounds.getWidth() - 2 * paddingX, // Adjust width
				originalBounds.getHeight() - 2 * paddingY // Adjust height
		);
	}

	private void initializeMovePattern() {
		for (int i = 0; i < MOVE_FREQUENCY_PER_CYCLE; i++) {
			movePattern.add(VERTICAL_VELOCITY);
			movePattern.add(-VERTICAL_VELOCITY);
			movePattern.add(ZERO);
		}
		Collections.shuffle(movePattern);
	}

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

	private int getNextMove() {
		int currentMove = movePattern.get(indexOfCurrentMove);
		consecutiveMovesInSameDirection++;
		if (consecutiveMovesInSameDirection == MAX_FRAMES_WITH_SAME_MOVE) {
			Collections.shuffle(movePattern);
			consecutiveMovesInSameDirection = 0;
			indexOfCurrentMove++;
		}
		if (indexOfCurrentMove == movePattern.size()) {
			indexOfCurrentMove = 0;
		}
		return currentMove;
	}

	private boolean bossFiresInCurrentFrame() {
		return Math.random() < BOSS_FIRE_RATE;
	}

	private double getProjectileInitialPosition() {
		return getLayoutY() + getTranslateY() + PROJECTILE_Y_POSITION_OFFSET;
	}

	private boolean shieldShouldBeActivated() {
		return Math.random() < BOSS_SHIELD_PROBABILITY;
	}

	private boolean shieldExhausted() {
		return framesWithShieldActivated == MAX_FRAMES_WITH_SHIELD;
	}

	private void activateShield() {
		isShielded = true;
		framesWithShieldActivated = 0;

		// Apply the shield glow effect
		setEffect(shieldGlowEffect);

		// Display the shield alert
		if (shieldAlert != null) {
			shieldAlert.setText("Boss is shielded!");
			shieldAlert.setVisible(true);
			shieldAlert.toFront();

		}
	}

	private void deactivateShield() {
		isShielded = false;
		framesWithShieldActivated = 0;

		// Remove the shield glow effect
		setEffect(null);

		// Hide the shield alert
		if (shieldAlert != null) {
			shieldAlert.setVisible(false);
		}
	}
}
