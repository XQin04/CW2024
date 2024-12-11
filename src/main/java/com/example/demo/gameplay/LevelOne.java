package com.example.demo.gameplay;

import com.example.demo.actors.enemies.EnemySpider;
import com.example.demo.ui.gameplayUI.LevelView;
import javafx.stage.Stage;

/**
 * Represents the first level of the game.
 *
 * <p>This class defines the specific behavior for Level One, including:
 * - Initialization of the level's background and player.
 * - Spawning enemy units in cycles.
 * - Checking for game over conditions and transitioning to the next level.</p>
 */
public class LevelOne extends LevelParent {

    // Constants for level configuration
    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background1.jpg"; // Background image path
    private static final String NEXT_LEVEL = "com.example.demo.gameplay.LevelTwo"; // Class name of the next level
    private static final int TOTAL_ENEMIES_PER_CYCLE = 5; // Number of enemies spawned per cycle
    private static final int TOTAL_SPAWN_CYCLES = 3; // Total number of spawn cycles for this level
    private static final int PLAYER_INITIAL_HEALTH = 5; // Initial health of the player

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
     *
     * <p>Conditions:
     * - The game ends if the player's health reaches zero.
     * - The level transitions to the next if all enemies are defeated and all spawn cycles are completed.</p>
     */
    @Override
    protected void checkIfGameOver() {
        if (userIsDestroyed()) {
            loseGame(); // Handle game over scenario
        } else if (enemyManager.getEnemyCount() == 0 && spawnCyclesCompleted >= TOTAL_SPAWN_CYCLES) {
            goToNextLevel(NEXT_LEVEL); // Transition to the next level
        }
    }

    /**
     * Initializes friendly units (e.g., player's superman) at the start of the level.
     * <p>Adds the player's character to the scene graph.</p>
     */
    @Override
    protected void initializeFriendlyUnits() {
        getRoot().getChildren().add(getUser());
    }

    /**
     * Spawns enemy units if the spawn cycle is incomplete and no enemies remain.
     *
     * <p>Behavior:
     * - Spawns a fixed number of enemies per cycle.
     * - Ensures that enemies are spawned only if all previous enemies are defeated.</p>
     */
    @Override
    protected void spawnEnemyUnits() {
        if (spawnCyclesCompleted < TOTAL_SPAWN_CYCLES && enemyManager.getEnemyCount() == 0) {
            for (int i = 0; i < TOTAL_ENEMIES_PER_CYCLE; i++) {
                double newEnemyInitialYPosition = Math.random() * getEnemyMaximumYPosition(); // Random Y position
                EnemySpider newEnemy = new EnemySpider(getScreenWidth(), newEnemyInitialYPosition);
                enemyManager.addEnemy(newEnemy); // Add the new enemy to the game
            }
            spawnCyclesCompleted++; // Increment the spawn cycle count
        }
    }

    /**
     * Creates and returns the view for this level.
     *
     * @return The level view for Level One, which includes the player's health display.
     */
    @Override
    protected LevelView instantiateLevelView() {
        return new LevelView(getRoot(), PLAYER_INITIAL_HEALTH);
    }
}
