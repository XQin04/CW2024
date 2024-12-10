package com.example.demo.gameplay;

import com.example.demo.actors.enemies.BossSpider;
import com.example.demo.actors.enemies.EnemySpider;
import com.example.demo.powerups.SpreadshotPowerUp;
import com.example.demo.ui.gameplayUI.LevelView;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Represents the third and final level of the game.
 * <p>This level includes waves of enemies, random power-up spawns, and a challenging boss battle.
 * The player wins by defeating the boss and all remaining enemies.</p>
 */
public class LevelThree extends LevelParent {

    // Constants for level configuration
    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background3.jpg"; // Background image for the level
    private static final int PLAYER_INITIAL_HEALTH = 5; // Initial health of the player
    private static final int WAVES_BEFORE_BOSS = 3; // Number of waves before the boss appears
    private static final double POWER_UP_SPAWN_PROBABILITY = 0.02; // Probability of spawning a power-up each frame

    private final BossSpider levelThreeBoss; // Reference to the boss enemy
    private int waveCount = 0;         // Tracks the number of completed waves
    private boolean bossSpawned = false; // Indicates if the boss has been introduced

    /**
     * Constructs the final level with the specified screen dimensions and stage.
     *
     * @param screenHeight The height of the game screen.
     * @param screenWidth  The width of the game screen.
     * @param stage        The primary stage for the game.
     */
    public LevelThree(double screenHeight, double screenWidth, Stage stage) {
        super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH, stage, "Final Level");

        // Initialize and add the shield alert label to the game root
        Label shieldAlert = createShieldAlert();
        getRoot().getChildren().add(shieldAlert);

        // Initialize the boss enemy
        levelThreeBoss = new BossSpider(this, shieldAlert);
    }

    /**
     * Adds the player's superman to the level.
     * <p>This ensures the player's character is present on the screen at the start of the level.</p>
     */
    @Override
    protected void initializeFriendlyUnits() {
        getRoot().getChildren().add(getUser());
    }

    /**
     * Spawns enemy units and power-ups for the level.
     * <p>Waves of enemies appear until the boss is introduced. Power-ups are spawned randomly.</p>
     */
    @Override
    protected void spawnEnemyUnits() {
        // Spawn waves of enemies if the boss has not yet appeared
        if (enemyManager.getEnemyCount() == 0 && !bossSpawned) {
            waveCount++;
            spawnEnemyWave(waveCount);

            // Introduce the boss after completing the predefined number of waves
            if (waveCount >= WAVES_BEFORE_BOSS) {
                spawnBoss();
            }
        }

        // Attempt to spawn a power-up
        spawnPowerUp();
    }

    /**
     * Instantiates the view for this level.
     *
     * @return A LevelView object that displays the player's health and level-specific elements.
     */
    @Override
    protected LevelView instantiateLevelView() {
        return new LevelView(getRoot(), PLAYER_INITIAL_HEALTH);
    }

    /**
     * Checks if the game is over based on the player's health and the boss's state.
     */
    @Override
    protected void checkIfGameOver() {
        if (userIsDestroyed()) {
            loseGame(); // Trigger game-over logic if the player is destroyed
        } else if (levelThreeBoss.isDestroyed() && enemyManager.getEnemyCount() == 0) {
            winGame(); // Trigger win logic if the boss and all enemies are defeated
        }
    }

    /**
     * Spawns a wave of enemy spiders.
     *
     * @param waveCount The current wave count to adjust the difficulty.
     */
    private void spawnEnemyWave(int waveCount) {
        for (int i = 0; i < 3 + waveCount; i++) { // Increase enemy count with each wave
            double x = getScreenWidth() + 100; // Position enemies off-screen to the right
            double y = Math.random() * getEnemyMaximumYPosition(); // Random vertical position
            enemyManager.addEnemy(new EnemySpider(x, y)); // Add enemy using EnemyManager
        }
    }

    /**
     * Spawns the boss enemy into the game.
     * <p>The boss is introduced after all waves of regular enemies are completed.</p>
     */
    private void spawnBoss() {
        enemyManager.addEnemy(levelThreeBoss); // Use EnemyManager to handle boss logic
        bossSpawned = true; // Mark the boss as spawned
    }

    /**
     * Attempts to spawn a spreadshot power-up at random intervals.
     */
    private void spawnPowerUp() {
        if (Math.random() < POWER_UP_SPAWN_PROBABILITY) { // Check if a power-up should spawn
            double screenWidthLimit = getScreenWidth() / 2; // Restrict spawning to the left half of the screen
            double x = Math.random() * screenWidthLimit; // Random horizontal position
            powerUpManager.addPowerUp(new SpreadshotPowerUp(x, 0)); // Add power-up using PowerUpManager
        }
    }

    /**
     * Creates and configures a label for displaying shield activation messages.
     *
     * @return A configured Label object with font, color, and layout settings.
     */
    private Label createShieldAlert() {
        Label label = new Label();
        label.setFont(new Font("Arial", 24)); // Set font style and size
        label.setTextFill(Color.RED); // Set text color
        label.setLayoutX(500); // Position the label horizontally
        label.setLayoutY(50);  // Position the label vertically
        label.setVisible(false); // Hide the label initially
        return label;
    }
}
