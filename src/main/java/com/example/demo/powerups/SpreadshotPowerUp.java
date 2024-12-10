package com.example.demo.powerups;

import com.example.demo.actors.player.UserSuperman;

/**
 * Represents a spread shot power-up in the game.
 * <p>
 * When collected by the player, this power-up activates the spread shot ability,
 * allowing the user to fire multiple projectiles in a spread pattern.
 * </p>
 */
public class SpreadshotPowerUp extends PowerUp {

    // Path to the image representing the spread shot power-up
    private static final String SPREADSHOT_IMAGE = "spreadshot.png";

    /**
     * Constructs a Spread shotPowerUp instance with the specified initial position.
     *
     * @param initialX The initial X-coordinate where the power-up spawns.
     * @param initialY The initial Y-coordinate where the power-up spawns.
     */
    public SpreadshotPowerUp(double initialX, double initialY) {
        super(SPREADSHOT_IMAGE, initialX, initialY);
    }

    /**
     * Activates the spread shot ability for the player when collected.
     * <p>
     * This method triggers the spread shot mechanic, granting the user the ability
     * to fire multiple projectiles in a single shot.
     * </p>
     *
     * @param user The {@link UserSuperman} instance collecting the power-up.
     */
    @Override
    public void activate(UserSuperman user) {
        user.activateOneTimeSpreadshot(); // Grant the spread shot ability to the user
    }
}
