package com.example.demo.powerups;

import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.actors.UserSuperman;

/**
 * Represents a power-up item in the game.
 * Power-ups provide special abilities or boosts when collected by the user.
 */
public class PowerUp extends ActiveActorDestructible {

    private static final int IMAGE_HEIGHT = 40;
    private static final double FALL_SPEED = 3.0; // Speed at which the power-up falls

    /**
     * Constructor for creating a power-up.
     *
     * @param imageName The name of the image file representing the power-up.
     * @param initialX  The initial X-coordinate of the power-up.
     * @param initialY  The initial Y-coordinate of the power-up.
     */
    public PowerUp(String imageName, double initialX, double initialY) {
        super(imageName, IMAGE_HEIGHT, initialX, initialY);
    }

    /**
     * Updates the actor's behavior on each frame.
     * Moves the power-up downwards and destroys it if it falls below the screen.
     */
    @Override
    public void updateActor() {
        moveVertically(FALL_SPEED); // Power-up falls slowly
        if (isOutOfBounds()) {
            destroy();
        }
    }

    /**
     * Updates the position of the power-up, making it fall downwards.
     */
    @Override
    public void updatePosition() {
        moveVertically(FALL_SPEED); // Moves down at a constant rate
    }

    /**
     * Handles what happens when the power-up "takes damage."
     * By default, it destroys the power-up.
     */
    @Override
    public void takeDamage() {
        destroy(); // Simply destroy the power-up if it "takes damage"
    }

    /**
     * Activates the effect of the power-up when collected by the user.
     *
     * @param user The user superman that collects the power-up.
     */
    public void activate(UserSuperman user) {
        // Check if the power-up is a spreadshot and apply its effect
        if ("spreadshot.png".equals(getImage().getUrl())) { // Replace with your spreadshot image path
            user.activateOneTimeSpreadshot(); // Activate the spreadshot power-up
        }

        destroy(); // Remove the power-up after activation
    }

    /**
     * Checks if the power-up has fallen out of bounds.
     *
     * @return True if the power-up is below the screen; otherwise, false.
     */
    private boolean isOutOfBounds() {
        return getTranslateY() > 650;
    }
}
