package com.example.demo;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class GameOverImage extends ImageView {

	private static final String IMAGE_NAME = "/com/example/demo/images/gameover.png";
	private static final double IMAGE_WIDTH = 650; // Adjust the width to make the image smaller
	private static final double IMAGE_HEIGHT = 600; // Adjust the height to make the image smaller

	public GameOverImage(double xPosition, double yPosition) {
		// Set up the image
		setImage(new Image(getClass().getResource(IMAGE_NAME).toExternalForm()));
		setFitWidth(IMAGE_WIDTH);
		setFitHeight(IMAGE_HEIGHT);
		setLayoutX(280);
		setLayoutY(40);
	}
}

