package com.example.demo.gameplay;

import com.example.demo.actors.enemies.EnemySpider;
import com.example.demo.ui.gameplayUI.LevelView;
import com.example.demo.managers.GameStateManager;
import com.example.demo.JavaFXInitializer;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

class LevelOneTest extends JavaFXInitializer {

    private LevelOne levelOne;
    private Stage testStage;
    private Group root;

    @BeforeAll
    static void initializeJavaFX() {
        Platform.startup(() -> {});
    }

    @BeforeEach
    void setUp() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            testStage = new Stage();
            levelOne = new LevelOne(600, 800, testStage);
            root = levelOne.getRoot();
            latch.countDown();
        });
        latch.await();
    }

    @AfterEach
    void tearDown() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            levelOne.cleanup();
            testStage.close();
            latch.countDown();
        });
        latch.await();
    }

    @Test
    void testInitialization() {
        Platform.runLater(() -> {
            Scene scene = levelOne.initializeScene(testStage);
            assertNotNull(scene, "Scene should not be null.");
            assertEquals(root, scene.getRoot(), "Scene root should match LevelOne's root.");
        });
    }

    @Test
    void testFriendlyUnitsInitialization() {
        Platform.runLater(() -> {
            levelOne.initializeFriendlyUnits();
            assertTrue(root.getChildren().contains(levelOne.getUser()), "Root should contain the user's superman.");
        });
    }

    @Test
    void testEnemySpawning() {
        Platform.runLater(() -> {
            levelOne.spawnEnemyUnits();
            assertEquals(5, levelOne.getEnemyManager().getEnemyCount(), "Enemy count should be 5 after spawning the first cycle.");

            levelOne.spawnEnemyUnits();
            assertEquals(5, levelOne.getEnemyManager().getEnemyCount(), "No new enemies should spawn until the current enemies are defeated.");

            levelOne.getEnemyManager().clearAllEnemies(); // Simulate defeating all enemies
            levelOne.spawnEnemyUnits();
            assertEquals(5, levelOne.getEnemyManager().getEnemyCount(), "Enemy count should increase to 5 after defeating all enemies.");
        });
    }

    @Test
    void testGameOverCondition() {
        Platform.runLater(() -> {
            levelOne.getUser().destroy(); // Simulate user destruction
            levelOne.checkIfGameOver();
            assertTrue(levelOne.getGameStateManager().getCurrentState() == GameStateManager.GameState.GAME_OVER, "Game state should be GAME_OVER if the user is destroyed.");
        });
    }

    @Test
    void testLevelViewInitialization() {
        Platform.runLater(() -> {
            LevelView levelView = levelOne.instantiateLevelView();
            assertNotNull(levelView, "LevelView should not be null.");
            assertEquals(5, levelView.getHealthDisplayCount(), "LevelView should initialize with the correct number of health displays.");
        });
    }
}
