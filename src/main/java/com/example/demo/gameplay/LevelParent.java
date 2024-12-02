package com.example.demo.gameplay;

import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.actors.Boss;
import com.example.demo.actors.FighterPlane;
import com.example.demo.actors.UserPlane;
import com.example.demo.controller.Main;
import com.example.demo.powerups.PowerUp;
import com.example.demo.powerups.SpreadshotPowerUp;
import com.example.demo.ui.EndGameMenu;
import com.example.demo.ui.LevelView;
import com.example.demo.ui.MainMenu;
import com.example.demo.ui.PauseMenu;
import com.example.demo.utils.SoundManager;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.stream.Collectors;




/**
 * Abstract class representing the core structure of a game level.
 * Manages gameplay elements such as actors, background, menus, and transitions.
 */
public abstract class LevelParent extends Observable {

	// Constants for screen adjustments and game loop timing
	private static final double SCREEN_HEIGHT_ADJUSTMENT = 150;
	private static final int MILLISECOND_DELAY = 50;

	// Screen dimensions
	private final double screenHeight;
	private final double screenWidth;
	private final double enemyMaximumYPosition;

	// Root groups for UI elements and menus
	private final Group root;
	private final Group menuLayer;

	// Game stage and scene
	private final Stage stage;
	private final Scene scene;

	// Game elements and actors
	private final UserPlane user;
	private final ImageView background;
	private final List<ActiveActorDestructible> friendlyUnits;
	private final List<ActiveActorDestructible> enemyUnits;
	private final List<ActiveActorDestructible> userProjectiles;
	private final List<ActiveActorDestructible> enemyProjectiles;
	private final List<ActiveActorDestructible> powerUps;

	// Game state and controls
	private final Timeline timeline;
	private Button pauseButton;
	private boolean isPaused;
	private int currentNumberOfEnemies;

	// Level-related views and settings
	private LevelView levelView;
	private String currentLevel;

	// Audio and menus
	private final SoundManager soundManager;
	private PauseMenu pauseMenu;
	private EndGameMenu endGameMenu;
	private MediaPlayer gameBackgroundMediaPlayer;



// ======================= Initialization Methods ==========================
//Methods responsible for initializing the level and its components.

	/**
	 * Constructs a LevelParent object and initializes its core components.
	 *
	 * @param backgroundImageName Path to the background image for the level.
	 * @param screenHeight        Height of the game screen.
	 * @param screenWidth         Width of the game screen.
	 * @param playerInitialHealth Initial health of the player's plane.
	 * @param stage               The game stage.
	 * @param levelName           Name of the current level.
	 */
	public LevelParent(String backgroundImageName, double screenHeight, double screenWidth,
					   int playerInitialHealth, Stage stage, String levelName) {
		// Initialize core components
		this.menuLayer = new Group();
		this.root = new Group();
		this.scene = new Scene(root, screenWidth, screenHeight);
		this.timeline = new Timeline();
		this.user = new UserPlane(this, playerInitialHealth);

		// Initialize lists for actors and game elements
		this.friendlyUnits = new ArrayList<>();
		this.enemyUnits = new ArrayList<>();
		this.userProjectiles = new ArrayList<>();
		this.enemyProjectiles = new ArrayList<>();
		this.powerUps = new ArrayList<>();

		// Initialize screen dimensions and background
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
		this.enemyMaximumYPosition = screenHeight - SCREEN_HEIGHT_ADJUSTMENT;
		this.background = new ImageView(new Image(getClass().getResource(backgroundImageName).toExternalForm()));

		// Game state variables
		this.stage = stage;
		this.soundManager = SoundManager.getInstance();
		this.currentLevel = levelName; // Initialize with the provided level name
		this.levelView = instantiateLevelView();
		this.currentNumberOfEnemies = 0;
		this.isPaused = false;

		// Add menu layer to the root and initialize components
		this.root.getChildren().add(menuLayer);
		menuLayer.toFront();

		// Initialize game elements
		initializeTimeline();
		initializePauseButton();
		friendlyUnits.add(user);
		initializeGameBackgroundMusic();
	}


