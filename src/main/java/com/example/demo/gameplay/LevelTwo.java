package com.example.demo.gameplay;

import com.example.demo.actors.enemies.BossSpider;
import com.example.demo.ui.gameplayUI.LevelView;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Represents the second level of the game.
 *
 * <p>This level introduces the BossSpider, a challenging enemy with shield mechanics.
 * The player must defeat the boss to proceed to the next level.</p>
 */
public class LevelTwo extends LevelParent {

    // Constants for level configuration
    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background2.png"; // Path to background image
    private static final int PLAYER_INITIAL_HEALTH = 5; // Initial health of the player
    private static final String NEXT_LEVEL = "com.example.demo.gameplay.LevelThree"; // Class name of the next level

    private final BossSpider boss; // Reference to the boss enemy

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
        Label shieldAlert = createShieldAlert();
        getRoot().getChildren().add(shieldAlert);

        // Initialize the boss with a reference to this level and the shield alert
        boss = new BossSpider(this, shieldAlert);
    }

    /**
     * Adds the user's superman character to the level.
     * <p>This method is called during level initialization to ensure the player's character
     * is present on the screen.</p>
     */
    @Override
    protected void initializeFriendlyUnits() {
        getRoot().getChildren().add(getUser());
    }

    /**
     * Checks if the game is over or if the player has completed the level.
     *
     * <p>Conditions:
     * - The game ends if the player's health reaches zero.
     * - The level transitions to the next if the boss is defeated.</p>
     */
    @Override
    protected void checkIfGameOver() {
        if (userIsDestroyed()) {
            loseGame(); // Trigger game over logic
        } else if (boss.isDestroyed()) {
            goToNextLevel(NEXT_LEVEL); // Transition to the next level
        }
    }

    /**
     * Spawns enemy units for the level.
     * <p>If no enemies are currently present, the boss is added to the level.</p>
     */
    @Override
    protected void spawnEnemyUnits() {
        if (enemyManager.getEnemyCount() == 0) {
            enemyManager.addEnemy(boss); // Add the boss using EnemyManager
        }
    }

    /**
     * Instantiates the view for this level.
     *
     * @return A LevelView object that includes the player's health display.
     */
    @Override
    protected LevelView instantiateLevelView() {
        return new LevelView(getRoot(), PLAYER_INITIAL_HEALTH);
    }

    /**
     * Creates and configures a Label for displaying shield activation messages.
     *
     * @return A configured Label object with font, color, and layout settings.
     */
    private Label createShieldAlert() {
        Label label = new Label();
        label.setFont(new Font("Arial", 24)); // Set font size and style
        label.setTextFill(Color.RED); // Set text color
        label.setLayoutX(500); // Position the label on the X-axis
        label.setLayoutY(50);  // Position the label on the Y-axis
        label.setVisible(false); // Hide the label initially
        return label;
    }
}
