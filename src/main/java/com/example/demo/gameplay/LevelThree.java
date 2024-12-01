package com.example.demo.gameplay;

import com.example.demo.actors.Boss;
import com.example.demo.actors.EnemyPlane;
import com.example.demo.powerups.SpreadshotPowerUp;
import com.example.demo.ui.LevelView;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class LevelThree extends LevelParent {

    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background3.jpg";
    private static final int PLAYER_INITIAL_HEALTH = 5;
    private static final int WAVES_BEFORE_BOSS = 3;
    private static final double POWER_UP_SPAWN_PROBABILITY = 0.02; // 2% chance to spawn power-up each frame

    private LevelView levelView;
    private final Boss levelThreeBoss;
    private int waveCount = 0;
    private boolean bossSpawned = false;
    private final Label shieldAlert; // Label for shield alerts


    // Constructor
    public LevelThree(double screenHeight, double screenWidth, Stage stage) {
        super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH, stage, "Final Level");

        // Initialize the shield alert label
        shieldAlert = createShieldAlert();
        getRoot().getChildren().add(shieldAlert); // Add shield alert to the root node

        // Initialize Boss with the LevelParent reference
        levelThreeBoss = new Boss(this, shieldAlert);
    }

    @Override
    protected void initializeFriendlyUnits() {
        // Add user plane to the scene graph
        getRoot().getChildren().add(getUser());
    }

    @Override
    protected void spawnEnemyUnits() {
        // Regular enemy spawning logic
        if (getCurrentNumberOfEnemies() == 0 && !bossSpawned) {
            waveCount++;
            for (int i = 0; i < 3 + waveCount; i++) {
                double x = getScreenWidth() + 100;
                double y = Math.random() * getEnemyMaximumYPosition();
                addEnemyUnit(new EnemyPlane(x, y));
            }

            if (waveCount >= WAVES_BEFORE_BOSS) {
                spawnBoss();
            }
        }

        // Randomly spawn spreadshot power-up
        if (Math.random() < POWER_UP_SPAWN_PROBABILITY) { // 2% chance per frame
            double screenWidthLimit = getScreenWidth() / 2; // Limit to the left half of the screen
            double x = Math.random() * screenWidthLimit; // Generate an x-coordinate within the left half
            addPowerUp(new SpreadshotPowerUp(x, 0)); // Create SpreadshotPowerUp class if necessary
        }

    }

    @Override
    protected LevelView instantiateLevelView() {
        // Instantiate level view with root and player initial health
        levelView = new LevelView(getRoot(), PLAYER_INITIAL_HEALTH);
        return levelView;
    }

    private void spawnBoss() {
        // Add boss to the scene graph
        addEnemyUnit(levelThreeBoss);
        bossSpawned = true;
    }

    @Override
    protected void checkIfGameOver() {
        if (userIsDestroyed()) {
            loseGame(); // The player loses if their health reaches zero.
        } else if (levelThreeBoss.isDestroyed() && getCurrentNumberOfEnemies() == 0) {
            // The player wins if the boss is destroyed and no other enemies are present.
            winGame();
        }
    }

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
