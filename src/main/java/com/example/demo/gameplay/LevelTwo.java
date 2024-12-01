package com.example.demo.gameplay;

import com.example.demo.actors.Boss;
import com.example.demo.ui.LevelView;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class LevelTwo extends LevelParent {

	private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background2.png";
	private static final int PLAYER_INITIAL_HEALTH = 5;
	private static final String NEXT_LEVEL = "com.example.demo.gameplay.LevelThree";

	private final Boss boss; // Reference to the boss
	private final Label shieldAlert; // Label for shield alerts

	public LevelTwo(double screenHeight, double screenWidth, Stage stage) {
		// Call the parent constructor with background image, height, width, and initial player health
		super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH, stage, "Level 2");

		// Initialize the shield alert label
		shieldAlert = createShieldAlert();
		getRoot().getChildren().add(shieldAlert); // Add shield alert to the root node

		// Instantiate the boss with a reference to the current LevelParent and shield alert
		boss = new Boss(this, shieldAlert);
	}

	@Override
	protected void initializeFriendlyUnits() {
		// Add the player's user plane to the scene graph
		getRoot().getChildren().add(getUser());
	}

	@Override
	protected void checkIfGameOver() {
		// Check if user is destroyed or if boss is destroyed
		if (userIsDestroyed()) {
			loseGame(); // Player loses
		} else if (boss.isDestroyed()) {
			goToNextLevel(NEXT_LEVEL); // Player wins and proceeds to Level 3
		}
	}

	@Override
	protected void spawnEnemyUnits() {
		// If no enemies currently exist, add the boss to the scene
		if (getCurrentNumberOfEnemies() == 0) {
			addEnemyUnit(boss);
		}
	}

	@Override
	protected LevelView instantiateLevelView() {
		// Instantiate and return a generic LevelView (or create a custom one if necessary)
		return new LevelView(getRoot(), PLAYER_INITIAL_HEALTH);
	}

	/**
	 * Creates a Label for displaying shield alerts.
	 *
	 * @return a configured Label object
	 */
	private Label createShieldAlert() {
		Label label = new Label();
		label.setFont(new Font("Arial", 24));
		label.setTextFill(Color.RED);
		label.setLayoutX(500); // Adjust position as needed
		label.setLayoutY(50); // Adjust position as needed
		label.setVisible(false); // Initially hidden
		return label;
	}
}
