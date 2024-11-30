package com.example.demo.controller;

import com.example.demo.ui.MainMenu;
import javafx.application.Application;
import javafx.stage.Stage;

import java.lang.reflect.InvocationTargetException;

public class Main extends Application {

	private static final int SCREEN_WIDTH = 1300;
	private static final int SCREEN_HEIGHT = 750;
	private static final String TITLE = "Sky Battle";
	private Controller myController;

	@Override
	public void start(Stage stage) throws ClassNotFoundException, NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		stage.setTitle(TITLE);
		stage.setResizable(false);
		stage.setHeight(SCREEN_HEIGHT);
		stage.setWidth(SCREEN_WIDTH);

		// Initialize the main menu and pass this instance
		MainMenu menu = new MainMenu();
		menu.start(stage, this); // Pass stage and main class to menu
	}

	// Method to launch the game from the main menu
	public void startGame(Stage stage) {
		try {
			myController = new Controller(stage);
			myController.launchGame();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static void main(String[] args) {
		launch();
	}
}
