package com.example.demo.ui;

import com.example.demo.gameplay.LevelParent;
import com.example.demo.ui.gameplayUI.LevelView;
import com.example.demo.JavaFXInitializer;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

public class UIManagerTest extends JavaFXInitializer {

    private UIManager uiManager;
    private LevelParentStub levelParent;
    private Group menuLayer;
    private double screenWidth = 800;
    private double screenHeight = 600;
    private Stage stage;

    @BeforeAll
    static void initializeJavaFX() {
        JavaFXInitializer.initialize();
    }

    @BeforeEach
    void setUp() {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            // Initialize JavaFX components
            menuLayer = new Group();
            stage = new Stage();

            // Create a stub for LevelParent
            levelParent = new LevelParentStub(screenWidth, screenHeight);

            // Initialize UIManager
            uiManager = UIManager.getInstance(levelParent, menuLayer, screenWidth, screenHeight, stage);
            uiManager.initializeUI();

            latch.countDown();
        });
        waitForFXThread(latch);
    }

    @AfterEach
    void tearDown() {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            // Cleanup after each test
            uiManager.cleanup();
            UIManager.resetInstance();
            latch.countDown();
        });
        waitForFXThread(latch);
    }


    @Test
    void testCleanup() {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            // Call cleanup and verify UI elements are removed
            uiManager.cleanup();
            assertTrue(menuLayer.getChildren().isEmpty(), "Menu layer should be empty after cleanup.");
            latch.countDown();
        });
        waitForFXThread(latch);
    }

    private void waitForFXThread(CountDownLatch latch) {
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Failed to wait for FX Thread", e);
        }
    }

    // Stub class for LevelParent with minimal implementation
    private static class LevelParentStub extends LevelParent {

        public LevelParentStub(double screenWidth, double screenHeight) {
            // Pass a non-null value for backgroundImageName and other parameters
            super("/com/example/demo/images/background1.jpg", screenHeight, screenWidth, 0, null, "Test Level");
        }

        @Override
        protected void initializeFriendlyUnits() {
            // Stub implementation
        }

        @Override
        protected LevelView instantiateLevelView() {
            // Return a mocked or default instance
            return new LevelView(new Group(), 3); // Adjust parameters as per constructor
        }

        @Override
        protected void checkIfGameOver() {
            // Stub implementation
        }

        @Override
        protected void spawnEnemyUnits() {
            // Stub implementation
        }
    }

}
