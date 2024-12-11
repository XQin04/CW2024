package com.example.demo.controller;

import com.example.demo.ui.menus.MainMenu;
import javafx.application.Application;
import javafx.stage.Stage;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Entry point for the Sky Battle game.
 * <p>
 * This class initializes the JavaFX application, sets up the main menu,
 * and provides functionality to launch the game.
 * </p>
 */
public class Main extends Application {

    private static final int SCREEN_WIDTH = 1300; // Default screen width
    private static final int SCREEN_HEIGHT = 750; // Default screen height
    private static final String TITLE = "Sky Battle"; // Title of the application

    private static final Logger logger = Logger.getLogger(Main.class.getName()); // Logger instance

    /**
     * Main entry point for the Java application.
     * <p>
     * This method launches the JavaFX application.
     * </p>
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        launch(); // Launch the JavaFX application
    }

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
        configureStage(stage); // Configure the primary stage

        // Initialize and start the main menu
        MainMenu menu = new MainMenu();
        menu.start(stage, this); // Pass the stage and the main application reference to the menu
    }

    /**
     * Configures the main application stage with default properties.
     * Sets the stage's title, size, and ensures it is not resizable.
     *
     * @param stage The primary stage to configure.
     */
    private void configureStage(Stage stage) {
        stage.setTitle(TITLE); // Set the window title
        stage.setResizable(false); // Disable resizing
        stage.setWidth(SCREEN_WIDTH); // Set the default width
        stage.setHeight(SCREEN_HEIGHT); // Set the default height
    }

    /**
     * Launches the game from the main menu.
     *
     * <p>
     * This method initializes the game controller and starts the game
     * by transitioning to the first level.
     * </p>
     *
     * @param stage The primary stage where the game will be displayed.
     */
    public void startGame(Stage stage) {
        try {
            Controller gameController = new Controller(stage); // Local variable
            gameController.launchGame(); // Launch the game
        } catch (Exception e) {
            handleException(e); // Handle any exceptions that occur during initialization
        }
    }

    /**
     * Handles exceptions that occur during game initialization.
     * Logs the exception details for debugging purposes.
     *
     * @param e The exception to handle.
     */
    private void handleException(Exception e) {
        logger.log(Level.SEVERE, "An error occurred while starting the game", e); // Log exception
    }
}
