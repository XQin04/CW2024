package com.example.demo;

import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class EndGameMenu extends VBox {

    public EndGameMenu(double screenWidth, double screenHeight, Runnable onMainMenu) {
        // Adjust menu size and styling
        this.setPrefSize(screenWidth * 0.6, screenHeight * 0.4); // Set the menu to 60% width and 40% height of the screen
        this.setLayoutX((screenWidth - this.getPrefWidth()) / 2); // Center horizontally
        this.setLayoutY((screenHeight - this.getPrefHeight()) / 2); // Center vertically
        this.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8); -fx-padding: 20; -fx-alignment: center; -fx-border-radius: 10;");

        this.setVisible(false); // Initially hidden

        // Result Message
        Text resultMessage = new Text("Game Over");
        resultMessage.setStyle("-fx-font-size: 28px; -fx-fill: white; -fx-font-weight: bold;");


        // Main Menu Button
        Button mainMenuButton = new Button("Main Menu");
        mainMenuButton.setStyle("-fx-font-size: 16px; -fx-background-color: orange; -fx-text-fill: white; -fx-border-radius: 5;");
        mainMenuButton.setOnAction(e -> {
            onMainMenu.run(); // Call main menu logic
            this.setVisible(false); // Hide the endgame menu
        });

        // Exit Button
        Button exitButton = new Button("Exit");
        exitButton.setStyle("-fx-font-size: 16px; -fx-background-color: red; -fx-text-fill: white; -fx-border-radius: 5;");
        exitButton.setOnAction(e -> {
            System.exit(0); // Exit the application
        });

        // Add spacing between elements
        this.setSpacing(20);

        // Add components to the menu
        this.getChildren().addAll(resultMessage, mainMenuButton, exitButton);
    }

    public void show(boolean isWin) {
        Text resultMessage = (Text) this.getChildren().get(0);
        resultMessage.setText(isWin ? "You Win!" : "Game Over");
        this.setVisible(true);
    }
}
