package com.example.demo.utils;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.HashMap;
import java.util.Map;

/**
 * Singleton class to manage game sounds and music.
 * Provides functionality to play, mute, and manage sound effects and background music.
 */
public class SoundManager {

    private static SoundManager instance; // Singleton instance

    private static final String SHOOT_SOUND_PATH = "/com/example/demo/sounds/Shoot.mp3";
    private static final String WIN_SOUND_PATH = "/com/example/demo/sounds/Win.mp3";
    private static final String GAME_OVER_SOUND_PATH = "/com/example/demo/sounds/GameOver.mp3";
    private static final String POWER_UP_SOUND_PATH = "/com/example/demo/sounds/Spreadshot.mp3";

    private AudioClip shootClip;         // Low-latency clip for shooting sound
    private AudioClip powerUpClip;       // Low-latency clip for power-up sound
    private final Map<String, MediaPlayer> soundEffects; // Map to store MediaPlayer objects

    private boolean soundEffectsMuted = false; // Mute status for sound effects
    private boolean musicMuted = false;        // Mute status for music

    /**
     * Private constructor to enforce the singleton pattern.
     * Loads sounds into memory for playback.
     */
    private SoundManager() {
        soundEffects = new HashMap<>();
        loadSounds();
    }

    /**
     * Returns the singleton instance of SoundManager.
     *
     * @return The SoundManager instance.
     */
    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    /**
     * Loads all sounds into memory.
     */
    private void loadSounds() {
        try {
            // Load shoot sound
            shootClip = loadAudioClip(SHOOT_SOUND_PATH);

            // Load power-up sound
            powerUpClip = loadAudioClip(POWER_UP_SOUND_PATH);

            // Load win sound
            soundEffects.put("win", loadMediaPlayer(WIN_SOUND_PATH));

            // Load game over sound
            soundEffects.put("gameOver", loadMediaPlayer(GAME_OVER_SOUND_PATH));

        } catch (Exception e) {
            System.err.println("Error loading sounds.");
            e.printStackTrace();
        }
    }

    /**
     * Loads an AudioClip from the given path.
     *
     * @param path The file path of the audio clip.
     * @return The loaded AudioClip.
     */
    private AudioClip loadAudioClip(String path) {
        try {
            AudioClip clip = new AudioClip(getClass().getResource(path).toExternalForm());
            System.out.println("Loaded sound: " + path);
            return clip;
        } catch (Exception e) {
            System.err.println("Failed to load sound: " + path);
            return null;
        }
    }

    /**
     * Loads a MediaPlayer from the given path.
     *
     * @param path The file path of the media file.
     * @return The loaded MediaPlayer.
     */
    private MediaPlayer loadMediaPlayer(String path) {
        try {
            Media media = new Media(getClass().getResource(path).toExternalForm());
            MediaPlayer player = new MediaPlayer(media);
            System.out.println("Loaded sound: " + path);
            return player;
        } catch (Exception e) {
            System.err.println("Failed to load sound: " + path);
            return null;
        }
    }

    /**
     * Plays the shoot sound if not muted.
     */
    public void playShootSound() {
        if (!soundEffectsMuted && shootClip != null) {
            shootClip.play();
        }
    }

    /**
     * Plays the power-up sound if not muted.
     */
    public void playPowerUpSound() {
        if (!soundEffectsMuted && powerUpClip != null) {
            powerUpClip.play();
        }
    }

    /**
     * Plays a specific sound effect by name.
     *
     * @param soundName The name of the sound to play.
     */
    public void playSound(String soundName) {
        if (soundEffectsMuted) return;

        MediaPlayer player = soundEffects.get(soundName);
        if (player != null) {
            player.stop();
            player.seek(javafx.util.Duration.ZERO);
            player.play();
        } else {
            System.err.println("Sound not found: " + soundName);
        }
    }

    /**
     * Sets the mute state for sound effects.
     *
     * @param muted True to mute sound effects, false to unmute.
     */
    public void setSoundEffectsMuted(boolean muted) {
        soundEffectsMuted = muted;

        // Adjust volume of AudioClips
        setAudioClipVolume(shootClip, muted);
        setAudioClipVolume(powerUpClip, muted);

        // Adjust volume of MediaPlayers
        soundEffects.values().forEach(player -> player.setVolume(muted ? 0 : 1));
    }

    /**
     * Sets the mute state for background music.
     *
     * @param muted True to mute music, false to unmute.
     */
    public void setMusicMuted(boolean muted) {
        musicMuted = muted;
        MediaPlayer backgroundMusic = soundEffects.get("backgroundMusic");
        if (backgroundMusic != null) {
            backgroundMusic.setVolume(muted ? 0 : 1);
        }
    }

    /**
     * Checks if music is muted.
     *
     * @return True if music is muted, false otherwise.
     */
    public boolean isMusicMuted() {
        return musicMuted;
    }

    /**
     * Checks if sound effects are muted.
     *
     * @return True if sound effects are muted, false otherwise.
     */
    public boolean isSoundEffectsMuted() {
        return soundEffectsMuted;
    }

    /**
     * Sets the volume of an AudioClip.
     *
     * @param clip  The AudioClip to adjust.
     * @param muted True to mute the clip, false to set full volume.
     */
    private void setAudioClipVolume(AudioClip clip, boolean muted) {
        if (clip != null) {
            clip.setVolume(muted ? 0 : 1);
        }
    }
}
