package com.example.demo;

public class PowerUp extends ActiveActorDestructible {
    private static final int IMAGE_HEIGHT = 40;

    public PowerUp(String imageName, double initialX, double initialY) {
        super(imageName, IMAGE_HEIGHT, initialX, initialY);
    }

    @Override
    public void updateActor() {
        moveVertically(3); // Power-up falls slowly
        if (getTranslateY() > 600) { // If power-up falls below screen, destroy it
            destroy();
        }
    }

    @Override
    public void updatePosition() {
        // Update the position of the power-up by making it fall downwards
        moveVertically(3); // Moves down at a constant rate
    }

    @Override
    public void takeDamage() {
        // Power-ups generally do not take damage, so this method could destroy the power-up or do nothing.
        destroy(); // Simply destroy the power-up if it "takes damage"
    }

    // Activate power-up effect when collected by the user
    public void activate(UserPlane user) {
        if (getImage().equals("spreadshot.png")) { // Replace with your spreadshot image name
            System.out.println("Spreadshot Power-Up Activated!");
            user.activateOneTimeSpreadshot(); // Increment the spreadshot count
        } else {
            System.out.println("Power-Up Activated: Default");
            // Add any other power-up behavior if necessary
        }

        destroy(); // Remove power-up after it's collected
    }
}
