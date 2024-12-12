package com.example.demo;

import javafx.application.Platform;

import java.util.concurrent.CountDownLatch;

/**
 * Utility class to initialize JavaFX Toolkit for tests or applications.
 */
public class JavaFXInitializer {
    private static boolean initialized = false;

    /**
     * Ensures JavaFX Toolkit is initialized once.
     */
    public static void initialize() {
        if (!initialized) {
            CountDownLatch latch = new CountDownLatch(1);
            new Thread(() -> {
                Platform.startup(latch::countDown); // Start JavaFX toolkit
            }).start();
            try {
                latch.await(); // Wait for JavaFX to initialize
            } catch (InterruptedException e) {
                throw new RuntimeException("JavaFX initialization interrupted", e);
            }
            initialized = true;
        }
    }
}
