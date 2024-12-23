package com.example.demo.managers;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Singleton class to manage all game sounds and music.
 * <p>
 * Provides functionality to play, mute, and manage sound effects and background music.
 * Supports preloading of audio resources for optimized playback.
 * </p>
 */
public class SoundManager {

    private static final Logger logger = Logger.getLogger(SoundManager.class.getName());
    // File paths for sound effects
    private static final String SHOOT_SOUND_PATH = "/com/example/demo/sounds/Shoot.mp3";
    private static final String WIN_SOUND_PATH = "/com/example/demo/sounds/Win.mp3";
    private static final String GAME_OVER_SOUND_PATH = "/com/example/demo/sounds/GameOver.mp3";
    private static final String POWER_UP_SOUND_PATH = "/com/example/demo/sounds/Spreadshot.mp3";

    private static SoundManager instance;
    private final Map<String, MediaPlayer> soundEffects = new HashMap<>(); // Map for other sound effects
    private AudioClip shootClip;          // Low-latency clip for shooting sound
    private AudioClip powerUpClip;        // Low-latency clip for power-up sound
    private boolean soundEffectsMuted = false; // Tracks mute status for sound effects
    private boolean musicMuted = false;        // Tracks mute status for background music

    /**
     * Private constructor to enforce the Singleton pattern.
     * Preloads audio files into memory for optimized playback during the game.
     */
    private SoundManager() {
        loadSounds();
    }

    /**
     * Retrieves the singleton instance of {@code SoundManager}.
     *
     * @return The single instance of {@code SoundManager}.
     */
    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    /**
     * Loads all sound resources into memory for playback.
     */
    private void loadSounds() {
        try {
            // Load shooting and power-up sounds as AudioClips
            shootClip = loadAudioClip(SHOOT_SOUND_PATH);
            powerUpClip = loadAudioClip(POWER_UP_SOUND_PATH);

            // Load other sound effects as MediaPlayer objects
            soundEffects.put("win", loadMediaPlayer(WIN_SOUND_PATH));
            soundEffects.put("gameOver", loadMediaPlayer(GAME_OVER_SOUND_PATH));
        } catch (Exception e) {
            logger.severe("Error loading sound resources: " + e.getMessage());
        }
    }

    /**
     * Loads an {@code AudioClip} from the given file path.
     *
     * @param path The path to the audio file.
     * @return The loaded {@code AudioClip}, or {@code null} if loading fails.
     */
    private AudioClip loadAudioClip(String path) {
        try {
            URL resource = getClass().getResource(path);
            if (resource == null) {
                logger.severe("AudioClip resource not found: " + path);
                return null;
            }
            return new AudioClip(resource.toExternalForm());
        } catch (Exception e) {
            logger.severe("Failed to load AudioClip: " + path + " - " + e.getMessage());
            return null;
        }
    }

    /**
     * Loads a {@code MediaPlayer} from the given file path.
     *
     * @param path The path to the media file.
     * @return The loaded {@code MediaPlayer}, or {@code null} if loading fails.
     */
    private MediaPlayer loadMediaPlayer(String path) {
        try {
            URL resource = getClass().getResource(path);
            if (resource == null) {
                logger.severe("MediaPlayer resource not found: " + path);
                return null;
            }
            Media media = new Media(resource.toExternalForm());
            return new MediaPlayer(media);
        } catch (Exception e) {
            logger.severe("Failed to load MediaPlayer: " + path + " - " + e.getMessage());
            return null;
        }
    }

    /**
     * Plays the shooting sound effect if sound effects are not muted.
     */
    public void playShootSound() {
        if (!soundEffectsMuted && shootClip != null) {
            shootClip.play();
        }
    }

    /**
     * Plays the power-up collection sound effect if sound effects are not muted.
     */
    public void playPowerUpSound() {
        if (!soundEffectsMuted && powerUpClip != null) {
            powerUpClip.play();
        }
    }

    /**
     * Plays a specific sound effect identified by its name.
     *
     * @param soundName The name of the sound effect to play (e.g., "win" or "gameOver").
     */
    public void playSound(String soundName) {
        if (soundEffectsMuted) return;
        MediaPlayer player = soundEffects.get(soundName);
        if (player != null) {
            player.stop();
            player.seek(javafx.util.Duration.ZERO); // Reset playback to the start
            player.play();
        } else {
            logger.warning("Sound not found: " + soundName);
        }
    }

    /**
     * Checks if background music is muted.
     *
     * @return {@code true} if music is muted, {@code false} otherwise.
     */
    public boolean isMusicMuted() {
        return musicMuted;
    }

    /**
     * Mutes or unmutes background music based on the specified parameter.
     *
     * @param muted {@code true} to mute music, {@code false} to unmute it.
     */
    public void setMusicMuted(boolean muted) {
        musicMuted = muted;
        MediaPlayer backgroundMusic = soundEffects.get("backgroundMusic");
        if (backgroundMusic != null) {
            backgroundMusic.setVolume(muted ? 0 : 1);
        }
    }

    /**
     * Checks if sound effects are muted.
     *
     * @return {@code true} if sound effects are muted, {@code false} otherwise.
     */
    public boolean isSoundEffectsMuted() {
        return soundEffectsMuted;
    }

    /**
     * Mutes or unmutes sound effects based on the specified parameter.
     *
     * @param muted {@code true} to mute sound effects, {@code false} to unmute them.
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
     * Sets the volume of an {@code AudioClip}.
     *
     * @param clip  The {@code AudioClip} whose volume is to be adjusted.
     * @param muted {@code true} to mute the clip, {@code false} to restore full volume.
     */
    private void setAudioClipVolume(AudioClip clip, boolean muted) {
        if (clip != null) {
            clip.setVolume(muted ? 0 : 1);
        }
    }
}
