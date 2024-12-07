package com.example.demo.gameplay;

import com.example.demo.actors.BossSpider;
import com.example.demo.ui.LevelView;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Represents the second level of the game.
 * Introduces a BossSpider enemy with shield mechanics and transitions to the next level upon completion.
 */
public class LevelTwo extends LevelParent {

	// Constants for level configuration
	private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background2.png";
	private static final int PLAYER_INITIAL_HEALTH = 5;
	private static final String NEXT_LEVEL = "com.example.demo.gameplay.LevelThree";

	private final BossSpider boss; // Reference to the boss enemy
	private final Label shieldAlert; // Label for displaying shield alerts

	/**
	 * Constructs the second level with the specified screen dimensions and stage.
	 *
	 * @param screenHeight The height of the game screen.
	 * @param screenWidth  The width of the game screen.
	 * @param stage        The primary stage for the game.
	 */
	public LevelTwo(double screenHeight, double screenWidth, Stage stage) {
		super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH, stage, "Level 2");

		// Initialize the shield alert label and add it to the root node
		shieldAlert = createShieldAlert();
		getRoot().getChildren().add(shieldAlert);

		// Initialize the boss with a reference to this level and the shield alert
		boss = new BossSpider(this, shieldAlert);
	}

	/**
	 * Adds the user's superman to the level.
	 */
	@Override
	protected void initializeFriendlyUnits() {
		getRoot().getChildren().add(getUser());
	}

	/**
	 * Checks if the game is over based on the state of the player and boss.
	 */
	@Override
	protected void checkIfGameOver() {
		if (userIsDestroyed()) {
			loseGame(); // Player loses
		} else if (boss.isDestroyed()) {
			goToNextLevel(NEXT_LEVEL); // Player wins and proceeds to the next level
		}
	}

	/**
	 * Spawns enemy units for the level. Adds the boss if no enemies are currently present.
	 */
	@Override
	protected void spawnEnemyUnits() {
		if (enemyManager.getEnemyCount() == 0) {
			enemyManager.addEnemy(boss); // Use EnemyManager to ensure the boss is managed properly
		}
	}

	/**
	 * Instantiates the view for this level.
	 *
	 * @return A generic LevelView for Level Two.
	 */
	@Override
	protected LevelView instantiateLevelView() {
		return new LevelView(getRoot(), PLAYER_INITIAL_HEALTH);
	}

	/**
	 * Creates and configures a Label for displaying shield alerts.
	 *
	 * @return A configured Label object.
	 */
	private Label createShieldAlert() {
		Label label = new Label();
		label.setFont(new Font("Arial", 24));
		label.setTextFill(Color.RED);
		label.setLayoutX(500); // Adjust X position as needed
		label.setLayoutY(50);  // Adjust Y position as needed
		label.setVisible(false); // Initially hidden
		return label;
	}
}
