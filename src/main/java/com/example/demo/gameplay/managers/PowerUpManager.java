package com.example.demo.gameplay.managers;

import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.powerups.PowerUp;
import com.example.demo.managers.CollisionManager;
import javafx.scene.Group;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manages all power-ups in the game, including spawning, updating, and collisions.
 */
public class PowerUpManager {

    private static PowerUpManager instance; // Singleton instance
    private List<ActiveActorDestructible> powerUps;
    private Group root;

    /**
     * Private constructor to enforce Singleton pattern.
     */
    private PowerUpManager() {
        this.powerUps = new ArrayList<>();
    }

    /**
     * Returns the Singleton instance of the PowerUpManager.
     */
    public static PowerUpManager getInstance() {
        if (instance == null) {
            instance = new PowerUpManager();
        }
        return instance;
    }

    /**
     * Initializes the PowerUpManager with the root group.
     * Should be called once when the level is initialized.
     *
     * @param root The root group to which power-ups will be added.
     */
    public void initialize(Group root) {
        this.root = root;
        this.powerUps = new ArrayList<>();
    }

    public void addPowerUp(PowerUp powerUp) {
        powerUps.add(powerUp);
        if (root != null) {
            root.getChildren().add(powerUp);
        }
    }

    public void updatePowerUps() {
        powerUps.forEach(ActiveActorDestructible::updateActor);
    }

    public void removeDestroyedPowerUps() {
        List<ActiveActorDestructible> destroyedPowerUps = powerUps.stream()
                .filter(ActiveActorDestructible::isDestroyed)
                .collect(Collectors.toList());

        if (root != null) {
            root.getChildren().removeAll(destroyedPowerUps);
        }
        powerUps.removeAll(destroyedPowerUps);
    }

    public void handlePowerUpCollisions(CollisionManager collisionManager) {
        collisionManager.handlePowerUpCollisions(powerUps);
    }

    /**
     * Clears all power-ups from the game.
     */
    public void clearAllPowerUps() {
        if (root != null) {
            root.getChildren().removeAll(powerUps);
        }
        powerUps.clear();
    }
}
