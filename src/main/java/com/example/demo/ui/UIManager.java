package com.example.demo.ui;

import com.example.demo.gameplay.levels.LevelParent;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Manages UI elements like menus and buttons for the game level.
 */
public class UIManager {
    private static UIManager instance; // Singleton instance

    private Group menuLayer;
    private LevelParent levelParent;
    private PauseMenu pauseMenu;
    private EndGameMenu endGameMenu;
    private Button pauseButton;

    /**
     * Private constructor to enforce the Singleton pattern.
     */
    private UIManager(LevelParent levelParent, Group menuLayer, double screenWidth, double screenHeight, Stage stage) {
        this.levelParent = levelParent;
        this.menuLayer = menuLayer;

        // Initialize menus
        this.pauseMenu = new PauseMenu(
                screenWidth,
                screenHeight,
                levelParent::resumeGame,
                () -> levelParent.goToMainMenu(stage)
        );

        this.endGameMenu = new EndGameMenu(
                screenWidth,
                screenHeight,
                () -> levelParent.goToMainMenu(stage),
                () -> System.exit(0)
        );

        // Initialize the pause button
        this.pauseButton = createPauseButton(screenWidth);
    }

    /**
     * Provides access to the Singleton instance of UIManager.
     *
     * @param levelParent  The LevelParent instance.
     * @param menuLayer    The menu layer.
     * @param screenWidth  The screen width.
     * @param screenHeight The screen height.
     * @param stage        The game stage.
     * @return The Singleton instance of UIManager.
     */
    public static UIManager getInstance(LevelParent levelParent, Group menuLayer, double screenWidth, double screenHeight, Stage stage) {
        if (instance == null) {
            instance = new UIManager(levelParent, menuLayer, screenWidth, screenHeight, stage);
        }
        return instance;
    }

    /**
     * Initializes the UI elements by adding them to the appropriate layers.
     */
    public void initializeUI() {
        levelParent.getRoot().getChildren().remove(pauseButton); // Remove existing button if present
        menuLayer.getChildren().clear(); // Clear to avoid duplicates

        // Add menus and buttons
        menuLayer.getChildren().addAll(pauseMenu, endGameMenu);
        levelParent.getRoot().getChildren().add(pauseButton);

        // Ensure pause button is visible
        pauseButton.setVisible(true);
    }

    /**
     * Resets the UIManager instance to handle level transitions or restarts.
     *
     * @param levelParent  The LevelParent instance.
     * @param menuLayer    The menu layer.
     * @param screenWidth  The screen width.
     * @param screenHeight The screen height.
     * @param stage        The game stage.
     */
    public void reset(LevelParent levelParent, Group menuLayer, double screenWidth, double screenHeight, Stage stage) {
        this.levelParent = levelParent;
        this.menuLayer = menuLayer;

        // Reinitialize menus
        this.pauseMenu = new PauseMenu(
                screenWidth,
                screenHeight,
                levelParent::resumeGame,
                () -> levelParent.goToMainMenu(stage)
        );

        this.endGameMenu = new EndGameMenu(
                screenWidth,
                screenHeight,
                () -> levelParent.goToMainMenu(stage),
                () -> System.exit(0)
        );

        // Reinitialize pause button
        this.pauseButton = createPauseButton(screenWidth);

        // Reinitialize UI elements
        initializeUI();
    }

    /**
     * Retrieves the PauseMenu instance.
     *
     * @return The PauseMenu instance.
     */
    public PauseMenu getPauseMenu() {
        return pauseMenu;
    }

    /**
     * Retrieves the EndGameMenu instance.
     *
     * @return The EndGameMenu instance.
     */
    public EndGameMenu getEndGameMenu() {
        return endGameMenu;
    }

    /**
     * Retrieves the Pause button.
     *
     * @return The Pause button.
     */
    public Button getPauseButton() {
        return pauseButton;
    }

    /**
     * Creates and configures the Pause button.
     *
     * @param screenWidth The width of the screen for positioning.
     * @return The Pause button.
     */
    private Button createPauseButton(double screenWidth) {
        Button button = new Button("Pause");
        button.setFont(javafx.scene.text.Font.font("Arial", 14));
        button.setStyle("-fx-background-color: orange; -fx-text-fill: white; -fx-background-radius: 10;");
        button.setLayoutX(screenWidth - 100);
        button.setLayoutY(20);
        button.setOnAction(e -> levelParent.togglePause());
        return button;
    }

    /**
     * Resets the Singleton instance.
     */
    public static void resetInstance() {
        instance = null;
    }
}
