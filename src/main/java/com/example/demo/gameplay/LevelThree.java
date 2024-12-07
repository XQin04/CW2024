package com.example.demo.gameplay;

import com.example.demo.actors.BossSpider;
import com.example.demo.actors.EnemySpider;
import com.example.demo.powerups.SpreadshotPowerUp;
import com.example.demo.ui.LevelView;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Represents the third and final level of the game.
 * Features waves of enemies, power-ups, and a boss battle.
 */
public class LevelThree extends LevelParent {

    // Constants for level configuration
    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background3.jpg";
    private static final int PLAYER_INITIAL_HEALTH = 5;
    private static final int WAVES_BEFORE_BOSS = 3;
    private static final double POWER_UP_SPAWN_PROBABILITY = 0.02; // 2% chance per frame

    private final BossSpider levelThreeBoss; // Reference to the boss enemy
    private final Label shieldAlert;   // Label for displaying shield alerts
    private int waveCount = 0;         // Tracks the number of waves completed
    private boolean bossSpawned = false; // Indicates if the boss has been spawned

    /**
     * Constructs the final level with the specified screen dimensions and stage.
     *
     * @param screenHeight The height of the game screen.
     * @param screenWidth  The width of the game screen.
     * @param stage        The primary stage for the game.
     */
    public LevelThree(double screenHeight, double screenWidth, Stage stage) {
        super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH, stage, "Final Level");

        // Initialize the shield alert label and add it to the scene graph
        shieldAlert = createShieldAlert();
        getRoot().getChildren().add(shieldAlert);

        // Initialize the boss with a reference to this level and the shield alert
        levelThreeBoss = new BossSpider(this, shieldAlert);
    }

    /**
     * Adds the player's superman to the level.
     */
    @Override
    protected void initializeFriendlyUnits() {
        getRoot().getChildren().add(getUser());
    }

    /**
     * Spawns enemy units and power-ups for the level.
     * Introduces the boss after a predefined number of waves.
     */
    @Override
    protected void spawnEnemyUnits() {
        // Spawn regular enemies if the boss has not yet appeared
        if (enemyManager.getEnemyCount() == 0 && !bossSpawned) {
            waveCount++;
            spawnEnemyWave(waveCount);

            if (waveCount >= WAVES_BEFORE_BOSS) {
                spawnBoss();
            }
        }

        // Randomly spawn spreadshot power-ups
        spawnPowerUp();
    }

    /**
     * Instantiates the view for this level.
     *
     * @return The level view for Level Three.
     */
    @Override
    protected LevelView instantiateLevelView() {
        return new LevelView(getRoot(), PLAYER_INITIAL_HEALTH);
    }

    /**
     * Checks if the game is over based on the player's and boss's state.
     */
    @Override
    protected void checkIfGameOver() {
        if (userIsDestroyed()) {
            loseGame(); // Player loses when their health reaches zero
        } else if (levelThreeBoss.isDestroyed() && enemyManager.getEnemyCount() == 0) {
            winGame(); // Player wins when the boss is destroyed and no enemies remain
        }
    }

    /**
     * Spawns a wave of enemy spiders.
     *
     * @param waveCount The current wave count to determine enemy difficulty.
     */
    private void spawnEnemyWave(int waveCount) {
        for (int i = 0; i < 3 + waveCount; i++) {
            double x = getScreenWidth() + 100;
            double y = Math.random() * getEnemyMaximumYPosition();
            enemyManager.addEnemy(new EnemySpider(x, y)); // Delegate enemy addition to EnemyManager
        }
    }

    /**
     * Spawns the boss enemy into the game.
     */
    private void spawnBoss() {
        enemyManager.addEnemy(levelThreeBoss); // Use EnemyManager for boss addition
        bossSpawned = true;
    }

    /**
     * Attempts to spawn a spreadshot power-up with a fixed probability.
     */
    private void spawnPowerUp() {
        if (Math.random() < POWER_UP_SPAWN_PROBABILITY) {
            double screenWidthLimit = getScreenWidth() / 2; // Limit to the left half of the screen
            double x = Math.random() * screenWidthLimit;
            powerUpManager.addPowerUp(new SpreadshotPowerUp(x, 0)); // Use PowerUpManager for power-ups
        }
    }

    /**
     * Creates and configures a label for displaying shield alerts.
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
