package com.example.demo.managers;

import com.example.demo.actors.player.UserSuperman;
import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.JavaFXInitializer;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

class InputHandlerTest extends JavaFXInitializer {

    private InputHandler inputHandler;
    private UserSupermanStub player;
    private GameStateManagerStub gameStateManager;

    @BeforeAll
    static void initializeJavaFX() {
        // Initialize JavaFX toolkit
        Platform.startup(() -> {});
    }

    @BeforeEach
    void setUp() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            player = new UserSupermanStub(); // Initialize player stub
            gameStateManager = new GameStateManagerStub(); // Initialize game state manager stub
            inputHandler = new InputHandler(player, gameStateManager); // Initialize input handler
            latch.countDown();
        });
        latch.await(); // Wait for JavaFX setup to complete
    }

    @AfterEach
    void tearDown() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            player = null;
            gameStateManager = null;
            inputHandler = null;
            latch.countDown();
        });
        latch.await(); // Wait for JavaFX cleanup
    }

    @Test
    void testHandleKeyPress() {
        Platform.runLater(() -> {
            // Simulate key presses and verify actions
            inputHandler.handleKeyPress(new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.UP, false, false, false, false));
            assertTrue(player.movedUp, "Player should move up on UP key press.");

            inputHandler.handleKeyPress(new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.DOWN, false, false, false, false));
            assertTrue(player.movedDown, "Player should move down on DOWN key press.");

            inputHandler.handleKeyPress(new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.LEFT, false, false, false, false));
            assertTrue(player.movedLeft, "Player should move left on LEFT key press.");

            inputHandler.handleKeyPress(new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.RIGHT, false, false, false, false));
            assertTrue(player.movedRight, "Player should move right on RIGHT key press.");

            inputHandler.handleKeyPress(new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.SPACE, false, false, false, false));
            assertTrue(player.firedProjectile, "Player should fire a projectile on SPACE key press.");
        });
    }

    @Test
    void testHandleKeyRelease() {
        Platform.runLater(() -> {
            // Simulate key releases and verify actions
            inputHandler.handleKeyRelease(new KeyEvent(KeyEvent.KEY_RELEASED, "", "", KeyCode.UP, false, false, false, false));
            assertTrue(player.stoppedVertical, "Player should stop vertical movement on UP key release.");

            inputHandler.handleKeyRelease(new KeyEvent(KeyEvent.KEY_RELEASED, "", "", KeyCode.DOWN, false, false, false, false));
            assertTrue(player.stoppedVertical, "Player should stop vertical movement on DOWN key release.");

            inputHandler.handleKeyRelease(new KeyEvent(KeyEvent.KEY_RELEASED, "", "", KeyCode.LEFT, false, false, false, false));
            assertTrue(player.stoppedHorizontal, "Player should stop horizontal movement on LEFT key release.");

            inputHandler.handleKeyRelease(new KeyEvent(KeyEvent.KEY_RELEASED, "", "", KeyCode.RIGHT, false, false, false, false));
            assertTrue(player.stoppedHorizontal, "Player should stop horizontal movement on RIGHT key release.");
        });
    }

    @Test
    void testIgnoreInputWhenNotPlaying() {
        Platform.runLater(() -> {
            // Set game state to not playing
            gameStateManager.setPlaying(false);

            // Simulate key press and release events
            inputHandler.handleKeyPress(new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.UP, false, false, false, false));
            inputHandler.handleKeyRelease(new KeyEvent(KeyEvent.KEY_RELEASED, "", "", KeyCode.UP, false, false, false, false));

            // Verify that no actions were performed
            assertFalse(player.movedUp, "Player should not move up when the game is not in PLAYING state.");
            assertFalse(player.stoppedVertical, "Player should not stop vertical movement when the game is not in PLAYING state.");
        });
    }

    // Stub Classes

    private static class UserSupermanStub extends UserSuperman {
        boolean movedUp = false;
        boolean movedDown = false;
        boolean movedLeft = false;
        boolean movedRight = false;
        boolean firedProjectile = false;
        boolean stoppedVertical = false;
        boolean stoppedHorizontal = false;

        public UserSupermanStub() {
            super(null, 100); // Initialize with dummy LevelParent and health
        }

        @Override
        public void moveUp() {
            movedUp = true;
        }

        @Override
        public void moveDown() {
            movedDown = true;
        }

        @Override
        public void moveLeft() {
            movedLeft = true;
        }

        @Override
        public void moveRight() {
            movedRight = true;
        }

        @Override
        public ActiveActorDestructible fireProjectile() {
            firedProjectile = true;

            // Return a dummy ActiveActorDestructible instance
            return new ActiveActorDestructible("userfire.png", 0, 0, 0) {
                @Override
                public void updatePosition() {
                    // No-op for testing
                }

                @Override
                public void updateActor() {
                    // No-op for testing
                }

                @Override
                public void destroy() {
                    // No-op for testing
                }

                @Override
                public boolean isDestroyed() {
                    return false;
                }

                @Override
                public void takeDamage() {
                    // No-op for testing
                }
            };
        }

        @Override
        public void stopVertical() {
            stoppedVertical = true;
        }

        @Override
        public void stopHorizontal() {
            stoppedHorizontal = true;
        }
    }

    private static class GameStateManagerStub extends GameStateManager {
        private boolean isPlaying = true;

        public GameStateManagerStub() {
            instance = this; // Inject this stub as the Singleton instance
        }

        @Override
        public boolean isNotPlaying() {
            return !isPlaying;
        }

        public void setPlaying(boolean playing) {
            isPlaying = playing;
        }
    }
}
