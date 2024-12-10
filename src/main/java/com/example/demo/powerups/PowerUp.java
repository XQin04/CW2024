package com.example.demo.powerups;

import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.actors.UserSuperman;

/**
 * Represents a collectible power-up item in the game.
 * <p>
 * Power-ups provide temporary abilities or boosts to the player
 * when collected. This class defines the basic behavior and
 * interactions for power-up items.
 * </p>
 */
public class PowerUp extends ActiveActorDestructible {

    // Constants for power-up properties
    private static final int IMAGE_HEIGHT = 40;      // Height of the power-up image
    private static final double FALL_SPEED = 3.0;   // Vertical falling speed of the power-up

    /**
     * Constructs a PowerUp instance with the specified image and initial position.
     *
     * @param imageName The file name of the image representing the power-up.
     * @param initialX  The initial X-coordinate where the power-up spawns.
     * @param initialY  The initial Y-coordinate where the power-up spawns.
     */
    public PowerUp(String imageName, double initialX, double initialY) {
        super(imageName, IMAGE_HEIGHT, initialX, initialY);
    }

    /**
     * Updates the power up state on each frame.
     * <p>
     * This method makes the power-up fall down the screen
     * and destroys it if it moves out of the visible game bounds.
     * </p>
     */
    @Override
    public void updateActor() {
        moveVertically(FALL_SPEED); // Make the power-up fall
        if (isOutOfBounds()) {
            destroy(); // Remove the power-up if it falls out of bounds
        }
    }

    /**
     * Updates the position of the power-up.
     * <p>
     * This method moves the power-up vertically downward
     * at a constant rate defined by {@link #FALL_SPEED}.
     * </p>
     */
    @Override
    public void updatePosition() {
        moveVertically(FALL_SPEED); // Move the power-up down the screen
    }

    /**
     * Handles damage logic for the power-up.
     * <p>
     * By default, this destroys the power-up, simulating its removal from the game.
     * </p>
     */
    @Override
    public void takeDamage() {
        destroy(); // Destroy the power-up when it "takes damage"
    }

    /**
     * Activates the effect of the power-up upon collection by the player.
     *
     * @param user The {@link UserSuperman} instance collecting the power-up.
     */
    public void activate(UserSuperman user) {
        // Check if this power-up is a spread shot power-up and activate its effect
        if ("spreadshot.png".equals(getImage().getUrl())) { // Adjust with your actual image path
            user.activateOneTimeSpreadshot(); // Grant spread shot ability to the player
        }
        destroy(); // Remove the power-up after activation
    }

    /**
     * Determines if the power-up has moved out of the visible game area.
     *
     * @return True if the power-up has fallen below the screen; otherwise, false.
     */
    private boolean isOutOfBounds() {
        return getTranslateY() > 650; // Replace 650 with your game's screen height limit if needed
    }
}
