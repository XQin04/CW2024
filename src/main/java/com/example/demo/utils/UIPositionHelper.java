package com.example.demo.utils;

import javafx.scene.layout.Region;

public class UIPositionHelper {

    /**
     * Sets the position of a UI element.
     *
     * @param region The UI element to position.
     * @param x      The X-coordinate.
     * @param y      The Y-coordinate.
     */
    public static void setPosition(Region region, double x, double y) {
        region.setLayoutX(x);
        region.setLayoutY(y);
    }
}
