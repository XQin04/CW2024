package com.example.demo.projectiles;

import com.example.demo.JavaFXInitializer;
import javafx.application.Platform;
import javafx.scene.Group;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

class UserProjectileTest extends JavaFXInitializer {

    private UserProjectile userProjectile;
    private Group root;

    @BeforeAll
    static void initializeJavaFX() {
        JavaFXInitializer.initialize();
    }

    @BeforeEach
    void setUp() throws Exception {
        // Initialize JavaFX environment and root node
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            root = new Group(); // Create the root group
            userProjectile = new UserProjectile(100, 200); // Create UserProjectile instance
            root.getChildren().add(userProjectile); // Add the projectile to the scene graph
            latch.countDown();
        });
        latch.await(); // Wait for the JavaFX setup to complete
    }

    @AfterEach
    void tearDown() throws Exception {
        // Clean up JavaFX environment
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            root.getChildren().clear(); // Clear all nodes
            latch.countDown();
        });
        latch.await(); // Wait for the cleanup to complete
    }

    @Test
    void testUpdatePosition() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            double initialX = userProjectile.getTranslateX(); // Get the initial X position

            // Update the projectile's position
            userProjectile.updatePosition();

            // Check that the projectile moved to the right
            assertTrue(userProjectile.getTranslateX() > initialX, "UserProjectile should move to the right.");
            latch.countDown();
        });

        latch.await(); // Wait for the test to complete
    }

    @Test
    void testUpdateActor() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            double initialX = userProjectile.getTranslateX(); // Get the initial X position

            // Update the actor (includes position update)
            userProjectile.updateActor();

            // Check that the projectile moved to the right
            assertTrue(userProjectile.getTranslateX() > initialX, "UserProjectile should move to the right when updateActor is called.");
            latch.countDown();
        });

        latch.await(); // Wait for the test to complete
    }

    @Test
    void testInitialPosition() {
        Platform.runLater(() -> {
            // Assert initial position matches the constructor values
            assertEquals(0, userProjectile.getTranslateX(), "Initial X position should match the constructor value.");
            assertEquals(0, userProjectile.getTranslateY(), "Initial Y position should match the constructor value.");
        });
    }
}
