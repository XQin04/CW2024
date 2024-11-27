package com.example.demo;

public class SpreadshotPowerUp extends PowerUp {
    private static final String SPREADSHOT_IMAGE = "spreadshot.png";

    public SpreadshotPowerUp(double initialX, double initialY) {
        super(SPREADSHOT_IMAGE, initialX, initialY);
    }

    @Override
    public void activate(UserPlane user) {
        System.out.println("Spreadshot Activated!");
        user.activateOneTimeSpreadshot(); // Enable spreadshot for the next shot
    }

}



