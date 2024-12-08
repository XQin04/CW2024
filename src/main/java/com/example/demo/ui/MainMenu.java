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
 * Provides options to start the game, adjust settings, view instructions, or exit.
 */
public class MainMenu {

    private static final int SCREEN_WIDTH = 1300;
    private static final int SCREEN_HEIGHT = 750;

    // MediaPlayer for background music
    private MediaPlayer backgroundMediaPlayer;

    // SoundManager instance for managing sound settings
    private final SoundManager soundManager = SoundManager.getInstance();

    /**
     * Starts the main menu.
     *
     * @param stage the primary stage for displaying the menu
     * @param main  the main game controller
     */
    public void start(Stage stage, Main main) {
        // Play background music
        playBackgroundMusic("/com/example/demo/sounds/Background.mp3");

        // Root StackPane to hold all menu layouts
        StackPane root = new StackPane();

        // Add background image
        ImageView backgroundImage = createBackgroundImage("/com/example/demo/images/menubackground.png");
        root.getChildren().add(backgroundImage);

        // Create layouts
        VBox mainMenuLayout = createMainMenuLayout(root, stage, main);
        VBox settingsLayout = createSettingsLayout(root);
        VBox howToPlayLayout = createHowToPlayLayout(root);

        // Add all layouts to the root StackPane
        root.getChildren().addAll(mainMenuLayout, settingsLayout, howToPlayLayout);

        // Initially, only the main menu is visible
        settingsLayout.setVisible(false);
        howToPlayLayout.setVisible(false);

        // Set up the scene
        Scene menuScene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);