	/**
	 * Initializes the scene for the level by setting up the background, units, menus, and UI elements.
	 *
	 * @param stage The game stage.
	 * @return The initialized Scene object.
	 */
	public Scene initializeScene(Stage stage) {
		initializeBackground();
		initializeFriendlyUnits();
		initializePauseButton();
		initializePauseMenu();
		initializeEndGameMenu();
		initializeGameBackgroundMusic();

		// Display the player's heart display (health UI)
		levelView.showHeartDisplay();
		return scene;
	}

	// Abstract methods to be implemented by subclasses
	protected abstract void initializeFriendlyUnits();
	protected abstract LevelView instantiateLevelView();
	protected abstract void checkIfGameOver();
	protected abstract void spawnEnemyUnits();


	/**
	 * Initializes the game background and sets up keyboard controls.
	 * Configures key press and release actions for the user.
	 */
	private void initializeBackground() {
		// Configure background appearance
		background.setFocusTraversable(true);
		background.setFitHeight(screenHeight);
		background.setFitWidth(screenWidth);

		// Set up key press actions
		background.setOnKeyPressed(event -> handleKeyPress(event));

		// Set up key release actions
		background.setOnKeyReleased(event -> handleKeyRelease(event));

		// Add the background to the root group
		root.getChildren().add(background);
	}


	/**
	 * Initializes the game timeline for the game loop.
	 * Sets up a repeating keyframe to call `updateScene`.
	 */
	private void initializeTimeline() {
		timeline.setCycleCount(Timeline.INDEFINITE);
		KeyFrame gameLoop = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> updateScene());
		timeline.getKeyFrames().add(gameLoop);
	}


	/**
	 * Initializes the pause button.
	 * Configures its styling, position, and behavior, then adds it to the scene graph.
	 */
	private void initializePauseButton() {
		// Create and style the pause button
		pauseButton = new Button("Pause");
		pauseButton.setFont(Font.font("Arial", 14));
		pauseButton.setStyle("-fx-background-color: orange; -fx-text-fill: white; -fx-background-radius: 10;");

		// Set the action for toggling pause/resume
		pauseButton.setOnAction(e -> togglePause());

		// Position the button in the top-right corner
		pauseButton.setLayoutX(screenWidth - 100);
		pauseButton.setLayoutY(20);

		// Add the pause button to the scene graph
		root.getChildren().add(pauseButton);
	}


	/**
	 * Initializes the pause menu.
	 * Adds options to resume the game or navigate to the main menu.
	 */
	private void initializePauseMenu() {
		pauseMenu = new PauseMenu(
				screenWidth,
				screenHeight,
				this::resumeGame,        // Logic for resuming the game
				() -> goToMainMenu(stage) // Logic for navigating to the main menu
		);

		// Add the pause menu to the menu layer
		menuLayer.getChildren().add(pauseMenu);
	}


	/**
	 * Initializes the end-game menu.
	 * Adds options to navigate to the main menu or exit the application.
	 */
	private void initializeEndGameMenu() {
		endGameMenu = new EndGameMenu(
				screenWidth,
				screenHeight,
				() -> goToMainMenu(stage), // Logic for navigating to the main menu
				() -> System.exit(0)       // Logic for exiting the application
		);

		// Add the end-game menu to the menu layer
		menuLayer.getChildren().add(endGameMenu);
	}


	/**
	 * Initializes the game background music.
	 * Sets up a looping media player for the game.
	 */
	private void initializeGameBackgroundMusic() {
		try {
			Media gameMusic = new Media(getClass().getResource("/com/example/demo/sounds/Background.mp3").toExternalForm());
			gameBackgroundMediaPlayer = new MediaPlayer(gameMusic);
			gameBackgroundMediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop the game music
			gameBackgroundMediaPlayer.setVolume(0.6); // Set an appropriate volume level
		} catch (Exception e) {
			System.err.println("Error loading game background music: " + e.getMessage());
			e.printStackTrace();
		}
	}



