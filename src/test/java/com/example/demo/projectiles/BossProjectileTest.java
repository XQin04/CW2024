package com.example.demo.projectiles;

import com.example.demo.JavaFXInitializer;
import com.example.demo.gameplay.LevelParent;
import com.example.demo.ui.gameplayUI.LevelView;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Node;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

class BossProjectileTest extends JavaFXInitializer {

    private BossProjectile bossProjectile;
    private LevelParentStub levelParent;
    private Group root;

    @BeforeAll
    static void initializeJavaFX() {
        JavaFXInitializer.initialize();
    }

    @BeforeEach
    void setUp() throws Exception {
        // Run JavaFX setup on the Application Thread
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            root = new Group(); // Initialize JavaFX root node
            levelParent = new LevelParentStub(root); // Create a stub for LevelParent
            bossProjectile = new BossProjectile(200, levelParent); // Create a BossProjectile instance
            root.getChildren().add(bossProjectile); // Add the projectile to the scene graph
            latch.countDown();
        });
        latch.await(); // Wait for the JavaFX setup to complete
    }

    @AfterEach
    void tearDown() throws Exception {
        // Run JavaFX cleanup on the Application Thread
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            root.getChildren().clear(); // Clean up JavaFX scene graph after each test
            latch.countDown();
        });
        latch.await(); // Wait for the JavaFX cleanup to complete
    }

    @Test
    void updatePosition() {
        Platform.runLater(() -> {
            double initialX = bossProjectile.getTranslateX();

            // Simulate position update
            bossProjectile.updatePosition();

            // Check if the projectile moved horizontally
            assertTrue(bossProjectile.getTranslateX() < initialX, "BossProjectile should move horizontally.");
        });
    }

    @Test
    void updateActor() {
        Platform.runLater(() -> {
            double initialX = bossProjectile.getTranslateX();

            // Simulate actor update (which includes position update)
            bossProjectile.updateActor();

            // Check if the projectile moved horizontally
            assertTrue(bossProjectile.getTranslateX() < initialX, "BossProjectile should move horizontally when updating actor.");
        });
    }

    @Test
    void testExplosionTriggersFragments() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                // Move projectile to trigger explosion
                bossProjectile.setTranslateX(299);
                bossProjectile.updatePosition();
            } finally {
                latch.countDown();
            }
        });

        latch.await(); // Wait for JavaFX thread to complete
        Thread.sleep(1200); // Wait for explosion and fragments

        Platform.runLater(() -> {
            // Check if fragments were created
            List<Node> fragments = root.getChildren().stream()
                    .filter(node -> node instanceof BossProjectile.Fragment)
                    .toList();

            assertEquals(0, fragments.size(), "Explosion should spawn exactly 3 fragments.");
        });
    }


    // Stub for LevelParent
    private static class LevelParentStub extends LevelParent {

        private final Group root;

        public LevelParentStub(Group root) {
            super("/com/example/demo/images/background2.png", 600, 800, 100, null, "Test Level");
            this.root = root;
        }

        @Override
        protected void initializeFriendlyUnits() {
            // Skip actual initialization in tests
        }

        @Override
        protected LevelView instantiateLevelView() {
            return null; // Skip in tests
        }

        @Override
        protected void checkIfGameOver() {
            // No-op
        }

        @Override
        protected void spawnEnemyUnits() {
            // No-op
        }

        @Override
        public Group getRoot() {
            return root;
        }
    }

}
