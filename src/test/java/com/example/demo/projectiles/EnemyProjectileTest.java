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

class EnemyProjectileTest extends JavaFXInitializer {

    private EnemyProjectile enemyProjectile;
    private Group root;

    @BeforeAll
    static void initializeJavaFX() {
        // Initialize JavaFX toolkit
        Platform.startup(() -> {});
    }

    @BeforeEach
    void setUp() throws Exception {
        // Initialize JavaFX environment and root node
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            root = new Group(); // Create the root group
            enemyProjectile = new EnemyProjectile(500, 200); // Create EnemyProjectile instance
            root.getChildren().add(enemyProjectile); // Add the projectile to the scene graph
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
            double initialX = enemyProjectile.getTranslateX(); // Get the initial X position

            // Update the projectile's position
            enemyProjectile.updatePosition();

            // Check that the projectile moved leftward
            assertTrue(enemyProjectile.getTranslateX() < initialX, "EnemyProjectile should move leftward.");
            latch.countDown();
        });

        latch.await(); // Wait for the test to complete
    }

    @Test
    void testUpdateActor() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            double initialX = enemyProjectile.getTranslateX(); // Get the initial X position

            // Update the actor (includes position update)
            enemyProjectile.updateActor();

            // Check that the projectile moved leftward
            assertTrue(enemyProjectile.getTranslateX() < initialX, "EnemyProjectile should move leftward when updateActor is called.");
            latch.countDown();
        });

        latch.await(); // Wait for the test to complete
    }

    @Test
    void testInitialPosition() {
        Platform.runLater(() -> {
            // Assert initial position matches the constructor values
            assertEquals(0, enemyProjectile.getTranslateX(), "Initial X position should match the constructor value.");
            assertEquals(0, enemyProjectile.getTranslateY(), "Initial Y position should match the constructor value.");
        });
    }
}