// ===================== Game State Management ===========================
//Methods responsible for managing the game's state (e.g., pause, resume, transitions).

	/**
	 * Starts the game by displaying the level info and initiating gameplay.
	 *
	 * @param levelName The name of the level to start.
	 */
	public void startGame(String levelName) {
		showLevelInfo(currentLevel);
	}


	/**
	 * Displays the level information at the top of the screen before gameplay begins.
	 *
	 * @param levelName The name of the level to display.
	 */
	private void showLevelInfo(String levelName) {
		// Temporarily disable the pause button
		if (pauseButton != null) {
			pauseButton.setDisable(true);
		}

		// Determine the level-specific message
		String levelMessage = getLevelMessage(levelName);

		// Create and style a text label to show the level info
		Text levelInfo = createLevelInfoText(levelMessage);

		// Add the level info to the scene
		root.getChildren().add(levelInfo);

		// Display the message for a short duration before starting the game
		PauseTransition pause = new PauseTransition(Duration.seconds(1));
		pause.setOnFinished(event -> {
			root.getChildren().remove(levelInfo); // Remove the level info text
			background.requestFocus(); // Refocus on the game

			// Re-enable the pause button after the level info disappears
			if (pauseButton != null) {
				pauseButton.setDisable(false);
			}

			// Play background music if not muted
			if (!SoundManager.getInstance().isMusicMuted() && gameBackgroundMediaPlayer != null
					&& gameBackgroundMediaPlayer.getStatus() != MediaPlayer.Status.PLAYING) {
				gameBackgroundMediaPlayer.play();
			}

			// Start the game loop
			timeline.play();
		});
		pause.play();
	}


	/**
	 * Retrieves the level-specific message based on the level name.
	 *
	 * @param levelName The name of the level.
	 * @return A descriptive message for the level.
	 */
	private String getLevelMessage(String levelName) {
		switch (levelName) {
			case "Level 1":
				return "Level 1: Kill all the enemies!";
			case "Level 2":
				return "Level 2: Kill the boss!";
			case "Final Level":
				return "Level 3: Kill all the enemies and the boss!";
			default:
				return levelName; // Default to the provided level name
		}
	}


	/**
	 * Creates and styles the text for the level information.
	 *
	 * @param message The message to display.
	 * @return A styled Text object ready for display.
	 */
	private Text createLevelInfoText(String message) {
		Text levelInfo = new Text(message);
		levelInfo.setFont(Font.font("Arial", 30));
		levelInfo.setFill(Color.WHITE);
		levelInfo.setStroke(Color.BLACK);
		levelInfo.setStrokeWidth(1);

		// Center the text horizontally at the top of the screen
		levelInfo.setX(screenWidth / 2 - levelInfo.getLayoutBounds().getWidth() / 2);
		levelInfo.setY(50);

		return levelInfo;
	}


	/**
	 * Transitions to the next level by clearing the current level and notifying observers.
	 *
	 * @param levelName The name of the next level.
	 */
	public void goToNextLevel(String levelName) {
		currentLevel = levelName; // Update the current level name
		stopGameBackgroundMusic(); // Stop background music
		timeline.stop(); // Stop the game loop
		root.getChildren().clear(); // Clear all nodes from the scene

		// Notify observers to switch to the next level
		setChanged();
		notifyObservers(levelName);
	}


	/**
	 * Toggles the pause state of the game.
	 * If the game is paused, it resumes; otherwise, it pauses.
	 */
	private void togglePause() {
		if (isPaused) {
			resumeGame();
		} else {
			pauseGame();
		}
	}


	/**
	 * Pauses the game by stopping the game loop and displaying the pause menu.
	 */
	private void pauseGame() {
		timeline.pause(); // Pause the game loop

		if (gameBackgroundMediaPlayer != null) {
			gameBackgroundMediaPlayer.pause(); // Pause the background music
		}

		isPaused = true; // Set the pause state
		pauseButton.setText("Resume"); // Update the button text to "Resume"
		pauseButton.setVisible(false); // Hide the pause button
		pauseMenu.setVisible(true); // Show the pause menu
		menuLayer.toFront(); // Bring the menu layer to the front
	}


	/**
	 * Resumes the game by restarting the game loop and hiding the pause menu.
	 */
	private void resumeGame() {
		timeline.play(); // Resume the game loop

		if (gameBackgroundMediaPlayer != null) {
			// Resume the background music if it's not muted
			if (soundManager.isMusicMuted()) {
				gameBackgroundMediaPlayer.pause(); // Keep music paused if muted
			} else {
				gameBackgroundMediaPlayer.play(); // Play music if not muted
			}
		}

		isPaused = false; // Clear the pause state
		pauseButton.setText("Pause"); // Update the button text to "Pause"
		pauseButton.setVisible(true); // Make the pause button visible
		pauseMenu.setVisible(false); // Hide the pause menu
		background.requestFocus(); // Refocus on the game background
	}


	/**
	 * Handles the logic for when the player wins the game.
	 * Displays a win message and transitions to the end-game menu.
	 */
	protected void winGame() {
		timeline.stop(); // Stop the game loop
		levelView.showWinImage(); // Show the win image

		if (pauseButton != null) {
			pauseButton.setVisible(false); // Hide the pause button
		}

		soundManager.playSound("win"); // Play the win sound

		// Delay to show the win image before transitioning to the end-game menu
		PauseTransition delay = new PauseTransition(Duration.seconds(3));
		delay.setOnFinished(e -> {
			levelView.removeWinImage(); // Remove the win image
			endGameMenu.show(true); // Show the end-game menu with "You Win!"
			menuLayer.toFront(); // Bring the menu layer to the front
		});
		delay.play();
	}


	/**
	 * Handles the logic for when the player loses the game.
	 * Displays a game-over message and transitions to the end-game menu.
	 */
	protected void loseGame() {
		timeline.stop(); // Stop the game loop
		levelView.showGameOverImage(); // Show the game-over image

		if (pauseButton != null) {
			pauseButton.setVisible(false); // Hide the pause button
		}

		soundManager.playSound("gameOver"); // Play the game-over sound

		// Delay to show the game-over image before transitioning to the end-game menu
		PauseTransition delay = new PauseTransition(Duration.seconds(3));
		delay.setOnFinished(e -> {
			levelView.removeGameOverImage(); // Remove the game-over image
			endGameMenu.show(false); // Show the end-game menu with "Game Over"
			menuLayer.toFront(); // Bring the menu layer to the front
		});
		delay.play();
	}


	/**
	 * Transitions to the main menu, stopping all gameplay components and music.
	 *
	 * @param stage The game stage.
	 */
	private void goToMainMenu(Stage stage) {
		// Stop game-related processes
		timeline.stop();
		stopGameBackgroundMusic();
		root.getChildren().clear(); // Clear all game components from the scene

		// Initialize and display the main menu
		MainMenu mainMenu = new MainMenu();
		mainMenu.start(stage, new Main());
	}


	/**
	 * Stops the game background music if it's currently playing.
	 */
	private void stopGameBackgroundMusic() {
		if (gameBackgroundMediaPlayer != null) {
			gameBackgroundMediaPlayer.stop();
		}
	}



