package com.example.demo.ui.menus;

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

import java.net.URL;

/**
 * Represents the main menu of the game.
 * Provides options to start the game, access settings, view instructions, or exit the application.
 * <p>
 * This class creates a user interface for the main menu, including background music, settings, and
 * navigation to different parts of the game.
 * </p>
 */
public class MainMenu {

    private static final int SCREEN_WIDTH = 1300; // The width of the main menu screen.
    private static final int SCREEN_HEIGHT = 750; // The height of the main menu screen.

    private static final String BACKGROUND_IMAGE_PATH = "/com/example/demo/images/menubackground.png"; // Path to the background image.
    private static final String BACKGROUND_MUSIC_PATH = "/com/example/demo/sounds/Background.mp3"; // Path to the background music.
    private final SoundManager soundManager = SoundManager.getInstance(); // Singleton instance for managing sound.
    private MediaPlayer backgroundMediaPlayer; // MediaPlayer to play background music.

    /**
     * Initializes and displays the main menu.
     *
     * @param stage the primary stage for displaying the menu.
     * @param main  the main game controller.
     */
    public void start(Stage stage, Main main) {
        playBackgroundMusic(); // Start background music for the main menu.
        StackPane root = new StackPane(); // Root container for stacking layouts.

        ImageView backgroundImage = createBackgroundImage(); // Create and set the background image.
        root.getChildren().add(backgroundImage);

        VBox mainMenuLayout = createMainMenuLayout(root, stage, main); // Main menu layout.
        VBox settingsLayout = createSettingsLayout(root); // Settings menu layout.
        VBox howToPlayLayout = createHowToPlayLayout(root); // Instructions layout.

        // Add all layouts to the root container and set their visibility.
        root.getChildren().addAll(mainMenuLayout, settingsLayout, howToPlayLayout);
        settingsLayout.setVisible(false); // Hide settings layout initially.
        howToPlayLayout.setVisible(false); // Hide "How to Play" layout initially.

        // Set up the scene and stage.
        Scene menuScene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);
        stage.setScene(menuScene);
        stage.setTitle("Sky Battle - Main Menu");
        stage.show();
    }

    /**
     * Creates the background image for the main menu.
     *
     * @return the configured ImageView for the background image.
     */
    private ImageView createBackgroundImage() {
        // Load the background image resource.
        URL resource = getClass().getResource(BACKGROUND_IMAGE_PATH);
        if (resource == null) {
            throw new IllegalArgumentException("Resource not found: " + BACKGROUND_IMAGE_PATH);
        }
        Image image = new Image(resource.toExternalForm());
        ImageView imageView = new ImageView(image);

        // Set dimensions and ensure it covers the entire screen.
        imageView.setFitWidth(SCREEN_WIDTH);
        imageView.setFitHeight(SCREEN_HEIGHT);
        imageView.setPreserveRatio(false); // Disable aspect ratio preservation.

        return imageView;
    }

    /**
     * Plays the background music for the main menu.
     */
    private void playBackgroundMusic() {
        // Stop existing music if already playing.
        if (backgroundMediaPlayer != null) {
            backgroundMediaPlayer.stop();
        }

        // Load the background music resource.
        URL resource = getClass().getResource(BACKGROUND_MUSIC_PATH);
        if (resource == null) {
            throw new IllegalArgumentException("Music resource not found: " + BACKGROUND_MUSIC_PATH);
        }

        // Set up and play the MediaPlayer for background music.
        Media media = new Media(resource.toExternalForm());
        backgroundMediaPlayer = new MediaPlayer(media);
        backgroundMediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop the music indefinitely.
        backgroundMediaPlayer.setVolume(soundManager.isMusicMuted() ? 0 : 1); // Adjust volume based on mute state.
        backgroundMediaPlayer.play();
    }

    /**
     * Creates the main menu layout with options to start the game, access settings, view instructions, or exit.
     *
     * @param root  the root StackPane for switching layouts.
     * @param stage the primary stage for the application.
     * @param main  the main game controller.
     * @return the VBox containing the main menu layout.
     */
    private VBox createMainMenuLayout(StackPane root, Stage stage, Main main) {
        // Create a title for the main menu.
        Label title = new Label("Sky Battle");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 50)); // Bold font for the title.
        title.setTextFill(Color.BLACK); // Set title text color.

        // Create buttons for main menu options.
        Button startButton = new Button("START GAME");
        Button settingsButton = new Button("SETTINGS");
        Button howToPlayButton = new Button("HOW TO PLAY");
        Button exitButton = new Button("EXIT");

        // Apply consistent styling to buttons.
        styleButton(startButton, "#FF6347", "#FF4500");
        styleButton(settingsButton, "#FFD700", "#FFA500");
        styleButton(howToPlayButton, "#87CEFA", "#4682B4");
        styleButton(exitButton, "#A9A9A9", "#808080");

        // Define actions for each button.
        startButton.setOnAction(e -> {
            if (backgroundMediaPlayer != null) {
                backgroundMediaPlayer.stop(); // Stop the background music before starting the game.
            }
            main.startGame(stage); // Start the game.
        });
        settingsButton.setOnAction(e -> switchToLayout(root, 1)); // Show settings layout.
        howToPlayButton.setOnAction(e -> switchToLayout(root, 2)); // Show instructions layout.
        exitButton.setOnAction(e -> stage.close()); // Exit the application.

        // Arrange buttons in a vertical box.
        VBox mainMenuLayout = new VBox(20, title, startButton, settingsButton, howToPlayButton, exitButton);
        mainMenuLayout.setAlignment(Pos.CENTER); // Center-align elements.
        mainMenuLayout.setStyle("-fx-padding: 20; -fx-background-color: rgba(255, 255, 255, 0.7);"
                + "-fx-border-color: #FF8C00; -fx-border-radius: 20; -fx-background-radius: 20;");
        mainMenuLayout.setMaxWidth(400); // Set maximum layout width.

        return mainMenuLayout;
    }

    /**
     * Creates the settings layout with options to mute sound effects and background music.
     *
     * @param root the root StackPane for switching layouts.
     * @return the VBox containing the settings layout.
     */
    private VBox createSettingsLayout(StackPane root) {
        // Create a title for the settings layout.
        Label settingsTitle = new Label("Settings");
        settingsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 36)); // Bold font for the title.
        settingsTitle.setTextFill(Color.web("#FF69B4")); // Set title color to pink.

        // Create checkboxes for sound and music mute options.
        CheckBox soundCheckBox = new CheckBox("Mute Sound Effects");
        soundCheckBox.setSelected(soundManager.isSoundEffectsMuted()); // Set initial state based on SoundManager.
        soundCheckBox.setStyle("-fx-font-size: 20px;");
        soundCheckBox.setOnAction(e -> soundManager.setSoundEffectsMuted(soundCheckBox.isSelected()));

        CheckBox musicCheckBox = createMusicCheckBox();

        // Back button to return to the main menu.
        Button backButton = new Button("BACK");
        styleButton(backButton, "#87CEFA", "#4682B4");
        backButton.setOnAction(e -> switchToLayout(root, 0)); // Switch back to main menu.

        // Arrange elements in the settings layout.
        VBox settingsLayout = new VBox(20, settingsTitle, soundCheckBox, musicCheckBox, backButton);
        settingsLayout.setAlignment(Pos.CENTER); // Center-align elements.
        settingsLayout.setStyle("-fx-padding: 20; -fx-background-color: rgba(255, 255, 255, 0.7);"
                + "-fx-border-color: #FF69B4; -fx-border-radius: 15;");
        settingsLayout.setMaxWidth(400); // Set maximum layout width.

        return settingsLayout;
    }

    /**
     * Switches the visibility of layouts within the root StackPane.
     * <p>
     * This method loops through all child nodes in the root StackPane and ensures
     * only the layout corresponding to the given index is visible.
     * </p>
     *
     * @param root        the root StackPane containing the layouts.
     * @param layoutIndex the index of the layout to make visible.
     */
    private void switchToLayout(StackPane root, int layoutIndex) {
        for (int i = 1; i < root.getChildren().size(); i++) {
            root.getChildren().get(i).setVisible(i == layoutIndex + 1); // Show the selected layout
        }
    }

    /**
     * Styles a button with gradient colors and hover effects.
     * <p>
     * This method applies a uniform style to buttons in the menu, using a gradient
     * color scheme and hover effects to improve interactivity.
     * </p>
     *
     * @param button     the button to style.
     * @param startColor the starting color for the gradient.
     * @param endColor   the ending color for the gradient.
     */
    private void styleButton(Button button, String startColor, String endColor) {
        // Set font and text color.
        button.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        button.setTextFill(Color.WHITE);

        // Define button dimensions and style.
        button.setPrefWidth(250);
        button.setStyle("-fx-background-color: linear-gradient(to bottom, " + startColor + ", " + endColor + ");"
                + "-fx-background-radius: 15; -fx-padding: 10 40; -fx-border-radius: 15;");

        // Define hover effects for the button.
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: linear-gradient(to bottom, " + endColor + ", " + startColor + ");"
                + "-fx-background-radius: 15; -fx-padding: 10 40; -fx-border-radius: 15;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: linear-gradient(to bottom, " + startColor + ", " + endColor + ");"
                + "-fx-background-radius: 15; -fx-padding: 10 40; -fx-border-radius: 15;"));
    }

    /**
     * Creates the "How to Play" layout with instructions for the game.
     * <p>
     * This layout provides guidance to players on game objectives, controls, and strategies.
     * It includes an instructional image and descriptive text.
     * </p>
     *
     * @param root the root StackPane for switching layouts.
     * @return a VBox containing the "How to Play" layout.
     */
    private VBox createHowToPlayLayout(StackPane root) {
        // Create a title for the "How to Play" layout.
        Label howToPlayTitle = new Label("How to Play");
        howToPlayTitle.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        howToPlayTitle.setTextFill(Color.web("#FF69B4"));

        // Load the instructional image.
        URL resource = getClass().getResource("/com/example/demo/images/instruction.png");
        if (resource == null) {
            throw new IllegalArgumentException("Resource not found: /com/example/demo/images/instruction.png");
        }

        ImageView instructionImage = new ImageView(new Image(resource.toExternalForm()));
        instructionImage.setFitWidth(350);
        instructionImage.setPreserveRatio(true);

        // Define instructional text.
        Label instructions = new Label("""
                Destroy all enemy spiders to advance.

                Collect power-ups to enhance abilities.

                Defeat the boss to WIN the game!
                """);
        instructions.setFont(Font.font("Arial", 18));
        instructions.setTextFill(Color.DARKBLUE);

        // Create a back button to return to the main menu.
        Button backButton = new Button("BACK");
        styleButton(backButton, "#87CEFA", "#4682B4");
        backButton.setOnAction(e -> switchToLayout(root, 0));

        // Arrange elements in a vertical box.
        VBox howToPlayLayout = new VBox(20, howToPlayTitle, instructionImage, instructions, backButton);
        howToPlayLayout.setAlignment(Pos.CENTER);
        howToPlayLayout.setStyle("-fx-padding: 20; -fx-background-color: rgba(255, 255, 255, 0.7);"
                + "-fx-border-color: #FF69B4; -fx-border-radius: 15;");
        howToPlayLayout.setMaxWidth(400);

        return howToPlayLayout;
    }

    /**
     * Creates a checkbox for toggling background music mute state.
     * <p>
     * This method creates a checkbox that allows the user to mute or unmute the
     * background music in the game. The checkbox reflects the current mute state
     * from the SoundManager.
     * </p>
     *
     * @return a CheckBox for muting or unmuting background music.
     */
    private CheckBox createMusicCheckBox() {
        CheckBox musicCheckBox = new CheckBox("Mute Background Music");
        musicCheckBox.setSelected(soundManager.isMusicMuted()); // Set initial state based on SoundManager.
        musicCheckBox.setStyle("-fx-font-size: 20px;");

        // Define the action to take when the checkbox state changes.
        musicCheckBox.setOnAction(e -> {
            boolean isMuted = musicCheckBox.isSelected();
            soundManager.setMusicMuted(isMuted); // Update the mute state in SoundManager.
            if (backgroundMediaPlayer != null) {
                backgroundMediaPlayer.setVolume(isMuted ? 0 : 1); // Adjust the volume of the background music.
            }
        });

        return musicCheckBox;
    }
}
