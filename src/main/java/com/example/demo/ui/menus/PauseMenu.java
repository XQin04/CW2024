package com.example.demo.ui.menus;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

/**
 * Represents the pause menu displayed in the game.
 * <p>
 * The pause menu allows players to:
 * <ul>
 *     <li>Resume the game</li>
 *     <li>Navigate to the main menu</li>
 *     <li>Exit the application</li>
 * </ul>
 * </p>
 */
public class PauseMenu extends VBox {

    /**
     * Constructs a PauseMenu with default exit behavior (exits the application).
     *
     * @param screenWidth  The width of the screen.
     * @param screenHeight The height of the screen.
     * @param onResume     The action to perform when the "Resume" button is clicked.
     * @param onMainMenu   The action to perform when the "Main Menu" button is clicked.
     */
    public PauseMenu(double screenWidth, double screenHeight, Runnable onResume, Runnable onMainMenu) {
        this(screenWidth, screenHeight, onResume, onMainMenu, () -> System.exit(0)); // Default behavior exits the app
    }

    /**
     * Constructs a PauseMenu with customizable exit behavior.
     *
     * @param screenWidth  The width of the screen.
     * @param screenHeight The height of the screen.
     * @param onResume     The action to perform when the "Resume" button is clicked.
     * @param onMainMenu   The action to perform when the "Main Menu" button is clicked.
     * @param onExit       The action to perform when the "Exit" button is clicked.
     */
    public PauseMenu(double screenWidth, double screenHeight, Runnable onResume, Runnable onMainMenu, Runnable onExit) {
        configureLayout(screenWidth, screenHeight);

        // Create buttons with consistent styling and actions
        Button resumeButton = createStyledButton("Resume", "#FF6347", "#FF4500", onResume);
        Button mainMenuButton = createStyledButton("Main Menu", "#FFD700", "#FFA500", onMainMenu);
        Button exitButton = createStyledButton("Exit", "#A9A9A9", "#808080", onExit);

        // Add buttons to the VBox layout
        this.getChildren().addAll(resumeButton, mainMenuButton, exitButton);
    }

    /**
     * Configures the layout of the pause menu.
     * <p>
     * Centers the menu on the screen, adds spacing between buttons, and applies rounded corners
     * and a semi-transparent background for a modern UI design.
     * </p>
     *
     * @param screenWidth  The width of the screen.
     * @param screenHeight The height of the screen.
     */
    private void configureLayout(double screenWidth, double screenHeight) {
        this.setPrefSize(screenWidth * 0.4, screenHeight * 0.4); // 40% width, 40% height
        this.setLayoutX((screenWidth - this.getPrefWidth()) / 2); // Center horizontally
        this.setLayoutY((screenHeight - this.getPrefHeight()) / 2); // Center vertically
        this.setAlignment(Pos.CENTER); // Align child elements in the center
        this.setSpacing(20); // Spacing between buttons
        this.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7); -fx-padding: 20; -fx-border-radius: 20; "
                + "-fx-border-color: white; -fx-background-radius: 20;"); // Style for a transparent, rounded design
        this.setVisible(false); // Initially hidden until activated
    }

    /**
     * Creates a styled button with gradient colors and a specified action.
     * <p>
     * Adds hover effects to reverse the gradient and a click action to hide the menu.
     * </p>
     *
     * @param text       The text displayed on the button.
     * @param startColor The starting gradient color of the button.
     * @param endColor   The ending gradient color of the button.
     * @param action     The action to perform when the button is clicked.
     * @return A fully styled Button instance.
     */
    private Button createStyledButton(String text, String startColor, String endColor, Runnable action) {
        Button button = new Button(text);

        // Set button font and text color
        button.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 20)); // Bold font
        button.setTextFill(javafx.scene.paint.Color.WHITE); // White text color

        // Set button size and style
        button.setPrefWidth(250); // Fixed button width
        button.setStyle("-fx-background-color: linear-gradient(to bottom, " + startColor + ", " + endColor + ");"
                + "-fx-background-radius: 15; -fx-padding: 10 40; -fx-border-radius: 15;");

        // Set button action
        button.setOnAction(e -> {
            action.run(); // Execute the provided action
            this.setVisible(false); // Hide the pause menu after the button is clicked
        });

        // Add hover effect to reverse gradient
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: linear-gradient(to bottom, " + endColor + ", " + startColor + ");"
                + "-fx-background-radius: 15; -fx-padding: 10 40; -fx-border-radius: 15;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: linear-gradient(to bottom, " + startColor + ", " + endColor + ");"
                + "-fx-background-radius: 15; -fx-padding: 10 40; -fx-border-radius: 15;"));

        return button;
    }
}
