package com.example.demo.powerups;

import com.example.demo.actors.UserPlane;

public class SpreadshotPowerUp extends PowerUp {
    private static final String SPREADSHOT_IMAGE = "spreadshot.png";

    public SpreadshotPowerUp(double initialX, double initialY) {
        super(SPREADSHOT_IMAGE, initialX, initialY);
    }

    @Override
    public void activate(UserPlane user) {
        user.activateOneTimeSpreadshot(); // Enable spreadshot for the next shot
    }

}



