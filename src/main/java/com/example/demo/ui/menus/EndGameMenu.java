package com.example.demo.ui.menus;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Represents the end game menu displayed when the game ends.
 * Includes options to return to the main menu or exit the game.
 */
public class EndGameMenu extends VBox {

    private final Text resultMessage;

    /**
     * Constructs the EndGameMenu with specified dimensions and button actions.
     *
     * @param screenWidth  the width of the screen
     * @param screenHeight the height of the screen
     * @param onMainMenu   the action to execute when the "Main Menu" button is clicked
     * @param onExit       the action to execute when the "Exit" button is clicked
     */
    public EndGameMenu(double screenWidth, double screenHeight, Runnable onMainMenu, Runnable onExit) {
        // Set up End Game Menu layout
        configureMenuLayout(screenWidth, screenHeight);

        // Create Title Text
        resultMessage = createTitleText();

        // Create Buttons
        Button mainMenuButton = createStyledButton("Main Menu", "#FFD700", "#FFA500", onMainMenu);
        Button exitButton = createStyledButton("Exit", "#FF6347", "#FF4500", onExit);

        // Add components to the VBox
        this.getChildren().addAll(resultMessage, mainMenuButton, exitButton);
    }

    /**
     * Configures the layout properties of the menu.
     *
     * @param screenWidth  the width of the screen
     * @param screenHeight the height of the screen
     */
    private void configureMenuLayout(double screenWidth, double screenHeight) {
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
     * Creates the title text for the menu.
     *
     * @return a Text object representing the title
     */
    private Text createTitleText() {
        Text title = new Text("Game Over");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 30)); // Bold and large text
        title.setStyle("-fx-fill: white;"); // White color for visibility
        return title;
    }

    /**
     * Creates a styled button with specified colors and action.
     *
     * @param text       the text displayed on the button
     * @param startColor the starting gradient color of the button
     * @param endColor   the ending gradient color of the button
     * @param action     the action to execute when the button is clicked
     * @return a Button object with the specified properties
     */
    private Button createStyledButton(String text, String startColor, String endColor, Runnable action) {
        Button button = new Button(text);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 20)); // Bold font
        button.setTextFill(javafx.scene.paint.Color.WHITE); // Text color
        button.setPrefWidth(250); // Fixed button width
        applyButtonStyle(button, startColor, endColor);

        // Attach action
        button.setOnAction(e -> {
            action.run(); // Execute the provided logic
            this.setVisible(false); // Hide the menu
        });

        return button;
    }

    /**
     * Applies consistent styling to a button.
     *
     * @param button     the button to style
     * @param startColor the starting gradient color
     * @param endColor   the ending gradient color
     */
    private void applyButtonStyle(Button button, String startColor, String endColor) {
        String defaultStyle = "-fx-background-color: linear-gradient(to bottom, " + startColor + ", " + endColor + ");"
                + "-fx-background-radius: 15; -fx-padding: 10 40; -fx-border-radius: 15;";
        button.setStyle(defaultStyle);

        // Change appearance on hover
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: linear-gradient(to bottom, " + endColor + ", " + startColor + ");"
                + "-fx-background-radius: 15; -fx-padding: 10 40; -fx-border-radius: 15;"));
        button.setOnMouseExited(e -> button.setStyle(defaultStyle));
    }

    /**
     * Updates the menu title dynamically and makes it visible.
     *
     * @param isWin true if the player won, false if the game is over
     */
    public void show(boolean isWin) {
        resultMessage.setText(isWin ? "You Win!" : "Game Over"); // Update title based on result
        this.setVisible(true); // Show the menu
    }
}
