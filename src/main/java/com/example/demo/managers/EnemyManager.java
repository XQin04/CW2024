package com.example.demo.managers;


import com.example.demo.actors.ActiveActorDestructible;
import javafx.scene.Group;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manages all enemy units in the game.
 */
public class EnemyManager {
    private final List<ActiveActorDestructible> enemyUnits;
    private final Group root;

    public EnemyManager(Group root) {
        this.root = root;
        this.enemyUnits = new ArrayList<>();
    }

    // Add an enemy to the scene and track it
    public void addEnemy(ActiveActorDestructible enemy) {
        if (enemy != null) {
            enemyUnits.add(enemy);
            root.getChildren().add(enemy);
        }
    }

    // Update all enemies
    public void updateEnemies() {
        enemyUnits.forEach(ActiveActorDestructible::updateActor);
    }

    // Remove destroyed enemies
    public void removeDestroyedEnemies() {
        List<ActiveActorDestructible> destroyed = enemyUnits.stream()
                .filter(ActiveActorDestructible::isDestroyed)
                .collect(Collectors.toList());
        root.getChildren().removeAll(destroyed);
        enemyUnits.removeAll(destroyed);
    }

    // Clear all enemies from the scene and the list
    public void clearAllEnemies() {
        root.getChildren().removeAll(enemyUnits);
        enemyUnits.clear();
    }

    // Get the current list of enemies
    public List<ActiveActorDestructible> getEnemies() {
        return enemyUnits;
    }

    // Get the count of active enemies
    public int getEnemyCount() {
        return enemyUnits.size();
    }
}
