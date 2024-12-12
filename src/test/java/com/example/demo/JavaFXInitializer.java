package com.example.demo;

import javafx.application.Platform;

/**
 * Utility class to ensure JavaFX is initialized for tests or applications.
 */
public class JavaFXInitializer {
    private static boolean initialized = false;

    public static void initialize() {
        if (!initialized) {
            Platform.startup(() -> {}); // Start the JavaFX toolkit
            initialized = true;
        }
    }
}