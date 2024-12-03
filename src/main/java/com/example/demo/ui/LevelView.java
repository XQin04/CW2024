package com.example.demo.ui;

import javafx.scene.Group;

public class LevelView {

	private final Group root;
	private final ImageDisplayManager imageDisplayManager;
	private final HeartManager heartManager;

	public LevelView(Group root, int heartsToDisplay, double screenWidth, double screenHeight) {
		this.root = root;
		this.heartManager = new HeartManager(5, 25, heartsToDisplay);  // Heart position and initial count
		this.imageDisplayManager = new ImageDisplayManager(root, screenWidth, screenHeight);  // Image manager
	}

	public void showHeartDisplay() {
		heartManager.showHeartDisplay(root);
	}

	public void showWinImage() {
		imageDisplayManager.showWinImage();
	}

	public void showGameOverImage() {
		imageDisplayManager.showGameOverImage();
	}

	public void removeWinImage() {
		imageDisplayManager.removeWinImage();
	}

	public void removeGameOverImage() {
		imageDisplayManager.removeGameOverImage();
	}

	public void removeHearts(int heartsRemaining) {
		heartManager.removeHearts(heartsRemaining);
	}
}
