package com.example.demo.gameplay;

import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.actors.EnemySpider;
import com.example.demo.ui.LevelView;
import javafx.stage.Stage;

/**
 * Represents the first level of the game.
 * Handles enemy spawning, game state checks, and transitions to the next level.
 */
public class LevelOne extends LevelParent {

	// Constants for level configuration
	private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background1.jpg";
	private static final String NEXT_LEVEL = "com.example.demo.gameplay.LevelTwo";
	private static final int TOTAL_ENEMIES_PER_CYCLE = 5;
	private static final int TOTAL_SPAWN_CYCLES = 3;
	private static final int PLAYER_INITIAL_HEALTH = 5;

	// Tracks the number of completed enemy spawn cycles
	private int spawnCyclesCompleted = 0;

	/**
	 * Constructs the first level with the specified screen dimensions and stage.
	 *
	 * @param screenHeight The height of the game screen.
	 * @param screenWidth  The width of the game screen.
	 * @param stage        The primary stage for the game.
	 */
	public LevelOne(double screenHeight, double screenWidth, Stage stage) {
		super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH, stage, "Level 1");
	}

	/**
	 * Checks if the game is over or if the player should proceed to the next level.
	 */
	@Override
	protected void checkIfGameOver() {
		if (userIsDestroyed()) {
			loseGame();
		} else if (allEnemiesKilled() && spawnCyclesCompleted >= TOTAL_SPAWN_CYCLES) {
			goToNextLevel(NEXT_LEVEL);
		}
	}

	/**
	 * Determines if all enemies have been killed.
	 *
	 * @return True if all enemies are destroyed, otherwise false.
	 */
	private boolean allEnemiesKilled() {
		return getCurrentNumberOfEnemies() == 0;
	}

	/**
	 * Initializes friendly units (e.g., player's superman) at the start of the level.
	 */
	@Override
	protected void initializeFriendlyUnits() {
		getRoot().getChildren().add(getUser());
	}

	/**
	 * Spawns enemy units if the spawn cycle is incomplete and no enemies remain.
	 */
	@Override
	protected void spawnEnemyUnits() {
		if (spawnCyclesCompleted < TOTAL_SPAWN_CYCLES && getCurrentNumberOfEnemies() == 0) {
			for (int i = 0; i < TOTAL_ENEMIES_PER_CYCLE; i++) {
				double newEnemyInitialYPosition = Math.random() * getEnemyMaximumYPosition();
				ActiveActorDestructible newEnemy = new EnemySpider(getScreenWidth(), newEnemyInitialYPosition);
				addEnemyUnit(newEnemy);
			}
			spawnCyclesCompleted++;
		}
	}

	/**
	 * Creates and returns the view for this level.
	 *
	 * @return The level view for Level One.
	 */
	@Override
	protected LevelView instantiateLevelView() {
		return new LevelView(getRoot(), PLAYER_INITIAL_HEALTH);
	}
}
