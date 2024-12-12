package com.example.demo.managers;

import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.powerups.PowerUp;
import com.example.demo.JavaFXInitializer;
import javafx.application.Platform;
import javafx.scene.Group;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

class PowerUpManagerTest extends JavaFXInitializer {

    private PowerUpManager powerUpManager;
    private Group root;

    @BeforeAll
    static void initializeJavaFX() {
        JavaFXInitializer.initialize();
    }

    @BeforeEach
    void setUp() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            root = new Group(); // Initialize the JavaFX root node
            powerUpManager = PowerUpManager.getInstance(); // Get the singleton instance
            powerUpManager.initialize(root); // Initialize the manager with the root
            latch.countDown();
        });
        latch.await(); // Wait for the JavaFX setup to complete
    }

    @AfterEach
    void tearDown() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            powerUpManager.clearAllPowerUps(); // Clear all power-ups
            root.getChildren().clear(); // Clear the root node
            latch.countDown();
        });
        latch.await(); // Wait for cleanup to complete
    }

    @Test
    void testAddPowerUp() {
        Platform.runLater(() -> {
            PowerUpStub powerUp = new PowerUpStub();
            powerUpManager.addPowerUp(powerUp);

            assertTrue(root.getChildren().contains(powerUp), "Root should contain the added power-up.");
            assertEquals(1, root.getChildren().size(), "Root should contain exactly one power-up.");
        });
    }

    @Test
    void testUpdatePowerUps() {
        Platform.runLater(() -> {
            PowerUpStub powerUp = new PowerUpStub();
            powerUpManager.addPowerUp(powerUp);

            powerUpManager.updatePowerUps();

            assertTrue(powerUp.isUpdated, "Power-up should be updated.");
        });
    }

    @Test
    void testRemoveDestroyedPowerUps() {
        Platform.runLater(() -> {
            PowerUpStub destroyedPowerUp = new PowerUpStub();
            destroyedPowerUp.setDestroyed(true);
            powerUpManager.addPowerUp(destroyedPowerUp);

            powerUpManager.removeDestroyedPowerUps();

            assertFalse(root.getChildren().contains(destroyedPowerUp), "Destroyed power-up should be removed from the root.");
            assertTrue(powerUpManager.powerUps.isEmpty(), "All destroyed power-ups should be removed from the manager.");
        });
    }

    @Test
    void testClearAllPowerUps() {
        Platform.runLater(() -> {
            PowerUpStub powerUp = new PowerUpStub();
            powerUpManager.addPowerUp(powerUp);

            powerUpManager.clearAllPowerUps();

            assertTrue(root.getChildren().isEmpty(), "Root should be empty after clearing all power-ups.");
        });
    }

    // Stub class for PowerUp
    private static class PowerUpStub extends PowerUp {
        boolean isUpdated = false;

        public PowerUpStub() {
            super("spreadshot.png", 0.0, 0.0); // Provide dummy parameters
        }

        @Override
        public void updatePosition() {
            isUpdated = true; // Mark as updated when this method is called
        }

        @Override
        public void updateActor() {
            updatePosition(); // Call updatePosition for testing
        }

        @Override
        public void takeDamage() {
            setDestroyed(true); // Mark as destroyed when taking damage
        }


        public void setDestroyed(boolean destroyed) {
            this.isDestroyed = destroyed;
        }
    }
}
