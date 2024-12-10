package com.example.demo.ui;

import com.example.demo.controller.Main;
import com.example.demo.managers.SoundManager;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * Represents the main menu of the game.
 * Provides options to start the game, access settings, view instructions, or exit the application.
 */
public class MainMenu {

    private static final int SCREEN_WIDTH = 1300; // Width of the main menu screen
    private static final int SCREEN_HEIGHT = 750; // Height of the main menu screen

    private MediaPlayer backgroundMediaPlayer; // MediaPlayer for playing background music
    private final SoundManager soundManager = SoundManager.getInstance(); // Singleton instance for managing sound settings

    /**
     * Initializes and displays the main menu.
     *
     * @param stage the primary stage for displaying the menu
     * @param main  the main game controller
     */
    public void start(Stage stage, Main main) {
        // Play the background music for the main menu
        playBackgroundMusic("/com/example/demo/sounds/Background.mp3");

        // Root layout for the menu
        StackPane root = new StackPane();

        // Add the background image to the root
        ImageView backgroundImage = createBackgroundImage("/com/example/demo/images/menubackground.png");
        root.getChildren().add(backgroundImage);

        // Create individual layouts for main menu, settings, and instructions
        VBox mainMenuLayout = createMainMenuLayout(root, stage, main);
        VBox settingsLayout = createSettingsLayout(root);
        VBox howToPlayLayout = createHowToPlayLayout(root);

        // Add all layouts to the root StackPane
        root.getChildren().addAll(mainMenuLayout, settingsLayout, howToPlayLayout);

        // Set visibility: initially, only the main menu layout is visible
        settingsLayout.setVisible(false);
        howToPlayLayout.setVisible(false);

        // Set up the scene and stage
        Scene menuScene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);
        stage.setScene(menuScene);
        stage.setTitle("Sky Battle - Main Menu");
        stage.show();
    }

    /**
     * Creates the background image for the main menu.
     *
     * @param filePath the file path of the background image
     * @return the configured ImageView for the background image
     */
    private ImageView createBackgroundImage(String filePath) {
        // Load the image from the specified file path
        Image image = new Image(getClass().getResource(filePath).toExternalForm());
        ImageView imageView = new ImageView(image);

        // Set the dimensions of the image to fit the screen
        imageView.setFitWidth(SCREEN_WIDTH);
        imageView.setFitHeight(SCREEN_HEIGHT);
        imageView.setPreserveRatio(false); // Disable maintaining aspect ratio

        return imageView;
    }

    /**
     * Plays the background music for the main menu.
     *
     * @param filePath the file path of the background music file
     */
    private void playBackgroundMusic(String filePath) {
        // Stop the current background music if it is already playing
        if (backgroundMediaPlayer != null) {
            backgroundMediaPlayer.stop();
        }

        // Load and play the new background music
        Media media = new Media(getClass().getResource(filePath).toExternalForm());
        backgroundMediaPlayer = new MediaPlayer(media);
        backgroundMediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop the music
        backgroundMediaPlayer.setVolume(soundManager.isMusicMuted() ? 0 : 1); // Set volume based on mute state
        backgroundMediaPlayer.play();
    }

    /**
     * Creates the main menu layout with options to start the game, access settings, view instructions, or exit.
     *
     * @param root  the root StackPane for switching layouts
     * @param stage the primary stage for the application
     * @param main  the main game controller
     * @return the VBox containing the main menu layout
     */
    private VBox createMainMenuLayout(StackPane root, Stage stage, Main main) {
        // Title of the main menu
        Label title = new Label("Sky Battle");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 50)); // Bold and large font
        title.setTextFill(Color.BLACK); // Black text color

        // Buttons for main menu options
        Button startButton = new Button("START GAME");
        Button settingsButton = new Button("SETTINGS");
        Button howToPlayButton = new Button("HOW TO PLAY");
        Button exitButton = new Button("EXIT");

        // Apply consistent styling to the buttons
        styleButton(startButton, "#FF6347", "#FF4500");
        styleButton(settingsButton, "#FFD700", "#FFA500");
        styleButton(howToPlayButton, "#87CEFA", "#4682B4");
        styleButton(exitButton, "#A9A9A9", "#808080");

        // Define actions for the buttons
        startButton.setOnAction(e -> {
            if (backgroundMediaPlayer != null) {
                backgroundMediaPlayer.stop(); // Stop the music before starting the game
            }
            main.startGame(stage); // Start the game
        });
        settingsButton.setOnAction(e -> switchToLayout(root, 1)); // Show settings menu
        howToPlayButton.setOnAction(e -> switchToLayout(root, 2)); // Show instructions menu
        exitButton.setOnAction(e -> stage.close()); // Close the application

        // Arrange the buttons vertically in the main menu layout
        VBox mainMenuLayout = new VBox(20, title, startButton, settingsButton, howToPlayButton, exitButton);
        mainMenuLayout.setAlignment(Pos.CENTER); // Center-align the elements
        mainMenuLayout.setStyle("-fx-padding: 20; -fx-background-color: rgba(255, 255, 255, 0.7);"
                + "-fx-border-color: #FF8C00; -fx-border-radius: 20; -fx-background-radius: 20;");
        mainMenuLayout.setMaxWidth(400); // Set maximum width for the layout

        return mainMenuLayout;
    }

    /**
     * Creates the settings layout with options to mute sound effects and background music.
     *
     * @param root the root StackPane for switching layouts
     * @return the VBox containing the settings layout
     */
    private VBox createSettingsLayout(StackPane root) {
        // Title for the settings menu
        Label settingsTitle = new Label("Settings");
        settingsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 36)); // Bold and large font
        settingsTitle.setTextFill(Color.web("#FF69B4")); // Pink text color

        // Checkboxes for toggling sound effects and music
        CheckBox soundCheckBox = new CheckBox("Mute Sound Effects");
        soundCheckBox.setSelected(soundManager.isSoundEffectsMuted()); // Initial state based on SoundManager
        soundCheckBox.setStyle("-fx-font-size: 20px;");
        soundCheckBox.setOnAction(e -> soundManager.setSoundEffectsMuted(soundCheckBox.isSelected()));

        CheckBox musicCheckBox = new CheckBox("Mute Background Music");
        musicCheckBox.setSelected(soundManager.isMusicMuted()); // Initial state based on SoundManager
        musicCheckBox.setStyle("-fx-font-size: 20px;");
        musicCheckBox.setOnAction(e -> {
            boolean isMuted = musicCheckBox.isSelected();
            soundManager.setMusicMuted(isMuted); // Update mute state in SoundManager
            if (backgroundMediaPlayer != null) {
                backgroundMediaPlayer.setVolume(isMuted ? 0 : 1); // Adjust music volume
            }
        });

        // Back button to return to the main menu
        Button backButton = new Button("BACK");
        styleButton(backButton, "#87CEFA", "#4682B4");
        backButton.setOnAction(e -> switchToLayout(root, 0)); // Switch back to the main menu

        // Arrange elements in the settings layout
        VBox settingsLayout = new VBox(20, settingsTitle, soundCheckBox, musicCheckBox, backButton);
        settingsLayout.setAlignment(Pos.CENTER); // Center-align the elements
        settingsLayout.setStyle("-fx-padding: 20; -fx-background-color: rgba(255, 255, 255, 0.7);"
                + "-fx-border-color: #FF69B4; -fx-border-radius: 15;");
        settingsLayout.setMaxWidth(400); // Set maximum width for the layout

        return settingsLayout;
    }

    /**
     * Switches the visibility of layouts within the root StackPane.
     *
     * @param root        the root StackPane
     * @param layoutIndex the index of the layout to make visible
     */
    private void switchToLayout(StackPane root, int layoutIndex) {
        for (int i = 1; i < root.getChildren().size(); i++) {
            root.getChildren().get(i).setVisible(i == layoutIndex + 1); // Show the selected layout
        }
    }

    /**
     * Styles a button with gradient colors and hover effects.
     *
     * @param button     the button to style
     * @param startColor the starting gradient color
     * @param endColor   the ending gradient color
     */
    private void styleButton(Button button, String startColor, String endColor) {
        button.setFont(Font.font("Arial", FontWeight.BOLD, 20)); // Bold font
        button.setTextFill(Color.WHITE); // White text color
        button.setPrefWidth(250); // Fixed button width
        button.setStyle("-fx-background-color: linear-gradient(to bottom, " + startColor + ", " + endColor + ");"
                + "-fx-background-radius: 15; -fx-padding: 10 40; -fx-border-radius: 15;");

        // Change button style on hover
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: linear-gradient(to bottom, " + endColor + ", " + startColor + ");"
                + "-fx-background-radius: 15; -fx-padding: 10 40; -fx-border-radius: 15;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: linear-gradient(to bottom, " + startColor + ", " + endColor + ");"
                + "-fx-background-radius: 15; -fx-padding: 10 40; -fx-border-radius: 15;"));
    }

    /**
     * Creates the "How to Play" layout with instructions for playing the game.
     *
     * @param root the root StackPane for switching layouts
     * @return the VBox containing the "How to Play" layout
     */
    private VBox createHowToPlayLayout(StackPane root) {
        // Title for the instructions menu
        Label howToPlayTitle = new Label("How to Play");
        howToPlayTitle.setFont(Font.font("Arial", FontWeight.BOLD, 36)); // Bold and large font
        howToPlayTitle.setTextFill(Color.web("#FF69B4")); // Pink text color

        // Image for instructions
        ImageView instructionImage = new ImageView(new Image(getClass().getResource("/com/example/demo/images/instruction.png").toExternalForm()));
        instructionImage.setFitWidth(350); // Width of the instruction image
        instructionImage.setPreserveRatio(true); // Maintain aspect ratio

        // Instruction text
        Label instructions = new Label(
                "Destroy all enemy spiders to advance.\n\n" +
                        "Collect power-ups to enhance abilities.\n\n" +
                        "Defeat the boss to WIN the game!\n"
        );
        instructions.setFont(Font.font("Arial", 18)); // Font size for the instructions
        instructions.setTextFill(Color.DARKBLUE); // Dark blue text color

        // Back button to return to the main menu
        Button backButton = new Button("BACK");
        styleButton(backButton, "#87CEFA", "#4682B4");
        backButton.setOnAction(e -> switchToLayout(root, 0)); // Switch back to the main menu

        // Arrange elements in the "How to Play" layout
        VBox howToPlayLayout = new VBox(20, howToPlayTitle, instructionImage, instructions, backButton);
        howToPlayLayout.setAlignment(Pos.CENTER); // Center-align the elements
        howToPlayLayout.setStyle("-fx-padding: 20; -fx-background-color: rgba(255, 255, 255, 0.7);"
                + "-fx-border-color: #FF69B4; -fx-border-radius: 15;");
        howToPlayLayout.setMaxWidth(400); // Set maximum width for the layout

        return howToPlayLayout;
    }

}
