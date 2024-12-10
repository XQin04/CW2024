package com.example.demo.gameplay.managers;

import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.managers.CollisionManager;
import com.example.demo.powerups.PowerUp;
import javafx.scene.Group;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages all power-ups in the game, including spawning, updating, and handling collisions.
 * <p>
 * This class implements the Singleton pattern to ensure that only one instance manages power-ups.
 * </p>
 */
public class PowerUpManager {

    private static PowerUpManager instance; // Singleton instance
    private List<ActiveActorDestructible> powerUps; // List of active power-ups in the game
    private Group root; // Root group for adding/removing power-ups from the scene graph

    /**
     * Private constructor to enforce the Singleton pattern.
     * Initializes an empty list of power-ups.
     */
    private PowerUpManager() {
        this.powerUps = new ArrayList<>();
    }

    /**
     * Returns the Singleton instance of the PowerUpManager.
     * Creates a new instance if one does not already exist.
     *
     * @return The Singleton instance of PowerUpManager.
     */
    public static PowerUpManager getInstance() {
        if (instance == null) {
            instance = new PowerUpManager();
        }
        return instance;
    }

    /**
     * Initializes the PowerUpManager with the root group.
     * This method should be called once when the level is initialized.
     *
     * @param root The root group to which power-ups will be added in the scene.
     */
    public void initialize(Group root) {
        this.root = root;
        this.powerUps = new ArrayList<>();
    }

    /**
     * Adds a new power-up to the game.
     * The power-up is added to the internal list and the scene graph.
     *
     * @param powerUp The power-up to be added.
     */
    public void addPowerUp(PowerUp powerUp) {
        powerUps.add(powerUp); // Add the power-up to the list
        if (root != null) {
            root.getChildren().add(powerUp); // Add the power-up to the scene graph
        }
    }

    /**
     * Updates all active power-ups.
     * This method should be called in the game loop to update the state or position of power-ups.
     */
    public void updatePowerUps() {
        powerUps.forEach(ActiveActorDestructible::updateActor); // Update each power-up
    }

    /**
     * Removes all destroyed power-ups from the game.
     * Power-ups marked as destroyed are removed from both the internal list and the scene graph.
     */
    public void removeDestroyedPowerUps() {
        // Find all destroyed power-ups
        List<ActiveActorDestructible> destroyedPowerUps = powerUps.stream()
                .filter(ActiveActorDestructible::isDestroyed)
                .toList(); // Replaced `collect(Collectors.toList())` with `toList()`

        if (root != null) {
            root.getChildren().removeAll(destroyedPowerUps); // Remove destroyed power-ups from the scene graph
        }
        powerUps.removeAll(destroyedPowerUps); // Remove destroyed power-ups from the list
    }

    /**
     * Handles collisions between the player and power-ups.
     * Delegates collision detection and response to the CollisionManager.
     *
     * @param collisionManager The CollisionManager responsible for detecting collisions.
     */
    public void handlePowerUpCollisions(CollisionManager collisionManager) {
        collisionManager.handlePowerUpCollisions(powerUps); // Delegate collision handling to CollisionManager
    }

    /**
     * Clears all power-ups from the game.
     * Removes all power-ups from both the internal list and the scene graph.
     */
    public void clearAllPowerUps() {
        if (root != null) {
            root.getChildren().removeAll(powerUps); // Remove power-ups from the scene graph
        }
        powerUps.clear(); // Clear the internal list of power-ups
    }
}
