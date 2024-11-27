package com.example.demo;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import com.example.demo.controller.Main;

public class MainMenu {

    private static final int SCREEN_WIDTH = 1300;
    private static final int SCREEN_HEIGHT = 750;

    // Flags for sound effects and background music
    private boolean isSoundMuted = false;
    private boolean isMusicMuted = false;

    // MediaPlayer for background music
    private MediaPlayer backgroundMediaPlayer;

    public void start(Stage stage, Main main) {
        // Load and play the background music for the main menu
        playBackgroundMusic("/com/example/demo/sounds/Menu.mp3");

        // Root StackPane to manage switching between different menu screens
        StackPane root = new StackPane();

        // Main menu layout
        VBox mainMenuLayout = createMainMenuLayout(root, stage, main);

        // Settings layout
        VBox settingsLayout = createSettingsLayout(root);

        // How to Play layout
        VBox howToPlayLayout = createHowToPlayLayout(root);

        // Add all layouts to the root StackPane
        root.getChildren().addAll(mainMenuLayout, settingsLayout, howToPlayLayout);

        // Initially show only the main menu
        settingsLayout.setVisible(false);
        howToPlayLayout.setVisible(false);

        // Scene
        Scene menuScene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);

        // Stage setup
        stage.setScene(menuScene);
        stage.setTitle("Sky Battle - Main Menu");
        stage.show();
    }

    private void playBackgroundMusic(String filePath) {
        if (backgroundMediaPlayer != null) {
            backgroundMediaPlayer.stop();
        }

        Media media = new Media(getClass().getResource(filePath).toExternalForm());
        backgroundMediaPlayer = new MediaPlayer(media);
        backgroundMediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop the music
        backgroundMediaPlayer.setVolume(isMusicMuted ? 0 : 1); // Adjust volume based on mute flag
        backgroundMediaPlayer.play();
    }

    private void restartBackgroundMusic(String filePath) {
        if (backgroundMediaPlayer != null) {
            backgroundMediaPlayer.stop(); // Stop current music
        }
        // Recreate the MediaPlayer and play from the beginning
        Media media = new Media(getClass().getResource(filePath).toExternalForm());
        backgroundMediaPlayer = new MediaPlayer(media);
        backgroundMediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        backgroundMediaPlayer.setVolume(isMusicMuted ? 0 : 1);
        backgroundMediaPlayer.play();
    }

    private VBox createMainMenuLayout(StackPane root, Stage stage, Main main) {
        // Game title
        Label title = new Label("Sky Battle");
        title.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 50));
        title.setTextFill(Color.BLACK); // Set title color to black

        // Buttons
        Button startButton = new Button("START GAME");
        Button settingsButton = new Button("SETTINGS");
        Button howToPlayButton = new Button("HOW TO PLAY");
        Button exitButton = new Button("EXIT");

        // Style buttons - same size and consistent look
        styleButton(startButton, "#FF6347", "#FF4500");
        styleButton(settingsButton, "#FFD700", "#FFA500");
        styleButton(howToPlayButton, "#87CEFA", "#4682B4");
        styleButton(exitButton, "#A9A9A9", "#808080");

        // Set button actions
        startButton.setOnAction(e -> {
            if (backgroundMediaPlayer != null) {
                backgroundMediaPlayer.stop(); // Stop the menu music
            }
            main.startGame(stage); // Call startGame from Main
        });

        settingsButton.setOnAction(e -> switchToLayout(root, 1)); // Switch to Settings layout
        howToPlayButton.setOnAction(e -> switchToLayout(root, 2)); // Switch to How to Play layout
        exitButton.setOnAction(e -> stage.close());

        // Main menu layout
        VBox mainMenuLayout = new VBox(20);
        mainMenuLayout.setAlignment(Pos.CENTER);
        mainMenuLayout.getChildren().addAll(title, startButton, settingsButton, howToPlayButton, exitButton);
        mainMenuLayout.setStyle("-fx-padding: 20; -fx-background-color: rgba(255, 255, 255, 0.7);"
                + "-fx-border-color: #FF8C00; -fx-border-radius: 20; -fx-background-radius: 20;");
        mainMenuLayout.setMaxWidth(400);

        return mainMenuLayout;
    }

    private VBox createSettingsLayout(StackPane root) {
        // Labels
        Label settingsTitle = new Label("Settings");
        settingsTitle.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 36));
        settingsTitle.setTextFill(Color.web("#FF69B4")); // Pink color for a cute effect

        // Checkboxes for sound and music
        CheckBox soundCheckBox = new CheckBox("Mute Sound Effects");
        soundCheckBox.setSelected(isSoundMuted);
        soundCheckBox.setStyle("-fx-font-size: 20px;");
        soundCheckBox.setOnAction(e -> {
            isSoundMuted = soundCheckBox.isSelected();
            System.out.println("Sound Effects Muted: " + isSoundMuted);
        });

        CheckBox musicCheckBox = new CheckBox("Mute Background Music");
        musicCheckBox.setSelected(isMusicMuted);
        musicCheckBox.setStyle("-fx-font-size: 20px;");
        musicCheckBox.setOnAction(e -> {
            isMusicMuted = musicCheckBox.isSelected();
            if (backgroundMediaPlayer != null) {
                backgroundMediaPlayer.setVolume(isMusicMuted ? 0 : 1);
            }
        });

        // Back button to return to the main menu
        Button backButton = new Button("BACK");
        styleButton(backButton, "#87CEFA", "#4682B4");
        backButton.setOnAction(e -> switchToLayout(root, 0)); // Switch back to main menu

        // Settings layout
        VBox settingsLayout = new VBox(20, settingsTitle, soundCheckBox, musicCheckBox, backButton);
        settingsLayout.setAlignment(Pos.CENTER);
        settingsLayout.setStyle("-fx-padding: 20; -fx-background-color: #FFE4E1; -fx-border-color: #FF69B4; -fx-border-radius: 15;");
        settingsLayout.setMaxWidth(400);

        return settingsLayout;
    }

    private VBox createHowToPlayLayout(StackPane root) {
        // Label for instructions
        Label howToPlayTitle = new Label("How to Play");
        howToPlayTitle.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 36));
        howToPlayTitle.setTextFill(Color.web("#FF69B4")); // Pink color for a cute effect

        Label instructions = new Label(
                "1. Use Arrow keys to move the plane.\n" +
                        "2. Press SPACE to shoot projectiles.\n" +
                        "3. Destroy all enemy planes to advance.\n" +
                        "4. Collect power-ups to enhance abilities.\n" +
                        "5. Defeat the boss to win the game.\n"
        );
        instructions.setFont(Font.font("Comic Sans MS", 20));
        instructions.setTextFill(Color.DARKBLUE);

        // Back button to return to the main menu
        Button backButton = new Button("BACK");
        styleButton(backButton, "#87CEFA", "#4682B4");
        backButton.setOnAction(e -> switchToLayout(root, 0)); // Switch back to main menu

        // How to Play layout
        VBox howToPlayLayout = new VBox(20, howToPlayTitle, instructions, backButton);
        howToPlayLayout.setAlignment(Pos.CENTER);
        howToPlayLayout.setStyle("-fx-padding: 20; -fx-background-color: #B0E0E6; -fx-border-color: #4682B4; -fx-border-radius: 15;");
        howToPlayLayout.setMaxWidth(400);

        return howToPlayLayout;
    }

    private void switchToLayout(StackPane root, int layoutIndex) {
        for (int i = 0; i < root.getChildren().size(); i++) {
            root.getChildren().get(i).setVisible(i == layoutIndex);
        }
    }

    private void styleButton(Button button, String startColor, String endColor) {
        button.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        button.setTextFill(Color.WHITE);
        button.setPrefWidth(250); // Set the width of all buttons to the same size for consistency
        button.setStyle("-fx-background-color: linear-gradient(to bottom, " + startColor + ", " + endColor + ");"
                + "-fx-background-radius: 15; -fx-padding: 10 40; -fx-border-radius: 15;");

        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: linear-gradient(to bottom, " + endColor + ", " + startColor + ");"
                + "-fx-background-radius: 15; -fx-padding: 10 40; -fx-border-radius: 15;"));

        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: linear-gradient(to bottom, " + startColor + ", " + endColor + ");"
                + "-fx-background-radius: 15; -fx-padding: 10 40; -fx-border-radius: 15;"));
    }
}
