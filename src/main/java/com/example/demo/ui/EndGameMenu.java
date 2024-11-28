package com.example.demo.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class EndGameMenu extends VBox {

    public EndGameMenu(double screenWidth, double screenHeight, Runnable onMainMenu, Runnable onExit) {
        // Set the size and position of the End Game Menu
        this.setPrefSize(screenWidth * 0.4, screenHeight * 0.4); // 40% width, 40% height
        this.setLayoutX((screenWidth - this.getPrefWidth()) / 2); // Center horizontally
        this.setLayoutY((screenHeight - this.getPrefHeight()) / 2); // Center vertically
        this.setAlignment(Pos.CENTER); // Center all child elements
        this.setSpacing(20); // Add spacing between buttons
        this.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7); -fx-padding: 20; -fx-border-radius: 20; "
                + "-fx-border-color: white; -fx-background-radius: 20;"); // Styled for transparency and rounded edges
        this.setVisible(false); // Initially hidden

        // Title Text
        Text resultMessage = new Text("Game Over");
        resultMessage.setFont(Font.font("Arial", FontWeight.BOLD, 30)); // Bold and large text
        resultMessage.setStyle("-fx-fill: white;"); // White color for visibility

        // Create Buttons with consistent styling
        Button mainMenuButton = createStyledButton("Main Menu", "#FFD700", "#FFA500");
        Button exitButton = createStyledButton("Exit", "#FF6347", "#FF4500");

        // Set Button Actions
        mainMenuButton.setOnAction(e -> {
            onMainMenu.run(); // Call main menu logic
            this.setVisible(false); // Hide the endgame menu
        });

        exitButton.setOnAction(e -> {
            onExit.run(); // Call exit logic
            this.setVisible(false); // Hide the endgame menu
        });

        // Add components to the VBox
        this.getChildren().addAll(resultMessage, mainMenuButton, exitButton);
    }

    // Method to update the title dynamically and show the menu
    public void show(boolean isWin) {
        Text resultMessage = (Text) this.getChildren().get(0);
        resultMessage.setText(isWin ? "You Win!" : "Game Over"); // Update title based on result
        this.setVisible(true); // Show the menu
    }

    // Method to style buttons consistently
    private Button createStyledButton(String text, String startColor, String endColor) {
        Button button = new Button(text);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 20)); // Bold font
        button.setTextFill(javafx.scene.paint.Color.WHITE); // Text color
        button.setPrefWidth(250); // Fixed button width
        button.setStyle("-fx-background-color: linear-gradient(to bottom, " + startColor + ", " + endColor + ");"
                + "-fx-background-radius: 15; -fx-padding: 10 40; -fx-border-radius: 15;");

        // Change appearance on hover
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: linear-gradient(to bottom, " + endColor + ", " + startColor + ");"
                + "-fx-background-radius: 15; -fx-padding: 10 40; -fx-border-radius: 15;"));

        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: linear-gradient(to bottom, " + startColor + ", " + endColor + ");"
                + "-fx-background-radius: 15; -fx-padding: 10 40; -fx-border-radius: 15;"));

        return button;
    }
}
