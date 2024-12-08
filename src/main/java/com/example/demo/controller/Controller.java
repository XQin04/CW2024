package com.example.demo.controller;

import com.example.demo.gameplay.levels.LevelParent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Observable;
import java.util.Observer;

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
		level.addObserver(this);

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
	 * @param observable The observable object sending the update.
	 * @param arg        The argument passed by the observable, expected to be the next level's class name.
	 */
	@Override
	public void update(Observable observable, Object arg) {
		try {
			goToLevel((String) arg);
		} catch (ClassNotFoundException | NoSuchMethodException | InstantiationException
				 | IllegalAccessException | InvocationTargetException e) {
			showErrorAlert(e);
		}
	}

	/**
	 * Displays an error alert with the exception details.
	 *
	 * @param e The exception to display.
	 */
	private void showErrorAlert(Exception e) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText("An error occurred while transitioning levels.");
		alert.setContentText(e.getMessage());
		alert.showAndWait();
	}
}
