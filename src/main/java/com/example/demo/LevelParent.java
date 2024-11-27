package com.example.demo;

import java.util.*;
import java.util.stream.Collectors;

import javafx.animation.*;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.text.Font;
import javafx.util.Duration;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import com.example.demo.controller.Main;




public abstract class LevelParent extends Observable {

	private static final double SCREEN_HEIGHT_ADJUSTMENT = 150;
	private static final int MILLISECOND_DELAY = 50;
	private final double screenHeight;
	private final double screenWidth;
	private final double enemyMaximumYPosition;

	private final Group root;
	private final Group menuLayer; // New layer for menus

	private final Stage stage; // Store the Stage reference


	private final Timeline timeline;
	private final UserPlane user;
	private final Scene scene;
	private final ImageView background;

	private final List<ActiveActorDestructible> friendlyUnits;
	private final List<ActiveActorDestructible> enemyUnits;
	private final List<ActiveActorDestructible> userProjectiles;
	private final List<ActiveActorDestructible> enemyProjectiles;
	private final List<ActiveActorDestructible> powerUps;

	private int currentNumberOfEnemies;
	private LevelView levelView;

	private Button pauseButton; // Pause/Resume button
	private boolean isPaused;   // Track if the game is paused

	private SoundManager soundManager;

	private PauseMenu pauseMenu;
	private EndGameMenu endGameMenu;





	public LevelParent(String backgroundImageName, double screenHeight, double screenWidth, int playerInitialHealth, Stage stage) {
		this.menuLayer = new Group();
		this.stage = stage; // Initialize stage
		this.root = new Group();
		this.scene = new Scene(root, screenWidth, screenHeight);
		this.timeline = new Timeline();
		this.user = new UserPlane(this, playerInitialHealth);
		this.friendlyUnits = new ArrayList<>();
		this.enemyUnits = new ArrayList<>();
		this.userProjectiles = new ArrayList<>();
		this.enemyProjectiles = new ArrayList<>();
		this.powerUps = new ArrayList<>();
		this.soundManager = new SoundManager();
		this.root.getChildren().add(menuLayer);
		menuLayer.toFront();


		this.background = new ImageView(new Image(getClass().getResource(backgroundImageName).toExternalForm()));
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
		this.enemyMaximumYPosition = screenHeight - SCREEN_HEIGHT_ADJUSTMENT;
		this.levelView = instantiateLevelView();
		this.currentNumberOfEnemies = 0;
		this.isPaused = false;

		initializeTimeline();
		initializePauseButton(); // Add pause button initialization
		friendlyUnits.add(user);
	}

	protected abstract void initializeFriendlyUnits();
	protected abstract void checkIfGameOver();
	protected abstract void spawnEnemyUnits();
	protected abstract LevelView instantiateLevelView();

	public Scene initializeScene(Stage stage) {
		initializeBackground();
		initializeFriendlyUnits();
		initializePauseButton();
		initializePauseMenu();// Initialize pause menu
		initializeEndGameMenu(); // EndGameMenu setup

		levelView.showHeartDisplay();
		return scene;
	}



	public void startGame() {
		background.requestFocus();
		timeline.play();
	}

