package com.example.demo.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

/**
 * Represents the pause menu displayed in the game.
 * Provides options to resume the game, navigate to the main menu, or exit the application.
 */
public class PauseMenu extends VBox {

    /**
     * Constructs a PauseMenu with default exit behavior (exits the application).
     *
     * @param screenWidth  the width of the screen
     * @param screenHeight the height of the screen
     * @param onResume     the action to perform when the "Resume" button is clicked
     * @param onMainMenu   the action to perform when the "Main Menu" button is clicked
     */
    public PauseMenu(double screenWidth, double screenHeight, Runnable onResume, Runnable onMainMenu) {
        this(screenWidth, screenHeight, onResume, onMainMenu, () -> System.exit(0)); // Default exit behavior
    }

    /**
     * Constructs a PauseMenu with customizable exit behavior.
     *
     * @param screenWidth  the width of the screen
     * @param screenHeight the height of the screen
     * @param onResume     the action to perform when the "Resume" button is clicked
     * @param onMainMenu   the action to perform when the "Main Menu" button is clicked
     * @param onExit       the action to perform when the "Exit" button is clicked
     */
    public PauseMenu(double screenWidth, double screenHeight, Runnable onResume, Runnable onMainMenu, Runnable onExit) {
        configureLayout(screenWidth, screenHeight);

        // Create buttons
        Button resumeButton = createStyledButton("Resume", "#FF6347", "#FF4500", onResume);
        Button mainMenuButton = createStyledButton("Main Menu", "#FFD700", "#FFA500", onMainMenu);
        Button exitButton = createStyledButton("Exit", "#A9A9A9", "#808080", onExit);

        // Add buttons to the VBox
        this.getChildren().addAll(resumeButton, mainMenuButton, exitButton);
    }

    /**
     * Configures the layout of the pause menu.
     *
     * @param screenWidth  the width of the screen
     * @param screenHeight the height of the screen
     */
    private void configureLayout(double screenWidth, double screenHeight) {
        this.setPrefSize(screenWidth * 0.4, screenHeight * 0.4); // 40% width, 40% height
        this.setLayoutX((screenWidth - this.getPrefWidth()) / 2); // Center horizontally
        this.setLayoutY((screenHeight - this.getPrefHeight()) / 2); // Center vertically
        this.setAlignment(Pos.CENTER); // Center all child elements
        this.setSpacing(20); // Add spacing between buttons
        this.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7); -fx-padding: 20; -fx-border-radius: 20; "
                + "-fx-border-color: white; -fx-background-radius: 20;"); // Styled for transparency and rounded edges
        this.setVisible(false); // Initially hidden
    }

    /**
     * Creates a styled button with the specified text, gradient colors, and action.
     *
     * @param text       the text displayed on the button
     * @param startColor the starting gradient color of the button
     * @param endColor   the ending gradient color of the button
     * @param action     the action to perform when the button is clicked
     * @return a Button object with consistent styling
     */
    private Button createStyledButton(String text, String startColor, String endColor, Runnable action) {
        Button button = new Button(text);
        button.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 20)); // Bold font
        button.setTextFill(javafx.scene.paint.Color.WHITE); // Text color
        button.setPrefWidth(250); // Fixed button width
        button.setStyle("-fx-background-color: linear-gradient(to bottom, " + startColor + ", " + endColor + ");"
                + "-fx-background-radius: 15; -fx-padding: 10 40; -fx-border-radius: 15;");

        // Set action
        button.setOnAction(e -> {
            action.run(); // Execute provided action
            this.setVisible(false); // Hide the menu
        });

        // Change appearance on hover
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: linear-gradient(to bottom, " + endColor + ", " + startColor + ");"
                + "-fx-background-radius: 15; -fx-padding: 10 40; -fx-border-radius: 15;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: linear-gradient(to bottom, " + startColor + ", " + endColor + ");"
                + "-fx-background-radius: 15; -fx-padding: 10 40; -fx-border-radius: 15;"));

        return button;
    }
}
