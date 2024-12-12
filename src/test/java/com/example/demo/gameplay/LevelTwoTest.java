package com.example.demo.gameplay;

import com.example.demo.ui.gameplayUI.LevelView;
import com.example.demo.JavaFXInitializer;
import com.example.demo.managers.GameStateManager;
import javafx.scene.control.Label;
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

class LevelTwoTest extends JavaFXInitializer {

    private LevelTwo levelTwo;
    private Stage testStage;
    private Group root;

    @BeforeAll
    static void initializeJavaFX() {
        // Initialize the JavaFX platform
        Platform.startup(() -> {});
    }

    @BeforeEach
    void setUp() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            testStage = new Stage();
            levelTwo = new LevelTwo(600, 800, testStage);
            root = levelTwo.getRoot();
            latch.countDown();
        });
        latch.await();
    }

    @AfterEach
    void tearDown() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            levelTwo.cleanup(); // Assuming cleanup method exists in LevelParent
            testStage.close();
            latch.countDown();
        });
        latch.await();
    }

    @Test
    void testInitialization() {
        Platform.runLater(() -> {
            Scene scene = levelTwo.initializeScene(testStage);
            assertNotNull(scene, "Scene should not be null.");
            assertEquals(root, scene.getRoot(), "Scene root should match LevelTwo's root.");
        });
    }

    @Test
    void testFriendlyUnitsInitialization() {
        Platform.runLater(() -> {
            levelTwo.initializeFriendlyUnits();
            assertTrue(root.getChildren().contains(levelTwo.getUser()), "Root should contain the user's superman.");
        });
    }

    @Test
    void testEnemySpawning() {
        Platform.runLater(() -> {
            levelTwo.spawnEnemyUnits();
            assertEquals(1, levelTwo.getEnemyManager().getEnemyCount(), "Enemy count should be 1 after spawning the boss.");

            // Simulate defeating the boss
            levelTwo.getEnemyManager().clearAllEnemies();
            levelTwo.spawnEnemyUnits();
            assertEquals(1, levelTwo.getEnemyManager().getEnemyCount(), "Enemy count should be 1 after respawning the boss.");
        });
    }



    @Test
    void testLevelViewInitialization() {
        Platform.runLater(() -> {
            LevelView levelView = levelTwo.instantiateLevelView();
            assertNotNull(levelView, "LevelView should not be null.");
            assertEquals(5, levelView.getHealthDisplayCount(), "LevelView should initialize with the correct number of health displays.");
        });
    }

    @Test
    void testShieldAlertInitialization() {
        Platform.runLater(() -> {
            Label shieldAlert = (Label) root.getChildren().stream()
                    .filter(node -> node instanceof Label)
                    .findFirst()
                    .orElse(null);
            assertNotNull(shieldAlert, "Shield alert label should be added to the root.");
            assertFalse(shieldAlert.isVisible(), "Shield alert should initially be invisible.");
            assertEquals("Arial", shieldAlert.getFont().getName(), "Font name should be Arial.");
        });
    }
}
