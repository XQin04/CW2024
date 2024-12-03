package com.example.demo.ui;

import javafx.scene.Group;

public class ImageDisplayManager {

    private final Group root;
    private final WinImage winImage;
    private final GameOverImage gameOverImage;

    public ImageDisplayManager(Group root, double screenWidth, double screenHeight) {
        this.root = root;
        this.winImage = new WinImage(screenWidth, screenHeight);
        this.gameOverImage = new GameOverImage(screenWidth, screenHeight);
    }

    public void showWinImage() {
        root.getChildren().add(winImage);
        winImage.show();
    }

    public void showGameOverImage() {
        root.getChildren().add(gameOverImage);
        gameOverImage.show();
    }

    public void removeWinImage() {
        root.getChildren().remove(winImage);
    }

    public void removeGameOverImage() {
        root.getChildren().remove(gameOverImage);
    }
}
