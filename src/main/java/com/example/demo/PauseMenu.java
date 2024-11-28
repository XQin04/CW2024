package com.example.demo;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
public class PauseMenu extends VBox {

    public PauseMenu(double screenWidth, double screenHeight, Runnable onResume, Runnable onMainMenu) {
        this(screenWidth, screenHeight, onResume, onMainMenu, () -> System.exit(0));
    }

    public PauseMenu(double screenWidth, double screenHeight, Runnable onResume, Runnable onMainMenu, Runnable onExit) {
        // Set the size and position of the Pause Menu
        this.setPrefSize(screenWidth * 0.4, screenHeight * 0.4); // 40% width, 40% height
        this.setLayoutX((screenWidth - this.getPrefWidth()) / 2); // Center horizontally
        this.setLayoutY((screenHeight - this.getPrefHeight()) / 2); // Center vertically
        this.setAlignment(Pos.CENTER); // Center all child elements
        this.setSpacing(20); // Add spacing between buttons
        this.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7); -fx-padding: 20; -fx-border-radius: 20; "
                + "-fx-border-color: white; -fx-background-radius: 20;"); // Styled for transparency and rounded edges
        this.setVisible(false); // Initially hidden

        // Create Buttons with consistent styling
        Button resumeButton = createStyledButton("Resume", "#FF6347", "#FF4500");
        Button mainMenuButton = createStyledButton("Main Menu", "#FFD700", "#FFA500");
        Button exitButton = createStyledButton("Exit", "#A9A9A9", "#808080");

        // Set Button Actions
        resumeButton.setOnAction(e -> {
            onResume.run(); // Call resume logic
            this.setVisible(false); // Hide the pause menu
        });

        mainMenuButton.setOnAction(e -> {
            onMainMenu.run(); // Call main menu logic
            this.setVisible(false); // Hide the pause menu
        });

        exitButton.setOnAction(e -> {
            onExit.run(); // Call exit logic
            this.setVisible(false); // Hide the pause menu
        });

        // Add buttons to the VBox
        this.getChildren().addAll(resumeButton, mainMenuButton, exitButton);
    }

    // Method to style buttons consistently
    private Button createStyledButton(String text, String startColor, String endColor) {
        Button button = new Button(text);
        button.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 20)); // Bold font
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