// ==================== Game Loop & Updates ==============================
//Methods responsible for the game's main loop and updating gameplay elements.


	/**
	 * Handles key press events to control the user's plane and actions.
	 *
	 * @param e The KeyEvent triggered by a key press.
	 */
	private void handleKeyPress(KeyEvent e) {
		if (isPaused) return;

		KeyCode keyCode = e.getCode();
		switch (keyCode) {
			case UP -> user.moveUp();
			case DOWN -> user.moveDown();
			case LEFT -> user.moveLeft();
			case RIGHT -> user.moveRight();
			case SPACE -> fireProjectile();
			default -> {
			}
		}
	}


	/**
	 * Handles key release events to stop the user's plane movements.
	 *
	 * @param e The KeyEvent triggered by a key release.
	 */
	private void handleKeyRelease(KeyEvent e) {
		if (isPaused) return;

		KeyCode keyCode = e.getCode();
		switch (keyCode) {
			case UP, DOWN -> user.stopVertical();
			case LEFT, RIGHT -> user.stopHorizontal();
			default -> {
			}
		}
	}


	/**
	 * Updates the game scene during each frame of the game loop.
	 * Handles spawning enemies, updating actors, and managing collisions.
	 */
	private void updateScene() {
		if (isPaused) return; // Skip updates if the game is paused

		spawnEnemyUnits();
		updateActors();
		generateEnemyFire();
		updateNumberOfEnemies();
		handleEnemyPenetration();
		removeAllDestroyedActors();
		handleUserProjectileCollisions();
		handleEnemyProjectileCollisions();
		handlePlaneCollisions();
		handlePowerUpCollisions(); // Handle power-up collection
		updateKillCount();
		updateLevelView();
		checkIfGameOver();
	}


	/**
	 * Updates all game actors, including friendly units, enemies, projectiles, and power-ups.
	 */
	private void updateActors() {
		friendlyUnits.forEach(ActiveActorDestructible::updateActor);
		enemyUnits.forEach(ActiveActorDestructible::updateActor);
		userProjectiles.forEach(ActiveActorDestructible::updateActor);
		enemyProjectiles.forEach(ActiveActorDestructible::updateActor);
		powerUps.forEach(ActiveActorDestructible::updateActor);
	}


	/**
	 * Removes all destroyed actors from the scene and their respective lists.
	 */
	private void removeAllDestroyedActors() {
		removeDestroyedActors(friendlyUnits);
		removeDestroyedActors(enemyUnits);
		removeDestroyedActors(userProjectiles);
		removeDestroyedActors(enemyProjectiles);
		removeDestroyedActors(powerUps);
	}


	/**
	 * Removes actors marked as destroyed from the specified list and the scene graph.
	 *
	 * @param actors The list of actors to check and remove if destroyed.
	 */
	private void removeDestroyedActors(List<ActiveActorDestructible> actors) {
		List<ActiveActorDestructible> destroyedActors = actors.stream()
				.filter(ActiveActorDestructible::isDestroyed)
				.collect(Collectors.toList());

		root.getChildren().removeAll(destroyedActors); // Remove from the scene
		actors.removeAll(destroyedActors);            // Remove from the list
	}


	/**
	 * Handles collisions between friendly and enemy planes.
	 */
	private void handlePlaneCollisions() {
		handleCollisions(friendlyUnits, enemyUnits);
	}


	/**
	 * Handles collisions between user projectiles and enemy units.
	 */
	private void handleUserProjectileCollisions() {
		handleCollisions(userProjectiles, enemyUnits);
	}


	/**
	 * Handles collisions between enemy projectiles and the user's plane.
	 */
	private void handleEnemyProjectileCollisions() {
		for (ActiveActorDestructible projectile : enemyProjectiles) {
			if (projectile.getBoundsInParent().intersects(user.getBoundsInParent())) {
				projectile.takeDamage();
				user.takeDamage();
			}
		}
	}


	/**
	 * Handles collisions between projectiles and enemies, including custom logic for bosses.
	 *
	 * @param projectiles The list of projectiles.
	 * @param enemies     The list of enemies.
	 */
	private void handleCollisions(List<ActiveActorDestructible> projectiles, List<ActiveActorDestructible> enemies) {
		for (ActiveActorDestructible projectile : projectiles) {
			for (ActiveActorDestructible enemy : enemies) {
				if (enemy instanceof Boss boss) { // Enhanced readability with pattern matching
					if (boss.getCustomHitbox().intersects(projectile.getBoundsInParent())) {
						projectile.takeDamage();
						boss.takeDamage();
					}
				} else if (enemy.getBoundsInParent().intersects(projectile.getBoundsInParent())) {
					projectile.takeDamage();
					enemy.takeDamage();
				}
			}
		}
	}


	/**
	 * Handles enemy penetration past the player's defenses.
	 * Damages the user and destroys the enemy.
	 */
	private void handleEnemyPenetration() {
		for (ActiveActorDestructible enemy : enemyUnits) {
			if (enemyHasPenetratedDefenses(enemy)) {
				user.takeDamage();
				enemy.destroy();
			}
		}
	}


	/**
	 * Handles collisions between the user's plane and power-ups.
	 * Activates the power-up and removes it from the game.
	 */
	private void handlePowerUpCollisions() {
		for (ActiveActorDestructible powerUp : powerUps) {
			if (powerUp.getBoundsInParent().intersects(user.getBoundsInParent())) {
				if (powerUp instanceof SpreadshotPowerUp spreadshotPowerUp) {
					spreadshotPowerUp.activate(user); // Activate spreadshot power-up
				} else if (powerUp instanceof PowerUp genericPowerUp) {
					genericPowerUp.activate(user); // Activate generic power-ups
				}

				soundManager.playPowerUpSound(); // Play collection sound
				powerUp.destroy(); // Remove power-up after collection
			}
		}
	}


	/**
	 * Updates the level view based on the user's current health.
	 */
	private void updateLevelView() {
		levelView.removeHearts(user.getHealth());
	}


	/**
	 * Updates the kill count for enemies eliminated in the level.
	 */
	private void updateKillCount() {
		int killedEnemies = currentNumberOfEnemies - enemyUnits.size();
		for (int i = 0; i < killedEnemies; i++) {
			user.incrementKillCount();
		}
	}



