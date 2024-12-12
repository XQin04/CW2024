package com.example.demo.managers;

import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.actors.enemies.FighterSpider;
import com.example.demo.JavaFXInitializer;
import javafx.application.Platform;
import javafx.scene.Group;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

class ProjectileManagerTest extends JavaFXInitializer {

    private ProjectileManager projectileManager;
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
            projectileManager = ProjectileManager.getInstance(); // Get the singleton instance
            projectileManager.initialize(root); // Initialize with the root group
            latch.countDown();
        });
        latch.await(); // Wait for the JavaFX setup to complete
    }

    @AfterEach
    void tearDown() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            projectileManager.clearAllProjectiles(); // Clear all projectiles
            root.getChildren().clear(); // Clear the root group
            latch.countDown();
        });
        latch.await(); // Wait for cleanup to complete
    }

    @Test
    void testAddUserProjectile() {
        Platform.runLater(() -> {
            ActiveActorDestructible projectile = new ProjectileStub();
            projectileManager.addUserProjectile(projectile);

            assertTrue(root.getChildren().contains(projectile), "Root should contain the user projectile.");
            assertEquals(1, root.getChildren().size(), "Root should have exactly one user projectile.");
        });
    }

    @Test
    void testAddEnemyProjectile() {
        Platform.runLater(() -> {
            ActiveActorDestructible projectile = new ProjectileStub();
            projectileManager.addEnemyProjectile(projectile);

            assertTrue(root.getChildren().contains(projectile), "Root should contain the enemy projectile.");
            assertEquals(1, root.getChildren().size(), "Root should have exactly one enemy projectile.");
        });
    }

    @Test
    void testUpdateProjectiles() {
        Platform.runLater(() -> {
            ProjectileStub userProjectile = new ProjectileStub();
            projectileManager.addUserProjectile(userProjectile);

            projectileManager.updateProjectiles();

            assertTrue(userProjectile.isUpdated, "User projectile should be updated.");
        });
    }

    @Test
    void testRemoveDestroyedProjectiles() {
        Platform.runLater(() -> {
            ProjectileStub destroyedProjectile = new ProjectileStub();
            destroyedProjectile.setDestroyed(true);
            projectileManager.addUserProjectile(destroyedProjectile);

            projectileManager.removeDestroyedProjectiles();

            assertFalse(root.getChildren().contains(destroyedProjectile), "Destroyed projectile should be removed from root.");
        });
    }

    @Test
    void testGenerateEnemyProjectiles() {
        Platform.runLater(() -> {
            FighterSpiderStub enemy = new FighterSpiderStub();
            List<ActiveActorDestructible> enemies = new ArrayList<>();
            enemies.add(enemy);

            projectileManager.generateEnemyProjectiles(enemies);

            assertTrue(enemy.hasFiredProjectile, "Enemy should have fired a projectile.");
            assertEquals(1, root.getChildren().size(), "Root should contain one enemy projectile.");
        });
    }

    @Test
    void testClearAllProjectiles() {
        Platform.runLater(() -> {
            ProjectileStub projectile = new ProjectileStub();
            projectileManager.addUserProjectile(projectile);

            projectileManager.clearAllProjectiles();

            assertTrue(root.getChildren().isEmpty(), "Root should be empty after clearing all projectiles.");
        });
    }

    // Stub Classes

    private static class ProjectileStub extends ActiveActorDestructible {
        boolean isUpdated = false;

        public ProjectileStub() {
            super("enemyweb.png", 10, 0, 0); // Provide necessary parameters for constructor
        }

        @Override
        public void updatePosition() {
            isUpdated = true;
        }

        @Override
        public void updateActor() {
            updatePosition();
        }

        @Override
        public void destroy() {
            setDestroyed(true);
        }

        @Override
        public boolean isDestroyed() {
            return isDestroyed;
        }

        @Override
        public void takeDamage() {
            setDestroyed(true);
        }

        public void setDestroyed(boolean destroyed) {
            this.isDestroyed = destroyed;
        }
    }

    private static class FighterSpiderStub extends FighterSpider {
        boolean hasFiredProjectile = false;

        public FighterSpiderStub() {
            super("enemy.png", 100, 0, 0, 1); // Provide necessary parameters for constructor
        }

        @Override
        public ActiveActorDestructible fireProjectile() {
            hasFiredProjectile = true;
            return new ProjectileStub();
        }

        @Override
        public void updatePosition() {
            // No-op for testing
        }

        @Override
        public void updateActor() {
            updatePosition(); // Simply call updatePosition for testing
        }

        @Override
        public void takeDamage() {
            destroy(); // Mark this stub as destroyed when taking damage
        }
    }

}
