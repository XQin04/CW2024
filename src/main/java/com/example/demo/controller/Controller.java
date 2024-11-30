package com.example.demo.controller;

import com.example.demo.gameplay.LevelParent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Observable;
import java.util.Observer;


public class Controller implements Observer {

	private static final String LEVEL_ONE_CLASS_NAME = "com.example.demo.gameplay.LevelOne";
	private final Stage stage;

	public Controller(Stage stage) {
		this.stage = stage;
	}

	public void launchGame() throws ClassNotFoundException, NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException  {

		stage.show();
		goToLevel(LEVEL_ONE_CLASS_NAME);
	}

	private void goToLevel(String className) throws ClassNotFoundException, NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Class<?> myClass = Class.forName(className);
		String levelName;
		if (className.endsWith("LevelOne")) {
			levelName = "Level 1";
		} else if (className.endsWith("LevelTwo")) {
			levelName = "Level 2";
		} else if (className.endsWith("LevelThree")) {
			levelName = "Final Level";
		} else {
			levelName = "Unknown Level";
		}
		Constructor<?> constructor = myClass.getConstructor(double.class, double.class, Stage.class);
		LevelParent myLevel = (LevelParent) constructor.newInstance(stage.getHeight(), stage.getWidth(), stage);
		myLevel.addObserver(this);
		Scene scene = myLevel.initializeScene(stage);
		stage.setScene(scene);
		myLevel.startGame(levelName);

	}

	@Override
	public void update(Observable arg0, Object arg1) {
		try {
			goToLevel((String) arg1);
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
				 | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText(e.getClass().toString());
			alert.show();
		}
	}

}