// ================= Actor & Projectile Management =====================
//Methods responsible for adding, removing, or interacting with actors and projectiles.

	/**
	 * Fires a projectile from the user's plane and adds it to the list of user projectiles.
	 */
	private void fireProjectile() {
		// Get the projectile from the user plane
		ActiveActorDestructible projectile = getUser().fireProjectile();

		// Add the projectile to the list for tracking
		if (projectile != null) {
			userProjectiles.add(projectile);
		}
	}


	/**
	 * Adds a projectile to the scene and tracks it in the appropriate list.
	 *
	 * @param projectile The projectile to be added to the scene.
	 */
	public void addProjectile(ActiveActorDestructible projectile) {
		if (projectile != null && !userProjectiles.contains(projectile)) {
			// Add the projectile to the scene graph
			getRoot().getChildren().add(projectile);

			// Track the projectile
			userProjectiles.add(projectile);
		}
	}


	/**
	 * Adds a new enemy unit to the level and scene graph.
	 *
	 * @param enemy The enemy unit to add.
	 */
	protected void addEnemyUnit(ActiveActorDestructible enemy) {
		enemyUnits.add(enemy);
		root.getChildren().add(enemy);
	}


	/**
	 * Adds a new power-up to the level and scene graph.
	 *
	 * @param powerUp The power-up to add.
	 */
	protected void addPowerUp(PowerUp powerUp) {
		powerUps.add(powerUp);
		root.getChildren().add(powerUp);
	}


	/**
	 * Generates enemy projectiles by allowing fighter planes to fire.
	 */
	private void generateEnemyFire() {
		enemyUnits.stream()
				.filter(enemy -> enemy instanceof FighterPlane)
				.map(enemy -> ((FighterPlane) enemy).fireProjectile())
				.forEach(this::spawnEnemyProjectile);
	}


	/**
	 * Adds a projectile to the scene and tracks it in the enemy projectile list.
	 *
	 * @param projectile The projectile to be spawned.
	 */
	private void spawnEnemyProjectile(ActiveActorDestructible projectile) {
		if (projectile != null) {
			root.getChildren().add(projectile); // Add projectile to the scene
			enemyProjectiles.add(projectile);   // Track the projectile
		}
	}



