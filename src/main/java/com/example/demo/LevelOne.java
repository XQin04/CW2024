package com.example.demo;
import javafx.stage.Stage;

public class LevelOne extends LevelParent {

	private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background1.jpg";
	private static final String NEXT_LEVEL = "com.example.demo.LevelTwo";
	private static final int TOTAL_ENEMIES_PER_CYCLE = 5; // 5 enemies per cycle
	private static final int TOTAL_SPAWN_CYCLES = 3; // Spawn 3 times
	private static final int PLAYER_INITIAL_HEALTH = 5;
	private int spawnCyclesCompleted = 0; // Track the number of spawn cycles completed

	public LevelOne(double screenHeight, double screenWidth, Stage stage) {
		super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH, stage, "Level 1");
	}

	@Override
	protected void checkIfGameOver() {
		if (userIsDestroyed()) {
			loseGame();
		} else if (allEnemiesKilled() && spawnCyclesCompleted >= TOTAL_SPAWN_CYCLES) { // Check if all enemies are killed and cycles are complete
			goToNextLevel(NEXT_LEVEL);
		}
	}

	// Check if all enemies are destroyed
	private boolean allEnemiesKilled() {
		return getCurrentNumberOfEnemies() == 0; // All enemies are dead if the enemy list is empty
	}
	@Override
	protected void initializeFriendlyUnits() {
		getRoot().getChildren().add(getUser());
	}


	@Override
	protected void spawnEnemyUnits() {
		if (spawnCyclesCompleted < TOTAL_SPAWN_CYCLES && getCurrentNumberOfEnemies() == 0) {
			// Spawn 5 enemies if the current cycle is not complete and no enemies remain
			for (int i = 0; i < TOTAL_ENEMIES_PER_CYCLE; i++) {
				double newEnemyInitialYPosition = Math.random() * getEnemyMaximumYPosition();
				ActiveActorDestructible newEnemy = new EnemyPlane(getScreenWidth(), newEnemyInitialYPosition);
				addEnemyUnit(newEnemy);
			}
			spawnCyclesCompleted++; // Increment the spawn cycle counter
		}
	}

	@Override
	protected LevelView instantiateLevelView() {
		return new LevelView(getRoot(), PLAYER_INITIAL_HEALTH);
	}



}
