package com.example.demo;

import javafx.stage.Stage;

public class LevelTwo extends LevelParent {

	private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background2.jpg";
	private static final int PLAYER_INITIAL_HEALTH = 5;
	private static final String NEXT_LEVEL = "com.example.demo.LevelThree";

	private final Boss boss; // Reference to the boss
	private LevelViewLevelTwo levelView; // Level-specific view

	public LevelTwo(double screenHeight, double screenWidth,  Stage stage) {
		// Call the parent constructor with background image, height, width, and initial player health
		super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH, stage,"Level 2");

		// Instantiate Boss with the current LevelParent reference
		boss = new Boss(this);
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
		// Instantiate and return the specific level view for Level 2
		levelView = new LevelViewLevelTwo(getRoot(), PLAYER_INITIAL_HEALTH);
		return levelView;
	}
}
