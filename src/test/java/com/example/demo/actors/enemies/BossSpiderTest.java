package com.example.demo.actors.enemies;

import com.example.demo.gameplay.LevelParent;
import com.example.demo.ui.gameplayUI.LevelView;
import com.example.demo.JavaFXInitializer;
import javafx.scene.control.Label;
import javafx.scene.Group;
import javafx.application.Platform;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BossSpiderTest extends JavaFXInitializer {

    private BossSpider bossSpider;
    private Label shieldAlert;
    private LevelParentStub levelParent;

    @BeforeAll
    static void initializeJavaFX() {
        // Ensure JavaFX is initialized
        Platform.startup(() -> {});
    }
    @BeforeEach
    void setUp() {
        // Initialize JavaFX components
        shieldAlert = new Label();
        levelParent = new LevelParentStub(1300.0, 750.0);

        // Initialize BossSpider
        bossSpider = new BossSpider(levelParent, shieldAlert);
    }

    @Test
    void testFireProjectile() {
        var projectile = bossSpider.fireProjectile();

        if (projectile != null) {
            assertEquals(bossSpider.getLayoutY() + bossSpider.getTranslateY() + 75.0, projectile.getLayoutY(),
                    "Projectile should start at the correct Y position.");
        } else {
            assertNull(projectile, "BossSpider may not fire a projectile every frame.");
        }
    }

    @Test
    void testActivateAndDeactivateShield() {
        bossSpider.activateShield();

        assertTrue(shieldAlert.isVisible(), "Shield alert should be visible.");
        assertNotNull(bossSpider.getEffect(), "Shield visual effect should be applied.");

        bossSpider.deactivateShield();

        assertFalse(shieldAlert.isVisible(), "Shield alert should be hidden.");
        assertNull(bossSpider.getEffect(), "Shield visual effect should be removed.");
    }

    @Test
    void testCustomHitbox() {
        var hitbox = bossSpider.getCustomHitbox();
        var originalBounds = bossSpider.getBoundsInParent();

        assertTrue(hitbox.getWidth() < originalBounds.getWidth(), "Custom hitbox width should be smaller.");
        assertTrue(hitbox.getHeight() < originalBounds.getHeight(), "Custom hitbox height should be smaller.");
    }

    // Stub class for LevelParent with required implementations
    private static class LevelParentStub extends LevelParent {
        private final double screenWidth;
        private final double screenHeight;

        public LevelParentStub(double screenWidth, double screenHeight) {
            super("/com/example/demo/images/background1.jpg", screenHeight, screenWidth, 0, null, null);
            this.screenWidth = screenWidth;
            this.screenHeight = screenHeight;
        }

        @Override
        public double getScreenWidth() {
            return screenWidth;
        }

        @Override
        public double getScreenHeight() {
            return screenHeight;
        }

        @Override
        protected void initializeFriendlyUnits() {
            // Stub implementation
        }

        @Override
        protected LevelView instantiateLevelView() {
            return new LevelView(new Group(), 3); // Adjust parameters to match your constructor
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