// ====================== Utility Methods ==============================
//Utility methods for gameplay logic and data retrieval.

	/**
	 * Checks if an enemy has penetrated the player's defenses.
	 *
	 * @param enemy The enemy actor to check.
	 * @return True if the enemy's position exceeds the screen width, false otherwise.
	 */
	private boolean enemyHasPenetratedDefenses(ActiveActorDestructible enemy) {
		return Math.abs(enemy.getTranslateX()) > screenWidth;
	}


	/**
	 * Retrieves the SoundManager instance for the game.
	 *
	 * @return The SoundManager instance.
	 */
	public SoundManager getSoundManager() {
		return soundManager;
	}


	/**
	 * Retrieves the player's UserPlane instance.
	 *
	 * @return The user's plane.
	 */
	public UserPlane getUser() {
		return user;
	}


	/**
	 * Retrieves the root group of the scene.
	 *
	 * @return The root group.
	 */
	public Group getRoot() {
		return root;
	}


	/**
	 * Gets the current number of enemy units in the level.
	 *
	 * @return The number of enemy units.
	 */
	protected int getCurrentNumberOfEnemies() {
		return enemyUnits.size();
	}


	/**
	 * Retrieves the maximum Y position for enemy units.
	 *
	 * @return The maximum Y position for enemies.
	 */
	protected double getEnemyMaximumYPosition() {
		return enemyMaximumYPosition;
	}


	/**
	 * Retrieves the width of the game screen.
	 *
	 * @return The screen width.
	 */
	public double getScreenWidth() {
		return screenWidth;
	}


	/**
	 * Retrieves the height of the game screen.
	 *
	 * @return The screen height.
	 */
	public double getScreenHeight() {
		return screenHeight;
	}


	/**
	 * Checks if the user's plane is destroyed.
	 *
	 * @return True if the user's plane is destroyed, false otherwise.
	 */
	protected boolean userIsDestroyed() {
		return user.isDestroyed();
	}


	/**
	 * Updates the number of enemies in the level.
	 */
	private void updateNumberOfEnemies() {
		currentNumberOfEnemies = enemyUnits.size();
	}
}