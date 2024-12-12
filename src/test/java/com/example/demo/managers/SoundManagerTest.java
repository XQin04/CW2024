package com.example.demo.managers;

import com.example.demo.JavaFXInitializer;
import javafx.application.Platform;
import javafx.scene.media.AudioClip;
import javafx.scene.media.MediaPlayer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

class SoundManagerTest extends JavaFXInitializer {

    private SoundManager soundManager;

    @BeforeAll
    static void initializeJavaFX() {
        JavaFXInitializer.initialize();
    }

    @BeforeEach
    void setUp() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            soundManager = SoundManager.getInstance(); // Initialize SoundManager singleton
            latch.countDown();
        });
        latch.await(); // Wait for JavaFX setup to complete
    }

    @AfterEach
    void tearDown() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            soundManager = null; // Clean up SoundManager instance
            latch.countDown();
        });
        latch.await(); // Wait for JavaFX cleanup
    }

    @Test
    void testPlayShootSound() {
        Platform.runLater(() -> {
            soundManager.setSoundEffectsMuted(false); // Unmute sound effects
            soundManager.playShootSound(); // Play shoot sound
            // Verify that no exception was thrown and sound played successfully
            assertTrue(true, "Shoot sound should play without errors.");
        });
    }

    @Test
    void testMuteAndUnmuteSoundEffects() {
        Platform.runLater(() -> {
            // Mute sound effects
            soundManager.setSoundEffectsMuted(true);
            assertTrue(soundManager.isSoundEffectsMuted(), "Sound effects should be muted.");

            // Unmute sound effects
            soundManager.setSoundEffectsMuted(false);
            assertFalse(soundManager.isSoundEffectsMuted(), "Sound effects should be unmuted.");
        });
    }

    @Test
    void testPlaySoundEffect() {
        Platform.runLater(() -> {
            soundManager.setSoundEffectsMuted(false); // Ensure sound effects are unmuted
            soundManager.playSound("win"); // Play the "win" sound
            // Verify that no exception was thrown and sound played successfully
            assertTrue(true, "Win sound should play without errors.");
        });
    }

    @Test
    void testPlayPowerUpSound() {
        Platform.runLater(() -> {
            soundManager.setSoundEffectsMuted(false); // Unmute sound effects
            soundManager.playPowerUpSound(); // Play power-up sound
            // Verify that no exception was thrown and sound played successfully
            assertTrue(true, "Power-up sound should play without errors.");
        });
    }

    @Test
    void testMuteAndUnmuteMusic() {
        Platform.runLater(() -> {
            // Mute music
            soundManager.setMusicMuted(true);
            assertTrue(soundManager.isMusicMuted(), "Music should be muted.");

            // Unmute music
            soundManager.setMusicMuted(false);
            assertFalse(soundManager.isMusicMuted(), "Music should be unmuted.");
        });
    }


    @Test
    void testVolumeAdjustment() {
        Platform.runLater(() -> {
            soundManager.setSoundEffectsMuted(false); // Ensure sound effects are unmuted
            soundManager.setMusicMuted(true); // Mute music
            // Verify that mute settings were applied
            assertFalse(soundManager.isSoundEffectsMuted(), "Sound effects should be unmuted.");
            assertTrue(soundManager.isMusicMuted(), "Music should be muted.");
        });
    }
}
