package com.example.demo.gameplay.levels;

import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.actors.UserSuperman;
import com.example.demo.controller.Main;
import com.example.demo.gameplay.GameStateManager;
import com.example.demo.gameplay.managers.PowerUpManager;
import com.example.demo.powerups.PowerUp;
import com.example.demo.ui.EndGameMenu;
import com.example.demo.ui.LevelView;
import com.example.demo.ui.MainMenu;
import com.example.demo.ui.PauseMenu;
import com.example.demo.ui.UIManager;
import com.example.demo.managers.SoundManager;
import com.example.demo.managers.CollisionManager;
import com.example.demo.managers.InputHandler;
import com.example.demo.managers.ProjectileManager;
import com.example.demo.managers.EnemyManager;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.stream.Collectors;


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
	protected final CollisionManager collisionManager;
	private final UIManager uiManager;
	private final GameStateManager gameStateManager;
	protected final PowerUpManager powerUpManager;
	private InputHandler inputHandler;
	protected final ProjectileManager projectileManager;
	protected final EnemyManager enemyManager;


	public LevelParent(String backgroundImageName, double screenHeight, double screenWidth,
					   int playerInitialHealth, Stage stage, String levelName) {
		this.menuLayer = new Group();
		this.root = new Group();
		this.scene = new Scene(root, screenWidth, screenHeight);
		this.timeline = new Timeline();
		this.user = new UserSuperman(this, playerInitialHealth);
		this.soundManager = SoundManager.getInstance();
		this.collisionManager = new CollisionManager(user, soundManager);
		this.gameStateManager = GameStateManager.getInstance();
		this.powerUpManager = PowerUpManager.getInstance();
		this.powerUpManager.initialize(root); // Pass the root for initialization
		this.inputHandler = new InputHandler(user, gameStateManager);
		this.projectileManager = ProjectileManager.getInstance();
		this.projectileManager.initialize(root); // Initialize with the current level's root
		this.enemyManager = EnemyManager.getInstance();
		this.enemyManager.initialize(root); // Set the root for the current level


		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
		this.enemyMaximumYPosition = screenHeight - SCREEN_HEIGHT_ADJUSTMENT;

		this.background = new ImageView(new Image(getClass().getResource(backgroundImageName).toExternalForm()));
		this.stage = stage;
		this.currentLevel = levelName;
		this.levelView = instantiateLevelView();

		this.currentNumberOfEnemies = 0;
		this.isPaused = false;

		this.root.getChildren().add(menuLayer);
		menuLayer.toBack();

		this.friendlyUnits = new ArrayList<>();
		this.powerUps = new ArrayList<>();
		this.uiManager = UIManager.getInstance(this, menuLayer, screenWidth, screenHeight, stage);

		initializeTimeline();
		friendlyUnits.add(user);
		initializeGameBackgroundMusic();
	}

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

	private void initializeBackground() {
		// Configure background appearance
		background.setFocusTraversable(true);
		background.setFitHeight(screenHeight);
		background.setFitWidth(screenWidth);

		// Delegate key handling to InputHandler
		background.setOnKeyPressed(event -> inputHandler.handleKeyPress(event));
		background.setOnKeyReleased(event -> inputHandler.handleKeyRelease(event));

		// Add the background to the root group
		root.getChildren().add(background);
	}

	private void initializeTimeline() {
		timeline.setCycleCount(Timeline.INDEFINITE);
		KeyFrame gameLoop = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> updateScene());
		timeline.getKeyFrames().add(gameLoop);
	}
	public ProjectileManager getProjectileManager() {
		return projectileManager;
	}

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

	public void startGame(String levelName) {
		gameStateManager.setCurrentState(GameStateManager.GameState.INITIALIZING); // Set state to initializing
		showLevelInfo(currentLevel);

		// Transition to PLAYING state after level info is shown
		PauseTransition pause = new PauseTransition(Duration.seconds(1));
		pause.setOnFinished(event -> {
			gameStateManager.setCurrentState(GameStateManager.GameState.PLAYING);
			timeline.play(); // Start the game loop
			background.requestFocus(); // Ensure the game is focused
		});
		pause.play();
	}

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

	public void goToNextLevel(String levelName) {
		gameStateManager.setCurrentState(GameStateManager.GameState.LOADING);
		currentLevel = levelName;

		ProjectileManager.getInstance().clearAllProjectiles();
		EnemyManager.getInstance().clearAllEnemies(); // Clear enemies for the next level
		PowerUpManager.getInstance().clearAllPowerUps(); // Clear power-ups for the next level
		stopGameBackgroundMusic();
		timeline.stop();
		root.getChildren().clear();

		// Reset and reinitialize UIManager
		UIManager.resetInstance();
		uiManager.initializeUI();

		setChanged();
		notifyObservers(levelName);

		gameStateManager.setCurrentState(GameStateManager.GameState.INITIALIZING);
	}

	public void togglePause() {
		if (isPaused) {
			resumeGame();
		} else {
			pauseGame();
		}
	}

	private void pauseGame() {
		gameStateManager.setCurrentState(GameStateManager.GameState.PAUSED); // Update state
		timeline.pause();
		if (gameBackgroundMediaPlayer != null) {
			gameBackgroundMediaPlayer.pause();
		}
		isPaused = true;
		uiManager.getPauseButton().setVisible(false);
		uiManager.getPauseMenu().setVisible(true);
		menuLayer.toFront();
	}

	public void resumeGame() {
		gameStateManager.setCurrentState(GameStateManager.GameState.PLAYING); // Update state
		timeline.play();
		if (gameBackgroundMediaPlayer != null && !soundManager.isMusicMuted()) {
			gameBackgroundMediaPlayer.play();
		}
		isPaused = false;
		uiManager.getPauseButton().setVisible(true);
		uiManager.getPauseMenu().setVisible(false);
		background.requestFocus();
	}

	protected void winGame() {
		gameStateManager.setCurrentState(GameStateManager.GameState.WIN); // Update state
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

	protected void loseGame() {
		gameStateManager.setCurrentState(GameStateManager.GameState.GAME_OVER); // Update state
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

	public void goToMainMenu(Stage stage) {
		// Stop game-related processes
		timeline.stop();
		stopGameBackgroundMusic();
		root.getChildren().clear(); // Clear all game components from the scene

		// Clear power-ups
		PowerUpManager.getInstance().clearAllPowerUps();
		ProjectileManager.getInstance().clearAllProjectiles();

		// Reset UIManager to prepare for new game
		UIManager.resetInstance();

		// Initialize and display the main menu
		MainMenu mainMenu = new MainMenu();
		mainMenu.start(stage, new Main());
	}

	private void stopGameBackgroundMusic() {
		if (gameBackgroundMediaPlayer != null) {
			gameBackgroundMediaPlayer.stop();
		}
	}

	private void updateScene() {
		if (!gameStateManager.isPlaying()) return;

		spawnEnemyUnits();
		updateActors();
		generateEnemyFire();
		updateNumberOfEnemies();
		handleEnemyPenetration();
		removeAllDestroyedActors();

		// Handle collisions
		collisionManager.handleSpiderCollisions(List.of(user), enemyManager.getEnemies());
		projectileManager.handleCollisions(collisionManager, enemyManager.getEnemies());
		powerUpManager.handlePowerUpCollisions(collisionManager);

		updateKillCount();
		updateLevelView();
		checkIfGameOver();
	}

	private void updateActors() {
		friendlyUnits.forEach(ActiveActorDestructible::updateActor);
		enemyManager.updateEnemies();
		projectileManager.updateProjectiles();
		powerUpManager.updatePowerUps();
	}

	private void removeAllDestroyedActors() {
		removeDestroyedActors(friendlyUnits);
		enemyManager.removeDestroyedEnemies();
		projectileManager.removeDestroyedProjectiles();
		powerUpManager.removeDestroyedPowerUps();
	}

	private void removeDestroyedActors(List<ActiveActorDestructible> actors) {
		List<ActiveActorDestructible> destroyedActors = actors.stream()
				.filter(ActiveActorDestructible::isDestroyed)
				.collect(Collectors.toList());

		root.getChildren().removeAll(destroyedActors); // Remove from the scene
		actors.removeAll(destroyedActors);            // Remove from the list
	}

	private void handleEnemyPenetration() {
		for (ActiveActorDestructible enemy : enemyManager.getEnemies()) {
			if (enemyHasPenetratedDefenses(enemy)) {
				user.takeDamage();
				enemy.destroy();
			}
		}
	}

	private void updateLevelView() {
		levelView.removeHearts(user.getHealth());
	}

	private void updateKillCount() {
		int killedEnemies = currentNumberOfEnemies - enemyManager.getEnemyCount();
		for (int i = 0; i < killedEnemies; i++) {
			user.incrementKillCount();
		}
	}


	public void addProjectile(ActiveActorDestructible projectile) {
		projectileManager.addUserProjectile(projectile);
	}

	protected void addEnemyUnit(ActiveActorDestructible enemy) {
		enemyManager.addEnemy(enemy);
	}


	protected void addPowerUp(PowerUp powerUp) {
		powerUpManager.addPowerUp(powerUp);
	}

	private void generateEnemyFire() {
		projectileManager.generateEnemyProjectiles(enemyManager.getEnemies());
	}


	private boolean enemyHasPenetratedDefenses(ActiveActorDestructible enemy) {
		return Math.abs(enemy.getTranslateX()) > screenWidth;
	}

	public SoundManager getSoundManager() {
		return soundManager;
	}

	public UserSuperman getUser() {
		return user;
	}

	public Group getRoot() {
		return root;
	}

	protected int getCurrentNumberOfEnemies() {
		return enemyManager.getEnemyCount();
	}

	protected double getEnemyMaximumYPosition() {
		return enemyMaximumYPosition;
	}

	public double getScreenWidth() {
		return screenWidth;
	}

	public double getScreenHeight() {
		return screenHeight;
	}

	protected boolean userIsDestroyed() {
		return user.isDestroyed();
	}

	private void updateNumberOfEnemies() {
		currentNumberOfEnemies = enemyManager.getEnemyCount();
	}

}