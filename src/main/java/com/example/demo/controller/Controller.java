package com.example.demo.controller;

import com.example.demo.observer.Observer;
import com.example.demo.gameplay.levels.LevelParent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.application.Platform;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Handles the overall control of the game, including level transitions and game launch.
 */
public class Controller implements Observer {

	private static final String LEVEL_ONE_CLASS_NAME = "com.example.demo.gameplay.levels.LevelOne";
	private final Stage stage;

	/**
	 * Constructs a Controller for managing game levels and stages.
	 *
	 * @param stage The primary stage where the game is displayed.
	 */
	public Controller(Stage stage) {
		this.stage = stage;
	}

	/**
	 * Launches the game starting from Level 1.
	 *
	 * @throws ClassNotFoundException    If the level class cannot be found.
	 * @throws NoSuchMethodException     If the level class does not have the required constructor.
	 * @throws InstantiationException    If the level class cannot be instantiated.
	 * @throws IllegalAccessException    If access to the level class constructor is not allowed.
	 * @throws InvocationTargetException If an exception occurs while invoking the constructor.
	 */
	public void launchGame() throws ClassNotFoundException, NoSuchMethodException, InstantiationException,
			IllegalAccessException, InvocationTargetException {
		stage.show();
		goToLevel(LEVEL_ONE_CLASS_NAME);
	}

	/**
	 * Transitions to a specified level using reflection to dynamically load the level class.
	 *
	 * @param className The fully qualified class name of the level to transition to.
	 * @throws ClassNotFoundException    If the class cannot be found.
	 * @throws NoSuchMethodException     If the required constructor is not present in the class.
	 * @throws InstantiationException    If the class cannot be instantiated.
	 * @throws IllegalAccessException    If the constructor is not accessible.
	 * @throws InvocationTargetException If an exception occurs while invoking the constructor.
	 */
	private void goToLevel(String className) throws ClassNotFoundException, NoSuchMethodException,
			InstantiationException, IllegalAccessException, InvocationTargetException {

		Class<?> levelClass = Class.forName(className);
		String levelName = getLevelName(className);

		Constructor<?> constructor = levelClass.getConstructor(double.class, double.class, Stage.class);
		LevelParent level = (LevelParent) constructor.newInstance(stage.getHeight(), stage.getWidth(), stage);
		level.addObserver(this); // Correctly register this as an Observer

		Scene scene = level.initializeScene(stage);
		stage.setScene(scene);
		level.startGame(levelName);
	}

	/**
	 * Determines the level name based on the class name.
	 *
	 * @param className The fully qualified class name of the level.
	 * @return A user-friendly level name.
	 */
	private String getLevelName(String className) {
		if (className.endsWith("LevelOne")) {
			return "Level 1";
		} else if (className.endsWith("LevelTwo")) {
			return "Level 2";
		} else if (className.endsWith("LevelThree")) {
			return "Final Level";
		} else {
			return "Unknown Level";
		}
	}

	/**
	 * Receives updates from levels and transitions to the next level.
	 *
	 * @param arg The argument passed by the observable, expected to be the next level's class name.
	 */
	@Override
	public void update(Object arg) {
		if (arg instanceof String) {
			String message = (String) arg;

			switch (message) {
				case "LOSE_GAME":
					System.out.println("Game over. Showing end game menu.");
					// Handle game over logic here if needed
					break;

				case "WIN_GAME":
					System.out.println("Game won. Showing win screen.");
					// Handle game win logic here if needed
					break;

				case "TOGGLE_PAUSE":
					System.out.println("Game paused.");
					// Handle pause or resume logic here if needed
					break;

				case "RESUME_GAME":
					System.out.println("Game resumed.");
					// Handle game resume logic here if needed
					break;

				default:
					try {
						// Treat any other string as a level transition
						goToLevel(message);
					} catch (ClassNotFoundException | NoSuchMethodException | InstantiationException
							 | IllegalAccessException | InvocationTargetException e) {
						showErrorAlert(e);
					}
					break;
			}
		} else {
			System.err.println("Unhandled update argument: " + arg);
		}
	}


	/**
	 * Displays an error alert with the exception details.
	 *
	 * @param e The exception to display.
	 */
	private void showErrorAlert(Exception e) {
		Platform.runLater(() -> {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("An error occurred while transitioning levels.");
			alert.setContentText(e.getMessage());
			alert.show(); // Use show() instead of showAndWait()
		});
	}

}