        // Stage setup
        stage.setScene(menuScene);
        stage.setTitle("Sky Battle - Main Menu");
        stage.show();
    }

    /**
     * Creates the background image for the main menu.
     *
     * @param filePath the file path of the background image
     * @return the configured ImageView
     */
    private ImageView createBackgroundImage(String filePath) {
        Image image = new Image(getClass().getResource(filePath).toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(SCREEN_WIDTH);
        imageView.setFitHeight(SCREEN_HEIGHT);
        imageView.setPreserveRatio(false);
        return imageView;
    }

    /**
     * Plays the background music for the main menu.
     *
     * @param filePath the file path of the background music file
     */
    private void playBackgroundMusic(String filePath) {
        if (backgroundMediaPlayer != null) {
            backgroundMediaPlayer.stop();
        }
        Media media = new Media(getClass().getResource(filePath).toExternalForm());
        backgroundMediaPlayer = new MediaPlayer(media);
        backgroundMediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        backgroundMediaPlayer.setVolume(soundManager.isMusicMuted() ? 0 : 1);
        backgroundMediaPlayer.play();
    }

    /**
     * Creates the main menu layout with buttons for different options.
     *
     * @param root  the root StackPane for switching layouts
     * @param stage the primary stage for the application
     * @param main  the main game controller
     * @return the VBox containing the main menu layout
     */
    private VBox createMainMenuLayout(StackPane root, Stage stage, Main main) {
        Label title = new Label("Sky Battle");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 50));
        title.setTextFill(Color.BLACK);

        // Buttons for main menu options
        Button startButton = new Button("START GAME");
        Button settingsButton = new Button("SETTINGS");
        Button howToPlayButton = new Button("HOW TO PLAY");
        Button exitButton = new Button("EXIT");

        // Style buttons
        styleButton(startButton, "#FF6347", "#FF4500");
        styleButton(settingsButton, "#FFD700", "#FFA500");
        styleButton(howToPlayButton, "#87CEFA", "#4682B4");
        styleButton(exitButton, "#A9A9A9", "#808080");

        // Button actions
        startButton.setOnAction(e -> {
            if (backgroundMediaPlayer != null) {
                backgroundMediaPlayer.stop();
            }
            main.startGame(stage);
        });

        settingsButton.setOnAction(e -> switchToLayout(root, 1));
        howToPlayButton.setOnAction(e -> switchToLayout(root, 2));
        exitButton.setOnAction(e -> stage.close());

        // Main menu layout
        VBox mainMenuLayout = new VBox(20, title, startButton, settingsButton, howToPlayButton, exitButton);
        mainMenuLayout.setAlignment(Pos.CENTER);
        mainMenuLayout.setStyle("-fx-padding: 20; -fx-background-color: rgba(255, 255, 255, 0.7);"
                + "-fx-border-color: #FF8C00; -fx-border-radius: 20; -fx-background-radius: 20;");
        mainMenuLayout.setMaxWidth(400);

        return mainMenuLayout;
    }

    /**
     * Creates the settings layout with sound and music toggles.
     *
     * @param root the root StackPane for switching layouts
     * @return the VBox containing the settings layout
     */
    private VBox createSettingsLayout(StackPane root) {
        Label settingsTitle = new Label("Settings");
        settingsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        settingsTitle.setTextFill(Color.web("#FF69B4"));

        // Checkboxes for sound settings
        CheckBox soundCheckBox = new CheckBox("Mute Sound Effects");
        soundCheckBox.setSelected(soundManager.isSoundEffectsMuted());
        soundCheckBox.setStyle("-fx-font-size: 20px;");
        soundCheckBox.setOnAction(e -> soundManager.setSoundEffectsMuted(soundCheckBox.isSelected()));

        CheckBox musicCheckBox = new CheckBox("Mute Background Music");
        musicCheckBox.setSelected(soundManager.isMusicMuted());
        musicCheckBox.setStyle("-fx-font-size: 20px;");
        musicCheckBox.setOnAction(e -> {
            boolean isMuted = musicCheckBox.isSelected();
            soundManager.setMusicMuted(isMuted);
            if (backgroundMediaPlayer != null) {
                backgroundMediaPlayer.setVolume(isMuted ? 0 : 1);
            }
        });

        // Back button
        Button backButton = new Button("BACK");
        styleButton(backButton, "#87CEFA", "#4682B4");
        backButton.setOnAction(e -> switchToLayout(root, 0));

        // Settings layout
        VBox settingsLayout = new VBox(20, settingsTitle, soundCheckBox, musicCheckBox, backButton);
        settingsLayout.setAlignment(Pos.CENTER);
        settingsLayout.setStyle("-fx-padding: 20; -fx-background-color: rgba(255, 255, 255, 0.7);"
                + "-fx-border-color: #FF69B4; -fx-border-radius: 15;");
        settingsLayout.setMaxWidth(400);

        return settingsLayout;
    }

    /**
     * Switches between different layouts in the root StackPane.
     *
     * @param root        the root StackPane
     * @param layoutIndex the index of the layout to show
     */
    private void switchToLayout(StackPane root, int layoutIndex) {
        for (int i = 1; i < root.getChildren().size(); i++) {
            root.getChildren().get(i).setVisible(i == layoutIndex + 1);
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
        button.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        button.setTextFill(Color.WHITE);
        button.setPrefWidth(250);
        button.setStyle("-fx-background-color: linear-gradient(to bottom, " + startColor + ", " + endColor + ");"
                + "-fx-background-radius: 15; -fx-padding: 10 40; -fx-border-radius: 15;");

        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: linear-gradient(to bottom, " + endColor + ", " + startColor + ");"
                + "-fx-background-radius: 15; -fx-padding: 10 40; -fx-border-radius: 15;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: linear-gradient(to bottom, " + startColor + ", " + endColor + ");"
                + "-fx-background-radius: 15; -fx-padding: 10 40; -fx-border-radius: 15;"));
    }

    /**
     * Creates the "How to Play" layout with instructions.
     *
     * @param root the root StackPane for switching layouts
     * @return the VBox containing the "How to Play" layout
     */
    private VBox createHowToPlayLayout(StackPane root) {
        Label howToPlayTitle = new Label("How to Play");
        howToPlayTitle.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        howToPlayTitle.setTextFill(Color.web("#FF69B4"));

        // Instruction image
        ImageView instructionImage = new ImageView(new Image(getClass().getResource("/com/example/demo/images/instruction.png").toExternalForm()));
        instructionImage.setFitWidth(350);
        instructionImage.setPreserveRatio(true);

        // Instruction text
        Label instructions = new Label(
                "Destroy all enemy spiders to advance.\n\n" +
                        "Collect power-ups to enhance abilities.\n\n" +
                        "Defeat the boss to WIN the game!\n"
        );
        instructions.setFont(Font.font("Arial", 18));
        instructions.setTextFill(Color.DARKBLUE);

        // Back button
        Button backButton = new Button("BACK");
        styleButton(backButton, "#87CEFA", "#4682B4");
        backButton.setOnAction(e -> switchToLayout(root, 0));

        // Layout
        VBox howToPlayLayout = new VBox(20, howToPlayTitle, instructionImage, instructions, backButton);
        howToPlayLayout.setAlignment(Pos.CENTER);
        howToPlayLayout.setStyle("-fx-padding: 20; -fx-background-color: rgba(255, 255, 255, 0.7);"
                + "-fx-border-color: #FF69B4; -fx-border-radius: 15;");
        howToPlayLayout.setMaxWidth(400);

        return howToPlayLayout;
    }
}
