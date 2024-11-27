package com.example.demo;

import javafx.scene.layout.VBox;
import javafx.scene.control.Button;

public class PauseMenu extends VBox {

    public PauseMenu(double screenWidth, double screenHeight, Runnable onResume, Runnable onRestart, Runnable onMainMenu) {
        this(screenWidth, screenHeight, onResume, onRestart, onMainMenu, () -> System.exit(0));
    }

    public PauseMenu(double screenWidth, double screenHeight, Runnable onResume, Runnable onRestart, Runnable onMainMenu, Runnable onExit) {
        this.setPrefSize(screenWidth * 0.6, screenHeight * 0.4); // Set the menu to 60% width and 40% height of the screen
        this.setLayoutX((screenWidth - this.getPrefWidth()) / 2); // Center horizontally
        this.setLayoutY((screenHeight - this.getPrefHeight()) / 2); // Center vertically
        this.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8); -fx-padding: 20; -fx-alignment: center; -fx-border-radius: 10;");
        this.setVisible(false); // Initially hidden

        // Resume Button
        Button resumeButton = new Button("Resume");
        resumeButton.setOnAction(e -> {
            onResume.run(); // Call resume logic
            this.setVisible(false); // Hide the pause menu
        });

        // Restart Button
        Button restartButton = new Button("Restart");
        restartButton.setOnAction(e -> {
            onRestart.run(); // Call restart logic
            this.setVisible(false); // Hide the pause menu
        });

        // Main Menu Button
        Button mainMenuButton = new Button("Main Menu");
        mainMenuButton.setOnAction(e -> {
            onMainMenu.run(); // Call main menu logic
            this.setVisible(false); // Hide the pause menu
        });

        // Exit Button
        Button exitButton = new Button("Exit");
        exitButton.setOnAction(e -> {
            onExit.run(); // Call exit logic
            this.setVisible(false); // Hide the pause menu
        });

        this.getChildren().addAll(resumeButton, restartButton, mainMenuButton, exitButton);
    }
}
