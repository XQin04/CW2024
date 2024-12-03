package com.example.demo.gameplay;

import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.powerups.PowerUp;
import com.example.demo.utils.CollisionManager;
import javafx.scene.Group;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manages all power-ups in the game, including spawning, updating, and collisions.
 */
public class PowerUpManager {

    private final List<ActiveActorDestructible> powerUps;
    private final Group root;

    public PowerUpManager(Group root) {
        this.powerUps = new ArrayList<>();
        this.root = root;
    }

    public void addPowerUp(PowerUp powerUp) {
        powerUps.add(powerUp);
        root.getChildren().add(powerUp);
    }

    public void updatePowerUps() {
        powerUps.forEach(ActiveActorDestructible::updateActor);
    }

    public void removeDestroyedPowerUps() {
        List<ActiveActorDestructible> destroyedPowerUps = powerUps.stream()
                .filter(ActiveActorDestructible::isDestroyed)
                .collect(Collectors.toList());

        root.getChildren().removeAll(destroyedPowerUps);
        powerUps.removeAll(destroyedPowerUps);
    }

    public void handlePowerUpCollisions(CollisionManager collisionManager) {
        collisionManager.handlePowerUpCollisions(powerUps);
    }

    public List<ActiveActorDestructible> getPowerUps() {
        return powerUps;
    }
}
