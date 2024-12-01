package com.example.demo.powerups;

import com.example.demo.actors.UserPlane;

/**
 * Represents a spreadshot power-up in the game.
 * When collected by the user, it activates the spreadshot ability for a limited time or number of shots.
 */
public class SpreadshotPowerUp extends PowerUp {

    private static final String SPREADSHOT_IMAGE = "spreadshot.png"; // Path to the spreadshot image

    /**
     * Constructor for creating a spreadshot power-up.
     *
     * @param initialX The initial X-coordinate of the power-up.
     * @param initialY The initial Y-coordinate of the power-up.
     */
    public SpreadshotPowerUp(double initialX, double initialY) {
        super(SPREADSHOT_IMAGE, initialX, initialY);
    }

    /**
     * Activates the spreadshot effect when collected by the user.
     *
     * @param user The user plane that collects the power-up.
     */
    @Override
    public void activate(UserPlane user) {
        user.activateOneTimeSpreadshot(); // Activates spreadshot for the user
    }
}
