package com.example.demo.actors.player;

import com.example.demo.JavaFXInitializer;
import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.ui.gameplayUI.LevelView;
import com.example.demo.gameplay.LevelParent;
import com.example.demo.managers.ProjectileManager;
import com.example.demo.managers.SoundManager;
import javafx.application.Platform;
import javafx.scene.Group;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

class UserSupermanTest extends JavaFXInitializer {

    private UserSuperman userSuperman;
    private LevelParentStub levelParent;
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
            root = new Group();
            levelParent = new LevelParentStub(root);
            userSuperman = new UserSuperman(levelParent, 100);
            root.getChildren().add(userSuperman);
            latch.countDown();
        });
        latch.await();
    }

    @AfterEach
    void tearDown() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            root.getChildren().clear();
            userSuperman = null;
            levelParent = null;
            latch.countDown();
        });
        latch.await();
    }

    @Test
    void testMoveUp() {
        Platform.runLater(() -> {
            userSuperman.moveUp();
            userSuperman.updatePosition();

            assertTrue(userSuperman.getTranslateY() < 0, "UserSuperman should move upward when moveUp is called.");
        });
    }

    @Test
    void testMoveDown() {
        Platform.runLater(() -> {
            userSuperman.moveDown();
            userSuperman.updatePosition();

            assertTrue(userSuperman.getTranslateY() > 0, "UserSuperman should move downward when moveDown is called.");
        });
    }

    @Test
    void testMoveRight() {
        Platform.runLater(() -> {
            userSuperman.moveRight();
            userSuperman.updatePosition();

            assertTrue(userSuperman.getTranslateX() > 0, "UserSuperman should move right when moveRight is called.");
        });
    }

    @Test
    void testStopMovement() {
        Platform.runLater(() -> {
            userSuperman.moveUp();
            userSuperman.stopVertical();
            userSuperman.updatePosition();

            assertEquals(0, userSuperman.getTranslateY(), "UserSuperman should stop vertical movement when stopVertical is called.");

            userSuperman.moveLeft();
            userSuperman.stopHorizontal();
            userSuperman.updatePosition();

            assertEquals(0, userSuperman.getTranslateX(), "UserSuperman should stop horizontal movement when stopHorizontal is called.");
        });
    }

    @Test
    void testFireProjectile() {
        Platform.runLater(() -> {
            ActiveActorDestructible projectile = userSuperman.fireProjectile();

            assertNotNull(projectile, "Projectile should not be null when fired.");
            assertTrue(levelParent.getProjectileManager().containsUserProjectile(projectile), "Projectile should be added to the ProjectileManager.");
        });
    }

    @Test
    void testSpreadshot() {
        Platform.runLater(() -> {
            userSuperman.activateOneTimeSpreadshot();
            userSuperman.fireProjectile();

            assertEquals(6, levelParent.getProjectileManager().getUserProjectiles().size(), "Spreadshot should fire 6 projectiles.");
        });
    }

    // Stub Classes

    private static class LevelParentStub extends LevelParent {

        private final ProjectileManager projectileManager;
        private final SoundManager soundManager;
        private final Group root;

        public LevelParentStub(Group root) {
            super("/com/example/demo/images/background1.jpg", 600, 800, 100, null, "Test Level");
            this.projectileManager = ProjectileManager.getInstance();
            this.soundManager = SoundManager.getInstance();
            this.root = root;
        }

        @Override
        protected void initializeFriendlyUnits() {
            // No-op for testing
        }

        @Override
        protected LevelView instantiateLevelView() {
            return new LevelView(new Group(), 0) {
                @Override
                public void showHeartDisplay() {
                    // No-op for testing
                }

                @Override
                public void removeHearts(int remainingHearts) {
                    // No-op for testing
                }

                @Override
                public void showWinImage() {
                    // No-op for testing
                }

                @Override
                public void removeWinImage() {
                    // No-op for testing
                }

                @Override
                public void showGameOverImage() {
                    // No-op for testing
                }

                @Override
                public void removeGameOverImage() {
                    // No-op for testing
                }
            };
        }


        @Override
        protected void checkIfGameOver() {
            // No-op for testing
        }

        @Override
        protected void spawnEnemyUnits() {
            // No-op for testing
        }

        @Override
        public ProjectileManager getProjectileManager() {
            return projectileManager;
        }

        @Override
        public SoundManager getSoundManager() {
            return soundManager;
        }

        @Override
        public Group getRoot() {
            return root;
        }
    }




}
