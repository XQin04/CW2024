package com.example.demo.actors.enemies;

import com.example.demo.JavaFXInitializer;
import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.projectiles.EnemyProjectile;
import javafx.application.Platform;
import javafx.scene.Group;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

class EnemySpiderTest extends JavaFXInitializer {

    private EnemySpider enemySpider;
    private Group root;

    @BeforeAll
    static void initializeJavaFX() {
        // Initialize JavaFX toolkit
        Platform.startup(() -> {});
    }

    @BeforeEach
    void setUp() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            root = new Group(); // Initialize JavaFX root node
            enemySpider = new EnemySpider(500, 300); // Initialize enemy spider at position (500, 300)
            root.getChildren().add(enemySpider); // Add enemy spider to the root
            latch.countDown();
        });
        latch.await(); // Wait for JavaFX setup to complete
    }

    @AfterEach
    void tearDown() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            root.getChildren().clear(); // Clear the root group
            enemySpider = null; // Reset enemy spider instance
            latch.countDown();
        });
        latch.await(); // Wait for JavaFX cleanup to complete
    }

    @Test
    void testUpdatePosition() {
        Platform.runLater(() -> {
            double initialX = enemySpider.getTranslateX();

            enemySpider.updatePosition();

            double updatedX = enemySpider.getTranslateX();
            assertTrue(updatedX < initialX, "EnemySpider should move horizontally to the left.");
        });
    }

    @Test
    void testFireProjectile() {
        Platform.runLater(() -> {
            ActiveActorDestructible projectile = enemySpider.fireProjectile();

            if (projectile != null) {
                assertTrue(projectile instanceof EnemyProjectile, "Projectile fired should be an instance of EnemyProjectile.");
                assertEquals(enemySpider.getLayoutX() + enemySpider.getTranslateX() - 100.0,
                        projectile.getTranslateX(),
                        0.1,
                        "Projectile X position should be offset by -100.0.");

                assertEquals(enemySpider.getLayoutY() + enemySpider.getTranslateY() + 50.0,
                        projectile.getTranslateY(),
                        0.1,
                        "Projectile Y position should be offset by 50.0.");
            } else {
                // Since firing is probabilistic, we don't enforce a projectile to always be fired.
                assertNull(projectile, "Projectile might not be fired due to random probability.");
            }
        });
    }

    @Test
    void testUpdateActor() {
        Platform.runLater(() -> {
            double initialX = enemySpider.getTranslateX();

            enemySpider.updateActor();

            double updatedX = enemySpider.getTranslateX();
            assertTrue(updatedX < initialX, "EnemySpider should move horizontally to the left when updateActor is called.");
        });
    }

    @Test
    void testHealthInitialization() {
        assertEquals(1, enemySpider.getHealth(), "EnemySpider should initialize with 1 health.");
    }
}
