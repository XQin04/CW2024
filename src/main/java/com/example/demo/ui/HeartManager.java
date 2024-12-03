package com.example.demo.ui;

import javafx.scene.Group;

public class HeartManager {

    private final HeartDisplay heartDisplay;

    public HeartManager(double xPosition, double yPosition, int heartsToDisplay) {
        this.heartDisplay = new HeartDisplay(xPosition, yPosition, heartsToDisplay);
    }

    /**
     * Displays the heart display by adding it to the specified root group.
     *
     * @param root The root group to which the heart display will be added.
     */
    public void showHeartDisplay(Group root) {
        root.getChildren().add(heartDisplay.getContainer());
    }

    /**
     * Removes hearts until the specified number remains.
     *
     * @param heartsRemaining The number of hearts to leave displayed.
     */
    public void removeHearts(int heartsRemaining) {
        int currentNumberOfHearts = heartDisplay.getContainer().getChildren().size();
        for (int i = 0; i < currentNumberOfHearts - heartsRemaining; i++) {
            heartDisplay.removeHeart();
        }
    }
}
