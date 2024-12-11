package com.example.demo.gameplay;

import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.actors.player.UserSuperman;
import com.example.demo.controller.Main;
import com.example.demo.managers.*;
import com.example.demo.observer.Observable;
import com.example.demo.observer.Observer;
import com.example.demo.ui.UIManager;
import com.example.demo.ui.gameplayUI.LevelView;
import com.example.demo.ui.menus.MainMenu;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Abstract base class for game levels.
 * Manages game state, actors, UI components, and interactions for a specific level.
 *
 * <p>
 * This class extends {@link Observable} to notify observers of level-related events
 * and implements {@link Observer} to respond to game state changes.
 * </p>
 */
public abstract class LevelParent extends Observable implements Observer {

    // Constants for screen adjustments and game loop timing
    private static final double SCREEN_HEIGHT_ADJUSTMENT = 150;
    private static final int MILLISECOND_DELAY = 50;
    // Managers
    protected final CollisionManager collisionManager;
    protected final PowerUpManager powerUpManager;
    protected final ProjectileManager projectileManager;
    protected final EnemyManager enemyManager;
    // Screen dimensions
    private final double screenHeight;
    private final double screenWidth;
    private final double enemyMaximumYPosition;
    // Root groups for UI elements and menus
    private final Group root;
    private final Group menuLayer;
    // Gamescene
    private final Scene scene;
    // Game elements and actors
    private final UserSuperman user;
    private final ImageView background;
    private final List<ActiveActorDestructible> friendlyUnits;
    // Game state and controls
    private final Timeline timeline;
    // Level-related views and settings
    private final LevelView levelView;
    // Audios
    private final SoundManager soundManager;
    private final UIManager uiManager;
    private final GameStateManager gameStateManager;
    private final InputHandler inputHandler;
    private Button pauseButton;
    private boolean isPaused;
    private int currentNumberOfEnemies;
    private String currentLevel;
    private MediaPlayer gameBackgroundMediaPlayer;

    /**
     * Constructs a LevelParent instance with the specified parameters.
     *
     * @param backgroundImageName Path to the level's background image.
     * @param screenHeight        Height of the screen.
     * @param screenWidth         Width of the screen.
     * @param playerInitialHealth Initial health of the player.
     * @param stage               The game stage.
     * @param levelName           Name of the level.
     */
    public LevelParent(String backgroundImageName, double screenHeight, double screenWidth,
                       int playerInitialHealth, Stage stage, String levelName) {
        // Initialize groups and scene
        this.menuLayer = new Group();
        this.root = new Group();
        this.scene = new Scene(root, screenWidth, screenHeight);

        // Check if the background resource exists
        URL resource = getClass().getResource(backgroundImageName);
        if (resource == null) {
            throw new IllegalArgumentException("Background image not found: " + backgroundImageName);
        }
        this.background = new ImageView(new Image(resource.toExternalForm()));

        // Initialize game components
        this.timeline = new Timeline();
        this.user = new UserSuperman(this, playerInitialHealth);
        this.soundManager = SoundManager.getInstance();
        this.collisionManager = new CollisionManager(user, soundManager);
        this.gameStateManager = GameStateManager.getInstance();
        this.gameStateManager.addObserver(this); // Observe game state changes
        this.powerUpManager = PowerUpManager.getInstance();
        this.powerUpManager.initialize(root);
        this.inputHandler = new InputHandler(user, gameStateManager);
        this.projectileManager = ProjectileManager.getInstance();
        this.projectileManager.initialize(root);
        this.enemyManager = EnemyManager.getInstance();
        this.enemyManager.initialize(root);

        // Screen properties
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
        this.enemyMaximumYPosition = screenHeight - SCREEN_HEIGHT_ADJUSTMENT;


        // Level setup
        this.currentLevel = levelName;
        this.levelView = instantiateLevelView();

        // Initialize game state
        this.currentNumberOfEnemies = 0;
        this.isPaused = false;

        // Add menu layer to the root group
        this.root.getChildren().add(menuLayer);
        menuLayer.toBack();

        // Initialize UI
        this.friendlyUnits = new ArrayList<>();
        this.uiManager = UIManager.getInstance(this, menuLayer, screenWidth, screenHeight, stage);
        addObserver(uiManager); // Register UIManager as an observer

        initializeTimeline();
        friendlyUnits.add(user);
        initializeGameBackgroundMusic();
    }

