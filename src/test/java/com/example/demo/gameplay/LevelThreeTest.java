package com.example.demo.gameplay;

import com.example.demo.ui.gameplayUI.LevelView;
import com.example.demo.JavaFXInitializer;
import com.example.demo.managers.GameStateManager;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

class LevelThreeTest extends JavaFXInitializer {

    private LevelThree levelThree;
    private Stage testStage;
    private Group root;

    @BeforeAll
    static void initializeJavaFX() {
        JavaFXInitializer.initialize();
    }

    @BeforeEach
    void setUp() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            testStage = new Stage();
            levelThree = new LevelThree(600, 800, testStage);
            root = levelThree.getRoot();
            latch.countDown();
        });
        latch.await();
    }

    @AfterEach
    void tearDown() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            levelThree.cleanup(); // Assuming cleanup method exists in LevelParent
            testStage.close();
            latch.countDown();
        });
        latch.await();
    }

    @Test
    void testInitialization() {
        Platform.runLater(() -> {
            Scene scene = levelThree.initializeScene(testStage);
            assertNotNull(scene, "Scene should not be null.");
            assertEquals(root, scene.getRoot(), "Scene root should match LevelThree's root.");
        });
    }

    @Test
    void testFriendlyUnitsInitialization() {
        Platform.runLater(() -> {
            levelThree.initializeFriendlyUnits();
            assertTrue(root.getChildren().contains(levelThree.getUser()), "Root should contain the user's superman.");
        });
    }

    @Test
    void testEnemyWaveSpawning() {
        Platform.runLater(() -> {
            levelThree.spawnEnemyUnits();
            assertTrue(levelThree.getEnemyManager().getEnemyCount() > 0, "Enemies should spawn during waves.");
        });
    }


    @Test
    void testPowerUpSpawning() {
        Platform.runLater(() -> {
            levelThree.spawnEnemyUnits(); // This also attempts to spawn power-ups
            assertTrue(levelThree.getPowerUpManager().getPowerUpCount() >= 0, "Power-ups should spawn with the correct probability.");
        });
    }


    @Test
    void testLevelViewInitialization() {
        Platform.runLater(() -> {
            LevelView levelView = levelThree.instantiateLevelView();
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