	public void goToNextLevel(String levelName) {
		timeline.stop(); // Stop the current game loop.
		root.getChildren().clear(); // Clear all nodes from the scene to release resources.

		// Notify observers to switch to the next level.
		setChanged();
		notifyObservers(levelName);
	}



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
	private void initializeTimeline() {
		timeline.setCycleCount(Timeline.INDEFINITE);
		KeyFrame gameLoop = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> updateScene());
		timeline.getKeyFrames().add(gameLoop);
	}

	private void initializeBackground() {
		background.setFocusTraversable(true);
		background.setFitHeight(screenHeight);
		background.setFitWidth(screenWidth);
		background.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent e) {
				if (!isPaused) {
					KeyCode kc = e.getCode();
					switch (kc) {
						case UP:
							user.moveUp();
							break;
						case DOWN:
							user.moveDown();
							break;
						case LEFT:
							user.moveLeft();
							break;
						case RIGHT:
							user.moveRight();
							break;
						case SPACE:
							fireProjectile();
							break;
						default:
							break;
					}
				}
			}
		});
		background.setOnKeyReleased(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent e) {
				if (!isPaused) {
					KeyCode kc = e.getCode();
					switch (kc) {
						case UP:
						case DOWN:
							user.stopVertical();
							break;
						case LEFT:
						case RIGHT:
							user.stopHorizontal();
							break;
						default:
							break;
					}
				}
			}
		});
		root.getChildren().add(background);
	}

	private void fireProjectile() {
		ActiveActorDestructible projectile = getUser().fireProjectile();
		// Add the returned projectile to the scene (already handled in UserPlane)
		userProjectiles.add(projectile);
	}



	public void addProjectile(ActiveActorDestructible projectile) {
		if (!userProjectiles.contains(projectile)) {
			getRoot().getChildren().add(projectile); // Add to the scene graph
			userProjectiles.add(projectile);         // Track the projectile
		}
	}





	private void initializePauseButton() {
		pauseButton = new Button("Pause");
		pauseButton.setFont(Font.font("Arial", 14));
		pauseButton.setStyle("-fx-background-color: orange; -fx-text-fill: white; -fx-background-radius: 10;");
		pauseButton.setOnAction(e -> togglePause());

		// Position the button
		pauseButton.setLayoutX(screenWidth - 100);
		pauseButton.setLayoutY(20);

		root.getChildren().add(pauseButton); // Add to the scene graph
	}

	private void initializePauseMenu() {
		pauseMenu = new PauseMenu(
				screenWidth,
				screenHeight,
				this::resumeGame, // Resume logic
				() -> restartLevel(stage), // Restart level logic
				() -> goToMainMenu(stage)  // Main menu logic
		);
		menuLayer.getChildren().add(pauseMenu); // Add to menuLayer
	}

	private void initializeEndGameMenu() {
		endGameMenu = new EndGameMenu(
				screenWidth,
				screenHeight,
				() -> restartLevel(stage), // Restart logic
				() -> goToMainMenu(stage)  // Main menu logic
		);
		menuLayer.getChildren().add(endGameMenu); // Add to menuLayer
	}





	private void togglePause() {
		if (isPaused) {
			resumeGame(); // Call resume logic
		} else {
			pauseGame(); // Call pause logic
		}
	}




	protected void winGame() {
		timeline.stop();
		levelView.showWinImage();
		soundManager.playSound("win");

		javafx.animation.PauseTransition delay = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(3));
		delay.setOnFinished(e -> {
			levelView.removeWinImage();
			endGameMenu.show(true); // Show endgame menu with "You Win!"
			menuLayer.toFront(); // Bring menuLayer to front
		});
		delay.play();
	}


	private void pauseGame() {
		timeline.pause(); // Pause the game loop
		isPaused = true; // Update pause state
		pauseButton.setText("Resume"); // Update button text
		pauseButton.setVisible(false); // Hide the pause button
		pauseMenu.setVisible(true); // Show the pause menu
		menuLayer.toFront(); // Bring menuLayer to front
	}

	protected void loseGame() {
		timeline.stop();
		levelView.showGameOverImage();
		soundManager.playSound("gameOver");

		javafx.animation.PauseTransition delay = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(3));
		delay.setOnFinished(e -> {
			levelView.removeGameOverImage();
			endGameMenu.show(false);
			menuLayer.toFront(); // Bring menuLayer to front
		});
		delay.play();
	}


	private void resumeGame() {
		timeline.play(); // Resume the game loop
		isPaused = false; // Update pause state
		pauseButton.setText("Pause"); // Update button text
		pauseButton.setVisible(true); // Ensure pause button is visible
		pauseMenu.setVisible(false); // Hide the pause menu
		background.requestFocus(); // Ensure the background gets focus again
	}


	private void goToMainMenu(Stage stage) {
		timeline.stop(); // Stop the game loop
		root.getChildren().clear(); // Clear all game components

		// Create and initialize the MainMenu
		MainMenu mainMenu = new MainMenu();
		mainMenu.start(stage, new Main()); // Pass stage and a new Main instance
	}




	private void generateEnemyFire() {
		for (ActiveActorDestructible enemy : enemyUnits) {
			if (enemy instanceof FighterPlane) {
				FighterPlane fighter = (FighterPlane) enemy;
				spawnEnemyProjectile(fighter.fireProjectile());
			}
		}
	}

	private void spawnEnemyProjectile(ActiveActorDestructible projectile) {
		if (projectile != null) {
			root.getChildren().add(projectile);
			enemyProjectiles.add(projectile);
		}
	}

	private void updateActors() {
		friendlyUnits.forEach(plane -> plane.updateActor());
		enemyUnits.forEach(enemy -> enemy.updateActor());
		userProjectiles.forEach(projectile -> projectile.updateActor());
		enemyProjectiles.forEach(projectile -> projectile.updateActor());
		powerUps.forEach(powerUp -> powerUp.updateActor());
	}

	private void removeAllDestroyedActors() {
		removeDestroyedActors(friendlyUnits);
		removeDestroyedActors(enemyUnits);
		removeDestroyedActors(userProjectiles);
		removeDestroyedActors(enemyProjectiles);
		removeDestroyedActors(powerUps);
	}

	private void removeDestroyedActors(List<ActiveActorDestructible> actors) {
		List<ActiveActorDestructible> destroyedActors = actors.stream().filter(actor -> actor.isDestroyed())
				.collect(Collectors.toList());
		root.getChildren().removeAll(destroyedActors);
		actors.removeAll(destroyedActors);
	}

	private void handlePlaneCollisions() {
		handleCollisions(friendlyUnits, enemyUnits);
	}

	private void handleUserProjectileCollisions() {
		handleCollisions(userProjectiles, enemyUnits);
	}

	private void handleEnemyProjectileCollisions() {
		for (ActiveActorDestructible projectile : enemyProjectiles) {
			if (projectile.getBoundsInParent().intersects(user.getBoundsInParent())) {
				projectile.takeDamage();
				user.takeDamage();
			}
		}
	}

	private void handleCollisions(List<ActiveActorDestructible> projectiles, List<ActiveActorDestructible> enemies) {
		for (ActiveActorDestructible projectile : projectiles) {
			for (ActiveActorDestructible enemy : enemies) {
				if (enemy instanceof Boss) {
					Boss boss = (Boss) enemy;
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

	private void handleEnemyPenetration() {
		for (ActiveActorDestructible enemy : enemyUnits) {
			if (enemyHasPenetratedDefenses(enemy)) {
				user.takeDamage();
				enemy.destroy();
			}
		}
	}

	private void handlePowerUpCollisions() {
		for (ActiveActorDestructible powerUp : powerUps) {
			if (powerUp.getBoundsInParent().intersects(user.getBoundsInParent())) {
				if (powerUp instanceof SpreadshotPowerUp) {
					((SpreadshotPowerUp) powerUp).activate(user); // Activate spreadshot
				} else if (powerUp instanceof PowerUp) {
					((PowerUp) powerUp).activate(user); // Activate default power-ups
				}

				// Play power-up collection sound
				soundManager.playPowerUpSound();

				powerUp.destroy(); // Remove power-up after collection
			}
		}
	}


	private void updateLevelView() {
		levelView.removeHearts(user.getHealth());
	}

	private void updateKillCount() {
		for (int i = 0; i < currentNumberOfEnemies - enemyUnits.size(); i++) {
			user.incrementKillCount();
		}
	}

	private boolean enemyHasPenetratedDefenses(ActiveActorDestructible enemy) {
		return Math.abs(enemy.getTranslateX()) > screenWidth;
	}
	public SoundManager getSoundManager() {
		return soundManager;
	}



	private void restartLevel(Stage stage) {
		timeline.stop();
		root.getChildren().clear(); // Clear all nodes from the current scene
		stage.setScene(initializeScene(stage)); // Reload the current level
		startGame(); // Start the game again
	}




	protected UserPlane getUser() {
		return user;
	}

	protected Group getRoot() {
		return root;
	}

	protected int getCurrentNumberOfEnemies() {
		return enemyUnits.size();
	}

	protected void addEnemyUnit(ActiveActorDestructible enemy) {
		enemyUnits.add(enemy);
		root.getChildren().add(enemy);
	}

	protected void addPowerUp(PowerUp powerUp) {
		powerUps.add(powerUp);
		root.getChildren().add(powerUp);
	}

	protected double getEnemyMaximumYPosition() {
		return enemyMaximumYPosition;
	}

	protected double getScreenWidth() {
		return screenWidth;
	}

	protected boolean userIsDestroyed() {
		return user.isDestroyed();
	}

	private void updateNumberOfEnemies() {
		currentNumberOfEnemies = enemyUnits.size();
	}
}