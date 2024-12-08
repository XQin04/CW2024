package com.example.demo.ui;

import com.example.demo.gameplay.levels.LevelParent;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Manages UI elements like menus and buttons for the game level.
 */
public class UIManager {
    private final Group menuLayer;
    private final LevelParent levelParent;
    private final PauseMenu pauseMenu;
    private final EndGameMenu endGameMenu;
    private final Button pauseButton;

    public UIManager(LevelParent levelParent, Group menuLayer, double screenWidth, double screenHeight, Stage stage) {
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
     * Adds the menus and buttons to the menu layer.
     */
    public void initializeUI() {
        menuLayer.getChildren().addAll(pauseMenu, endGameMenu);
        levelParent.getRoot().getChildren().add(pauseButton);
    }

    /**
     * Retrieves the pause menu.
     *
     * @return The pause menu.
     */
    public PauseMenu getPauseMenu() {
        return pauseMenu;
    }

    /**
     * Retrieves the end game menu.
     *
     * @return The end game menu.
     */
    public EndGameMenu getEndGameMenu() {
        return endGameMenu;
    }

    /**
     * Retrieves the pause button.
     *
     * @return The pause button.
     */
    public Button getPauseButton() {
        return pauseButton;
    }

    /**
     * Creates and configures the pause button.
     *
     * @param screenWidth The width of the screen for positioning.
     * @return The pause button.
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
}
