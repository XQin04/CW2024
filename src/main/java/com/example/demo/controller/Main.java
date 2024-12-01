package com.example.demo.controller;

import com.example.demo.ui.MainMenu;
import javafx.application.Application;
import javafx.stage.Stage;

import java.lang.reflect.InvocationTargetException;

/**
 * Entry point for the Sky Battle game.
 * Initializes the main menu and allows launching the game.
 */
public class Main extends Application {

	// Constants for the game's screen properties and title
	private static final int SCREEN_WIDTH = 1300;
	private static final int SCREEN_HEIGHT = 750;
	private static final String TITLE = "Sky Battle";

	private Controller gameController;

	/**
	 * Starts the JavaFX application and initializes the main menu.
	 *
	 * @param stage The primary stage for the application.
	 * @throws ClassNotFoundException    If the game controller class cannot be found.
	 * @throws NoSuchMethodException     If the required constructor is not present in the game controller class.
	 * @throws InstantiationException    If the game controller cannot be instantiated.
	 * @throws IllegalAccessException    If the game controller constructor is not accessible.
	 * @throws InvocationTargetException If an error occurs during the game controller instantiation.
	 */
	@Override
	public void start(Stage stage) throws ClassNotFoundException, NoSuchMethodException, InstantiationException,
			IllegalAccessException, InvocationTargetException {
		configureStage(stage);

		// Initialize and start the main menu
		MainMenu menu = new MainMenu();
		menu.start(stage, this);
	}

	/**
	 * Configures the main application stage with default properties.
	 *
	 * @param stage The primary stage to configure.
	 */
	private void configureStage(Stage stage) {
		stage.setTitle(TITLE);
		stage.setResizable(false);
		stage.setWidth(SCREEN_WIDTH);
		stage.setHeight(SCREEN_HEIGHT);
	}

	/**
	 * Launches the game from the main menu.
	 *
	 * @param stage The primary stage where the game will be displayed.
	 */
	public void startGame(Stage stage) {
		try {
			gameController = new Controller(stage);
			gameController.launchGame();
		} catch (Exception e) {
			handleException(e);
		}
	}

	/**
	 * Handles exceptions that occur during game initialization.
	 *
	 * @param e The exception to handle.
	 */
	private void handleException(Exception e) {
		System.err.println("An error occurred while starting the game:");
		e.printStackTrace();
	}

	/**
	 * Main entry point for the Java application.
	 *
	 * @param args Command-line arguments (not used).
	 */
	public static void main(String[] args) {
		launch();
	}
}
