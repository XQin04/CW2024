package com.example.demo;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.HashMap;
import java.util.Map;

public class SoundManager {

    // Static instance for singleton
    private static SoundManager instance;

    private static final String SHOOT_SOUND_PATH = "/com/example/demo/sounds/Shoot.mp3";
    private static final String WIN_SOUND_PATH = "/com/example/demo/sounds/Win.mp3";
    private static final String GAME_OVER_SOUND_PATH = "/com/example/demo/sounds/GameOver.mp3";
    private static final String POWER_UP_SOUND_PATH = "/com/example/demo/sounds/Spreadshot.mp3";

    private AudioClip shootClip; // Use AudioClip for low latency shooting sound
    private AudioClip powerUpClip; // Use AudioClip for power-up collection sound
    private Map<String, MediaPlayer> soundEffects;

    private boolean soundEffectsMuted = false; // Flag to mute/unmute sound effects
    private boolean musicMuted = false; // Flag to mute/unmute music

    // Private constructor to enforce singleton pattern
    private SoundManager() {
        soundEffects = new HashMap<>();
        loadSounds();
    }

    // Public method to get the single instance of SoundManager
    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    private void loadSounds() {
        try {
            // Load shooting sound using AudioClip for low latency
            shootClip = new AudioClip(getClass().getResource(SHOOT_SOUND_PATH).toExternalForm());
            if (shootClip != null) {
                System.out.println("Loaded shooting sound from: " + SHOOT_SOUND_PATH);
            } else {
                System.err.println("Failed to load shooting sound.");
            }

            // Load power-up sound using AudioClip for low latency
            powerUpClip = new AudioClip(getClass().getResource(POWER_UP_SOUND_PATH).toExternalForm());
            if (powerUpClip != null) {
                System.out.println("Loaded power-up sound from: " + POWER_UP_SOUND_PATH);
            } else {
                System.err.println("Failed to load power-up sound.");
            }

            // Load win sound
            Media winMedia = new Media(getClass().getResource(WIN_SOUND_PATH).toExternalForm());
            soundEffects.put("win", new MediaPlayer(winMedia));
            System.out.println("Loaded win sound from: " + WIN_SOUND_PATH);

            // Load game over sound
            Media gameOverMedia = new Media(getClass().getResource(GAME_OVER_SOUND_PATH).toExternalForm());
            soundEffects.put("gameOver", new MediaPlayer(gameOverMedia));
            System.out.println("Loaded game over sound from: " + GAME_OVER_SOUND_PATH);
        } catch (Exception e) {
            System.err.println("Error loading sounds.");
            e.printStackTrace();
        }
    }

    public void setSoundEffectsMuted(boolean muted) {
        soundEffectsMuted = muted;

        // Mute or unmute AudioClips
        if (shootClip != null) {
            shootClip.setVolume(soundEffectsMuted ? 0 : 1);
        }
        if (powerUpClip != null) {
            powerUpClip.setVolume(soundEffectsMuted ? 0 : 1);
        }

        // Mute or unmute MediaPlayers
        for (MediaPlayer mediaPlayer : soundEffects.values()) {
            mediaPlayer.setVolume(soundEffectsMuted ? 0 : 1);
        }
        System.out.println("Sound effects muted: " + soundEffectsMuted);
    }

    public void setMusicMuted(boolean muted) {
        musicMuted = muted;

        if (soundEffects.containsKey("backgroundMusic")) {
            MediaPlayer backgroundMusic = soundEffects.get("backgroundMusic");
            backgroundMusic.setVolume(musicMuted ? 0 : 1);
        }

        System.out.println("Background music muted: " + musicMuted);
    }

    public boolean isMusicMuted() {
        return musicMuted;
    }

    public void playShootSound() {
        if (!soundEffectsMuted && shootClip != null) {
            try {
                System.out.println("Playing shoot sound.");
                shootClip.play();
            } catch (Exception e) {
                System.err.println("Error playing shoot sound.");
                e.printStackTrace();
            }
        }
    }

    public void playPowerUpSound() {
        if (!soundEffectsMuted && powerUpClip != null) {
            try {
                System.out.println("Playing power-up sound.");
                powerUpClip.play();
            } catch (Exception e) {
                System.err.println("Error playing power-up sound.");
                e.printStackTrace();
            }
        }
    }

    public void playSound(String soundName) {
        if (soundEffectsMuted) {
            System.out.println("Sound effects muted, not playing: " + soundName);
            return;
        }

        if (soundEffects.containsKey(soundName)) {
            try {
                MediaPlayer player = soundEffects.get(soundName);
                System.out.println("Playing sound: " + soundName);
                player.stop();  // Stop the sound if it is already playing
                player.seek(javafx.util.Duration.ZERO); // Reset to the start of the sound
                player.play(); // Play the sound
            } catch (Exception e) {
                System.err.println("Error playing sound: " + soundName);
                e.printStackTrace();
            }
        } else {
            System.err.println("Sound not found: " + soundName);
        }
    }


    // Additional method to get the current mute status of sound effects
    public boolean isSoundEffectsMuted() {
        return soundEffectsMuted;
    }
}