    // Abstract methods for subclasses to define level-specific behavior
    protected abstract void initializeFriendlyUnits();

    protected abstract LevelView instantiateLevelView();

    protected abstract void checkIfGameOver();

    protected abstract void spawnEnemyUnits();

    /**
     * Initializes the scene for the current level.
     *
     * @param stage The game stage.
     * @return The initialized {@link Scene} for the level.
     */
    public Scene initializeScene(Stage stage) {
        initializeBackground();
        uiManager.initializeUI(); // Initialize UI (pause button and menus)
        initializeFriendlyUnits();
        initializeGameBackgroundMusic();
        levelView.showHeartDisplay(); // Display player's health
        return scene;
    }

    /**
     * Initializes the background image for the level.
     * Sets up key event handlers for user input and adds the background to the root group.
     */
    private void initializeBackground() {
        background.setFocusTraversable(true); // Allow key events
        background.setFitHeight(screenHeight); // Fit background to screen height
        background.setFitWidth(screenWidth); // Fit background to screen width

        // Delegate key handling to InputHandler
        background.setOnKeyPressed(inputHandler::handleKeyPress);
        background.setOnKeyReleased(inputHandler::handleKeyRelease);

        // Add the background to the root group
        root.getChildren().add(background);
    }

    /**
     * Initializes the game loop timeline.
     * The timeline updates the scene periodically based on the configured delay.
     */
    private void initializeTimeline() {
        timeline.setCycleCount(Timeline.INDEFINITE); // Run indefinitely
        KeyFrame gameLoop = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> updateScene());
        timeline.getKeyFrames().add(gameLoop);
    }


    /**
     * Retrieves the {@link ProjectileManager} instance.
     * The {@code ProjectileManager} handles all projectiles in the game, including
     * creation, updates, and removal.
     *
     * @return The {@code ProjectileManager} instance used by this level.
     */
    public ProjectileManager getProjectileManager() {
        return projectileManager; // Return the instance managing projectiles
    }

    /**
     * Initializes the background music for the game level.
     * This method sets up the music to loop indefinitely and configures its volume.
     * If the music file cannot be loaded, an error message is logged.
     */
    private void initializeGameBackgroundMusic() {
        try {
            // Load the background music file with null check
            URL musicResource = getClass().getResource("/com/example/demo/sounds/Background.mp3");
            if (musicResource == null) {
                throw new IllegalArgumentException("Music resource not found.");
            }
            Media gameMusic = new Media(musicResource.toExternalForm());

            // Create a MediaPlayer instance for the music
            gameBackgroundMediaPlayer = new MediaPlayer(gameMusic);

            // Configure the MediaPlayer to loop the music indefinitely
            gameBackgroundMediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

            // Set the volume for the music
            gameBackgroundMediaPlayer.setVolume(0.6);
        } catch (Exception e) {
            // Log an error message if the music file cannot be loaded
            Logger.getLogger(LevelParent.class.getName()).log(Level.SEVERE, "Error loading background music", e);
        }
    }


    /**
     * Starts the game for the current level.
     * Displays level information and transitions to the PLAYING state.
     *
     * @param levelName Name of the level to start.
     */
    public void startGame(String levelName) {
        gameStateManager.setCurrentState(GameStateManager.GameState.INITIALIZING); // Set state to initializing
        showLevelInfo(levelName);

        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(event -> {
            gameStateManager.setCurrentState(GameStateManager.GameState.PLAYING);
            timeline.play(); // Start game loop
            background.requestFocus(); // Ensure game focus
        });
        pause.play();
    }

    /**
     * Updates the state of the level based on changes to the game state.
     * This method is triggered when the {@link GameStateManager} notifies its observers
     * of a state change. The state dictates how the game level should behave (e.g., play, pause, win, or lose).
     *
     * @param arg The updated game state, provided as an instance of {@link GameStateManager.GameState}.
     *            If the argument is not of the correct type, this method does nothing.
     */
    @Override
    public void update(Object arg) {
        // Check if the argument is an instance of GameStateManager.GameState
        if (arg instanceof GameStateManager.GameState newState) {
            switch (newState) {
                case PLAYING -> resumeGame();
                case PAUSED -> pauseGame();
                case GAME_OVER -> loseGame();
                case WIN -> winGame();
                default -> System.out.println("Unhandled game state: " + newState);
            }
        }

    }

    /**
     * Displays information about the current level to the player.
     *
     * @param levelName Name of the level being played.
     */
    private void showLevelInfo(String levelName) {
        if (pauseButton != null) {
            pauseButton.setDisable(true); // Temporarily disable pause button
        }

        String levelMessage = getLevelMessage(levelName); // Get the message for the level
        Text levelInfo = createLevelInfoText(levelMessage); // Create the level info text

        root.getChildren().add(levelInfo); // Add level info to the scene

        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(event -> {
            root.getChildren().remove(levelInfo); // Remove level info text
            background.requestFocus(); // Refocus on the game

            if (pauseButton != null) {
                pauseButton.setDisable(false); // Re-enable pause button
            }

            if (!soundManager.isMusicMuted() && gameBackgroundMediaPlayer != null &&
                    gameBackgroundMediaPlayer.getStatus() != MediaPlayer.Status.PLAYING) {
                gameBackgroundMediaPlayer.play();
            }

            timeline.play(); // Start the game loop
        });
        pause.play();
    }

    /**
     * Retrieves the appropriate level message based on the level name.
     *
     * @param levelName The name of the level.
     * @return The message to display for the level.
     */
    private String getLevelMessage(String levelName) {
        return switch (levelName) {
            case "Level 1" -> "Level 1: Kill all the enemies!";
            case "Level 2" -> "Level 2: Kill the boss!";
            case "Final Level" -> "Level 3: Kill all the enemies and the boss!";
            default -> levelName; // Default to the provided level name
        };
    }


    /**
     * Creates a styled {@link Text} object to display level information.
     *
     * @param message The message to display.
     * @return A styled {@link Text} object.
     */
    private Text createLevelInfoText(String message) {
        Text levelInfo = new Text(message);
        levelInfo.setFont(Font.font("Arial", 30));
        levelInfo.setFill(Color.WHITE);
        levelInfo.setStroke(Color.BLACK);
        levelInfo.setStrokeWidth(1);

        levelInfo.setX(screenWidth / 2 - levelInfo.getLayoutBounds().getWidth() / 2); // Center text
        levelInfo.setY(50); // Position near the top of the screen

        return levelInfo;
    }

    /**
     * Transitions the game to the next level by cleaning up the current level
     * and initializing the necessary components for the new level.
     *
     * <p>This method performs the following actions:</p>
     * <ul>
     *     <li>Unregisters the current level as an observer.</li>
     *     <li>Clears all projectiles, enemies, and power-ups from the game.</li>
     *     <li>Stops background music and the game loop timeline.</li>
     *     <li>Resets the UIManager and initializes the new UI elements.</li>
     *     <li>Notifies observers about the new level name.</li>
     *     <li>Sets the game state to INITIALIZING for the new level.</li>
     * </ul>
     *
     * @param levelName The name of the next level to load.
     */
    public void goToNextLevel(String levelName) {
        // Cleanup the current level and unregister LevelParent as an observer
        cleanup();

        // Set the game state to LOADING and update the current level name
        gameStateManager.setCurrentState(GameStateManager.GameState.LOADING);
        currentLevel = levelName;

        // Clear all projectiles, enemies, and power-ups from the game
        ProjectileManager.getInstance().clearAllProjectiles();
        EnemyManager.getInstance().clearAllEnemies();
        PowerUpManager.getInstance().clearAllPowerUps();

        // Stop the game background music and the game loop
        stopGameBackgroundMusic();
        timeline.stop();

        // Clear all visual elements from the root node
        root.getChildren().clear();

        // Reset and reinitialize the UIManager for the new level
        UIManager.resetInstance();
        uiManager.initializeUI();

        // Notify observers about the level change
        setChanged(); // Mark LevelParent as changed
        notifyObservers(levelName); // Notify all observers of the level name

        // Set the game state to INITIALIZING for the new level
        gameStateManager.setCurrentState(GameStateManager.GameState.INITIALIZING);
    }


    /**
     * Toggles the pause state of the game.
     * If the game is currently paused, it resumes the game. If the game is running, it pauses the game.
     *
     * <p>This method notifies all observers of the toggle action using the {@code "TOGGLE_PAUSE"} message.</p>
     */
    public void togglePause() {
        if (isPaused) {
            resumeGame(); // Resume the game if currently paused
        } else {
            pauseGame(); // Pause the game if currently running
        }

        setChanged(); // Mark this object as changed for the observer pattern
        notifyObservers("TOGGLE_PAUSE"); // Notify observers about the pause toggle
    }


    /**
     * Pauses the game by stopping the timeline, updating the game state,
     * and displaying the pause menu.
     */
    private void pauseGame() {
        gameStateManager.setCurrentState(GameStateManager.GameState.PAUSED); // Update state to PAUSED
        timeline.pause(); // Pause the game loop

        if (gameBackgroundMediaPlayer != null) {
            gameBackgroundMediaPlayer.pause(); // Pause background music
        }

        isPaused = true; // Update pause state
        uiManager.getPauseButton().setVisible(false); // Hide pause button
        uiManager.getPauseMenu().setVisible(true); // Show pause menu
        menuLayer.toFront(); // Bring menu layer to the front
    }

    /**
     * Resumes the game by restarting the timeline, updating the game state,
     * and hiding the pause menu.
     */
    public void resumeGame() {
        gameStateManager.setCurrentState(GameStateManager.GameState.PLAYING); // Update state to PLAYING
        timeline.play(); // Resume the game loop

        if (gameBackgroundMediaPlayer != null && !soundManager.isMusicMuted()) {
            gameBackgroundMediaPlayer.play(); // Resume background music
        }
        setChanged(); // Mark LevelParent as changed
        notifyObservers("RESUME_GAME"); // Notify observers of win

        isPaused = false; // Update pause state
        uiManager.getPauseButton().setVisible(true); // Show pause button
        uiManager.getPauseMenu().setVisible(false); // Hide pause menu
        background.requestFocus(); // Refocus on the game
    }

    /**
     * Ends the game with a win state.
     * Displays the "You Win" screen and transitions to the end game menu.
     */
    protected void winGame() {
        gameStateManager.setCurrentState(GameStateManager.GameState.WIN); // Set state to WIN
        timeline.stop(); // Stop the game loop
        levelView.showWinImage(); // Show the win image

        if (uiManager.getPauseButton() != null) {
            uiManager.getPauseButton().setVisible(false); // Hide pause button
        }

        soundManager.playSound("win"); // Play win sound

        setChanged(); // Mark LevelParent as changed
        notifyObservers("WIN_GAME"); // Notify observers of win

        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(e -> {
            levelView.removeWinImage(); // Remove win image
            uiManager.getEndGameMenu().show(true); // Show end game menu
            menuLayer.toFront(); // Bring menu layer to the front
        });
        delay.play();
    }

    /**
     * Ends the game with a game-over state.
     * Displays the "Game Over" screen and transitions to the end game menu.
     */
    protected void loseGame() {
        gameStateManager.setCurrentState(GameStateManager.GameState.GAME_OVER); // Set state to GAME_OVER
        timeline.stop(); // Stop the game loop
        levelView.showGameOverImage(); // Show game-over image

        if (uiManager.getPauseButton() != null) {
            uiManager.getPauseButton().setVisible(false); // Hide pause button
        }

        soundManager.playSound("gameOver"); // Play game-over sound

        setChanged(); // Mark LevelParent as changed
        notifyObservers("LOSE_GAME"); // Notify observers of game over

        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(e -> {
            levelView.removeGameOverImage(); // Remove game-over image
            uiManager.getEndGameMenu().show(false); // Show end game menu
            menuLayer.toFront(); // Bring menu layer to the front
        });
        delay.play();
    }

    /**
     * Cleans up resources and observers before transitioning to the main menu.
     *
     * @param stage The game stage.
     */
    public void goToMainMenu(Stage stage) {
        cleanup(); // Clean up resources and unregister observers
        timeline.stop(); // Stop game loop
        stopGameBackgroundMusic(); // Stop background music
        root.getChildren().clear(); // Clear game components

        // Clear power-ups
        PowerUpManager.getInstance().clearAllPowerUps();
        ProjectileManager.getInstance().clearAllProjectiles();

        // Reset UIManager to prepare for new game
        UIManager.resetInstance();

        // Initialize and display the main menu
        MainMenu mainMenu = new MainMenu();
        mainMenu.start(stage, new Main()); // Show main menu
    }


    /**
     * Stops the background music for the level.
     */
    private void stopGameBackgroundMusic() {
        if (gameBackgroundMediaPlayer != null) {
            gameBackgroundMediaPlayer.stop();
        }
    }

    /**
     * Updates the scene for each frame in the game loop.
     * This includes spawning enemies, updating actors, handling collisions, and checking for game over.
     */
    private void updateScene() {
        if (gameStateManager.isNotPlaying()) {
            return; // Do not update if the game is not in PLAYING state
        }

        spawnEnemyUnits(); // Spawn enemy units periodically
        updateActors(); // Update the positions of all active actors
        generateEnemyFire(); // Generate enemy projectiles
        updateNumberOfEnemies(); // Update the enemy count
        handleEnemyPenetration(); // Check for enemies that penetrate defenses
        removeAllDestroyedActors(); // Remove destroyed actors from the scene

        // Handle collisions
        collisionManager.handleSpiderCollisions(List.of(user), enemyManager.getEnemies());
        projectileManager.handleCollisions(collisionManager, enemyManager.getEnemies());
        powerUpManager.handlePowerUpCollisions(collisionManager);

        updateKillCount(); // Update the kill count for the user
        updateLevelView(); // Update the level view (e.g., health display)
        checkIfGameOver(); // Check if the game is over
    }

    /**
     * Updates all active actors, including friendly units, enemies, projectiles, and power-ups.
     */
    private void updateActors() {
        friendlyUnits.forEach(ActiveActorDestructible::updateActor); // Update friendly units
        enemyManager.updateEnemies(); // Update enemy units
        projectileManager.updateProjectiles(); // Update projectiles
        powerUpManager.updatePowerUps(); // Update power-ups
    }

    /**
     * Removes all destroyed actors from their respective groups.
     */
    private void removeAllDestroyedActors() {
        removeDestroyedActors(friendlyUnits); // Remove destroyed friendly units
        enemyManager.removeDestroyedEnemies(); // Remove destroyed enemies
        projectileManager.removeDestroyedProjectiles(); // Remove destroyed projectiles
        powerUpManager.removeDestroyedPowerUps(); // Remove destroyed power-ups
    }

    /**
     * Removes destroyed actors from the specified list and the scene.
     *
     * @param actors The list of actors to clean up.
     */
    private void removeDestroyedActors(List<ActiveActorDestructible> actors) {
        List<ActiveActorDestructible> destroyedActors = actors.stream()
                .filter(ActiveActorDestructible::isDestroyed) // Filter destroyed actors
                .toList();

        root.getChildren().removeAll(destroyedActors); // Remove destroyed actors from the scene
        actors.removeAll(destroyedActors); // Remove destroyed actors from the list
    }

    /**
     * Handles enemies that have penetrated the player's defenses.
     * This typically results in the player taking damage and the enemy being destroyed.
     */
    private void handleEnemyPenetration() {
        for (ActiveActorDestructible enemy : enemyManager.getEnemies()) {
            if (enemyHasPenetratedDefenses(enemy)) {
                user.takeDamage(); // Player takes damage
                enemy.destroy(); // Destroy the enemy
            }
        }
    }

    /**
     * Updates the level view, such as the player's health display.
     */
    private void updateLevelView() {
        levelView.removeHearts(user.getHealth()); // Update the heart display based on player's health
    }

    /**
     * Updates the user's kill count based on the number of enemies destroyed.
     */
    private void updateKillCount() {
        int killedEnemies = currentNumberOfEnemies - enemyManager.getEnemyCount(); // Calculate kills
        for (int i = 0; i < killedEnemies; i++) {
            user.incrementKillCount(); // Increment kill count for the user
        }
    }

    /**
     * Adds a projectile fired by the user to the scene and tracks it.
     *
     * @param projectile The projectile to add.
     */
    public void addProjectile(ActiveActorDestructible projectile) {
        projectileManager.addUserProjectile(projectile); // Add user projectile
    }


    /**
     * Generates projectiles fired by enemies.
     */
    private void generateEnemyFire() {
        projectileManager.generateEnemyProjectiles(enemyManager.getEnemies()); // Generate enemy projectiles
    }

    /**
     * Checks if an enemy has penetrated the player's defenses.
     *
     * @param enemy The enemy to check.
     * @return True if the enemy has penetrated defenses, false otherwise.
     */
    private boolean enemyHasPenetratedDefenses(ActiveActorDestructible enemy) {
        return Math.abs(enemy.getTranslateX()) > screenWidth; // Check if enemy is off-screen
    }

    /**
     * Retrieves the instance of the SoundManager for managing sound effects and music.
     *
     * @return The {@link SoundManager} instance used in the level.
     */
    public SoundManager getSoundManager() {
        return soundManager;
    }

    /**
     * Retrieves the instance of the user's character (UserSuperman).
     *
     * @return The {@link UserSuperman} instance representing the player's character.
     */
    public UserSuperman getUser() {
        return user;
    }

    /**
     * Retrieves the root {@link Group} for the current level.
     * This group contains all the visual elements of the level.
     *
     * @return The root {@link Group} of the current level.
     */
    public Group getRoot() {
        return root;
    }


    /**
     * Retrieves the maximum Y-coordinate position that enemies can reach before being considered out of bounds.
     *
     * @return The maximum Y-coordinate for enemy movement.
     */
    protected double getEnemyMaximumYPosition() {
        return enemyMaximumYPosition;
    }

    /**
     * Retrieves the width of the game screen.
     *
     * @return The width of the screen in pixels.
     */
    public double getScreenWidth() {
        return screenWidth;
    }

    /**
     * Retrieves the height of the game screen.
     *
     * @return The height of the screen in pixels.
     */
    public double getScreenHeight() {
        return screenHeight;
    }

    /**
     * Checks if the user's character (UserSuperman) has been destroyed.
     *
     * @return {@code true} if the user is destroyed; {@code false} otherwise.
     */
    protected boolean userIsDestroyed() {
        return user.isDestroyed();
    }


    /**
     * Updates the number of enemies currently in the level.
     */
    private void updateNumberOfEnemies() {
        currentNumberOfEnemies = enemyManager.getEnemyCount(); // Update enemy count
    }

    /**
     * Cleans up resources and unregisters observers.
     * This is typically called when transitioning between levels or exiting the game.
     */
    public void cleanup() {
        gameStateManager.deleteObserver(this); // Unregister from GameStateManager
        uiManager.cleanup(); // Cleanup UIManager
        System.out.println("LevelParent cleaned up and observers unregistered.");
    }


}