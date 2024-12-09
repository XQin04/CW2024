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
    private static EnemyManager instance; // Singleton instance
    private List<ActiveActorDestructible> enemyUnits;
    private Group root;

    // Private constructor to enforce Singleton pattern
    private EnemyManager() {
        this.enemyUnits = new ArrayList<>();
    }

    /**
     * Get the singleton instance of EnemyManager.
     *
     * @return the EnemyManager instance
     */
    public static EnemyManager getInstance() {
        if (instance == null) {
            instance = new EnemyManager();
        }
        return instance;
    }

    /**
     * Initialize the EnemyManager with a Group object.
     *
     * @param root the Group object for managing enemy nodes
     */
    public void initialize(Group root) {
        this.root = root;
        this.enemyUnits = new ArrayList<>(); // Reset the list for the new level
    }

    // Add an enemy to the scene and track it
    public void addEnemy(ActiveActorDestructible enemy) {
        if (enemy != null) {
            enemyUnits.add(enemy);
            if (root != null) {
                root.getChildren().add(enemy);
            }
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
        if (root != null) {
            root.getChildren().removeAll(destroyed);
        }
        enemyUnits.removeAll(destroyed);
    }

    // Clear all enemies from the scene and the list
    public void clearAllEnemies() {
        if (root != null) {
            root.getChildren().removeAll(enemyUnits);
        }
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
