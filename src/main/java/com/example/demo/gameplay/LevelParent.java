package com.example.demo.gameplay;

import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.actors.FighterSpider;
import com.example.demo.actors.UserSuperman;
import com.example.demo.actors.BossSpider;
import com.example.demo.controller.Main;
import com.example.demo.powerups.PowerUp;
import com.example.demo.powerups.SpreadshotPowerUp;
import com.example.demo.ui.EndGameMenu;
import com.example.demo.ui.LevelView;
import com.example.demo.ui.MainMenu;
import com.example.demo.ui.PauseMenu;
import com.example.demo.ui.UIManager;
import com.example.demo.utils.SoundManager;
import com.example.demo.utils.CollisionManager;
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
	private final UserSuperman user;
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
	private final CollisionManager collisionManager;
	private final UIManager uiManager;




// ======================= Initialization Methods ==========================
//Methods responsible for initializing the level and its components.

	/**
	 * Constructs a LevelParent object and initializes its core components.
	 *
	 * @param backgroundImageName Path to the background image for the level.
	 * @param screenHeight        Height of the game screen.
	 * @param screenWidth         Width of the game screen.
	 * @param playerInitialHealth Initial health of the player's superman.
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
		this.user = new UserSuperman(this, playerInitialHealth);
		this.soundManager = SoundManager.getInstance();
		this.collisionManager = new CollisionManager(user, soundManager);
		this.uiManager = new UIManager(this, menuLayer, screenWidth, screenHeight, stage);

		// Initialize screen dimensions and background
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
		this.enemyMaximumYPosition = screenHeight - SCREEN_HEIGHT_ADJUSTMENT;

		this.background = new ImageView(new Image(getClass().getResource(backgroundImageName).toExternalForm()));
		this.stage = stage;
		this.currentLevel = levelName; // Initialize with the provided level name
		this.levelView = instantiateLevelView();

		this.currentNumberOfEnemies = 0;
		this.isPaused = false;

		// Add background and initialize UI components
		this.root.getChildren().add(menuLayer);  // Add menu layer after UI is initialized
		menuLayer.toBack();                 // Move menu layer to the back

		// Initialize lists for actors and game elements
		this.friendlyUnits = new ArrayList<>();
		this.enemyUnits = new ArrayList<>();
		this.userProjectiles = new ArrayList<>();
		this.enemyProjectiles = new ArrayList<>();
		this.powerUps = new ArrayList<>();

		// Initialize game elements
		initializeTimeline();
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
		uiManager.initializeUI();           // Initialize UI (pause button and menus)
		initializeFriendlyUnits();
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
	public void togglePause() {
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
		timeline.pause();
		if (gameBackgroundMediaPlayer != null) {
			gameBackgroundMediaPlayer.pause();
		}
		isPaused = true;
		uiManager.getPauseButton().setVisible(false);
		uiManager.getPauseMenu().setVisible(true);
		menuLayer.toFront();
	}



	/**
	 * Resumes the game by restarting the game loop and hiding the pause menu.
	 */
	public void resumeGame() {
		timeline.play();
		if (gameBackgroundMediaPlayer != null && !soundManager.isMusicMuted()) {
			gameBackgroundMediaPlayer.play();
		}
		isPaused = false;
		uiManager.getPauseButton().setVisible(true);
		uiManager.getPauseMenu().setVisible(false);
		background.requestFocus();
	}



	/**
	 * Handles the logic for when the player wins the game.
	 * Displays a win message and transitions to the end-game menu.
	 */
	protected void winGame() {
		timeline.stop(); // Stop the game loop
		levelView.showWinImage(); // Show the win image

		if (uiManager.getPauseButton() != null) {
			uiManager.getPauseButton().setVisible(false); // Hide the pause button
		}

		soundManager.playSound("win"); // Play the win sound

		// Delay to show the win image before transitioning to the end-game menu
		PauseTransition delay = new PauseTransition(Duration.seconds(3));
		delay.setOnFinished(e -> {
			levelView.removeWinImage(); // Remove the win image
			uiManager.getEndGameMenu().show(true); // Show the end-game menu with "You Win!"
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

		if (uiManager.getPauseButton() != null) {
			uiManager.getPauseButton().setVisible(false); // Hide the pause button
		}

		soundManager.playSound("gameOver"); // Play the game-over sound

		// Delay to show the game-over image before transitioning to the end-game menu
		PauseTransition delay = new PauseTransition(Duration.seconds(3));
		delay.setOnFinished(e -> {
			levelView.removeGameOverImage(); // Remove the game-over image
			uiManager.getEndGameMenu().show(false); // Show the end-game menu with "Game Over"
			menuLayer.toFront(); // Bring the menu layer to the front
		});
		delay.play();
	}



	/**
	 * Transitions to the main menu, stopping all gameplay components and music.
	 *
	 * @param stage The game stage.
	 */
	public void goToMainMenu(Stage stage) {
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
	 * Handles key press events to control the user's superman and actions.
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
	 * Handles key release events to stop the user's superman movements.
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
		if (isPaused) return;

		spawnEnemyUnits();
		updateActors();
		generateEnemyFire();
		updateNumberOfEnemies();
		handleEnemyPenetration(); // Keep this logic here for now
		removeAllDestroyedActors();

		// Replace CollisionManager calls with inline logic
		collisionManager.handleSpiderCollisions(friendlyUnits, enemyUnits);
		collisionManager.handleUserProjectileCollisions(userProjectiles, enemyUnits);
		collisionManager.handleEnemyProjectileCollisions(enemyProjectiles);
		collisionManager.handlePowerUpCollisions(powerUps);


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
	 * Fires a projectile from the user's superman and adds it to the list of user projectiles.
	 */
	private void fireProjectile() {
		// Get the projectile from the user superman
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
	 * Generates enemy projectiles by allowing fighter spiders to fire.
	 */
	private void generateEnemyFire() {
		enemyUnits.stream()
				.filter(enemy -> enemy instanceof FighterSpider)
				.map(enemy -> ((FighterSpider) enemy).fireProjectile())
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
	 * Retrieves the player's UserSuperman instance.
	 *
	 * @return The user's superman.
	 */
	public UserSuperman getUser() {
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
	 * Checks if the user's superman is destroyed.
	 *
	 * @return True if the user's superman is destroyed, false otherwise.
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