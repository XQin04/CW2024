package com.example.demo.managers;

import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.JavaFXInitializer;
import javafx.application.Platform;
import javafx.scene.Group;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EnemyManagerTest extends JavaFXInitializer {

    private EnemyManager enemyManager;
    private Group root;
    @BeforeAll
    static void initializeJavaFX() {
        JavaFXInitializer.initialize();
    }

    @BeforeEach
    void setUp() {
        // Set up the JavaFX root group and initialize EnemyManager
        root = new Group();
        enemyManager = EnemyManager.getInstance();
        enemyManager.initialize(root); // Initialize with the root group
    }

    @Test
    void testAddEnemy() {
        ActiveActorDestructibleMock enemy = new ActiveActorDestructibleMock();

        enemyManager.addEnemy(enemy);

        // Check if the enemy was added to the tracking list
        assertEquals(1, enemyManager.getEnemies().size(), "Enemy should be added to the tracking list.");

        // Check if the enemy was added to the root group
        assertTrue(root.getChildren().contains(enemy), "Enemy should be added to the root group.");
    }

    @Test
    void testUpdateEnemies() {
        ActiveActorDestructibleMock enemy1 = new ActiveActorDestructibleMock();
        ActiveActorDestructibleMock enemy2 = new ActiveActorDestructibleMock();

        enemyManager.addEnemy(enemy1);
        enemyManager.addEnemy(enemy2);

        enemyManager.updateEnemies();

        // Verify that `updateActor` is called for each enemy
        assertTrue(enemy1.isUpdated(), "Enemy1 should have its `updateActor` method called.");
        assertTrue(enemy2.isUpdated(), "Enemy2 should have its `updateActor` method called.");
    }

    @Test
    void testRemoveDestroyedEnemies() {
        ActiveActorDestructibleMock enemy1 = new ActiveActorDestructibleMock();
        ActiveActorDestructibleMock enemy2 = new ActiveActorDestructibleMock();

        enemyManager.addEnemy(enemy1);
        enemyManager.addEnemy(enemy2);

        // Destroy one enemy
        enemy1.destroy();

        // Remove destroyed enemies
        enemyManager.removeDestroyedEnemies();

        // Check that the destroyed enemy is removed
        assertEquals(1, enemyManager.getEnemies().size(), "Only one enemy should remain after removing destroyed ones.");
        assertFalse(root.getChildren().contains(enemy1), "Destroyed enemy should be removed from the root group.");
        assertTrue(root.getChildren().contains(enemy2), "Non-destroyed enemy should still be in the root group.");
    }

    @Test
    void testClearAllEnemies() {
        ActiveActorDestructibleMock enemy1 = new ActiveActorDestructibleMock();
        ActiveActorDestructibleMock enemy2 = new ActiveActorDestructibleMock();

        enemyManager.addEnemy(enemy1);
        enemyManager.addEnemy(enemy2);

        enemyManager.clearAllEnemies();

        // Check that all enemies are cleared
        assertTrue(enemyManager.getEnemies().isEmpty(), "Enemy list should be empty after clearing all enemies.");
        assertTrue(root.getChildren().isEmpty(), "Root group should be empty after clearing all enemies.");
    }

    @Test
    void testGetEnemyCount() {
        ActiveActorDestructibleMock enemy1 = new ActiveActorDestructibleMock();
        ActiveActorDestructibleMock enemy2 = new ActiveActorDestructibleMock();

        enemyManager.addEnemy(enemy1);
        enemyManager.addEnemy(enemy2);

        // Check the count of active enemies
        assertEquals(2, enemyManager.getEnemyCount(), "Enemy count should match the number of added enemies.");
    }

    @Test
    void testGetEnemies() {
        ActiveActorDestructibleMock enemy1 = new ActiveActorDestructibleMock();
        ActiveActorDestructibleMock enemy2 = new ActiveActorDestructibleMock();

        enemyManager.addEnemy(enemy1);
        enemyManager.addEnemy(enemy2);

        List<ActiveActorDestructible> enemies = enemyManager.getEnemies();

        // Check that the retrieved list matches the added enemies
        assertEquals(2, enemies.size(), "Enemy list size should match the number of added enemies.");
        assertTrue(enemies.contains(enemy1), "Enemy list should contain enemy1.");
        assertTrue(enemies.contains(enemy2), "Enemy list should contain enemy2.");
    }

    // Mock class for ActiveActorDestructible
    private static class ActiveActorDestructibleMock extends ActiveActorDestructible {
        private boolean destroyed = false;
        private boolean updated = false;

        public ActiveActorDestructibleMock() {
            super("enemy.png", 50, 0, 0);
        }

        @Override
        public void destroy() {
            destroyed = true;
        }

        @Override
        public boolean isDestroyed() {
            return destroyed;
        }

        @Override
        public void updateActor() {
            updated = true;
        }

        @Override
        public void updatePosition() {
            // Mock implementation for position update
        }

        @Override
        public void takeDamage() {
            destroyed = true;
        }

        public boolean isUpdated() {
            return updated;
        }


    }
}
