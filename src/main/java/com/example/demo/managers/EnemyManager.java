package com.example.demo.managers;

import com.example.demo.actors.ActiveActorDestructible;
import javafx.scene.Group;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages all enemy units in the game.
 * <p>
 * The EnemyManager is responsible for tracking, updating, and managing the lifecycle of enemy entities
 * during gameplay. This includes adding, updating, removing, and clearing enemies from the game scene.
 * It follows the Singleton pattern to ensure there is only one instance managing all enemies.
 * </p>
 */
public class EnemyManager {

    private static EnemyManager instance; // Singleton instance of the EnemyManager
    private List<ActiveActorDestructible> enemyUnits; // List of currently active enemies
    private Group root; // Reference to the root group for rendering enemies

    /**
     * Private constructor to enforce the Singleton pattern.
     * Initializes an empty list of enemy units.
     */
    private EnemyManager() {
        this.enemyUnits = new ArrayList<>();
    }

    /**
     * Retrieves the singleton instance of EnemyManager.
     * If the instance does not exist, it is created.
     *
     * @return The singleton instance of EnemyManager.
     */
    public static EnemyManager getInstance() {
        if (instance == null) {
            instance = new EnemyManager();
        }
        return instance;
    }

    /**
     * Initializes the EnemyManager with a specified root group.
     * <p>
     * This method should be called at the start of a new level to prepare the manager
     * for managing enemies specific to that level.
     * </p>
     *
     * @param root The root {@link Group} where enemy nodes will be added.
     */
    public void initialize(Group root) {
        this.root = root;
        this.enemyUnits = new ArrayList<>(); // Reset the enemy list for the new level
    }

    /**
     * Adds an enemy to the game and tracks it within the EnemyManager.
     * The enemy is also added to the root group for rendering.
     *
     * @param enemy The enemy actor to add.
     */
    public void addEnemy(ActiveActorDestructible enemy) {
        if (enemy != null) {
            enemyUnits.add(enemy); // Add to the tracking list
            if (root != null) {
                root.getChildren().add(enemy); // Add to the scene graph
            }
        }
    }

    /**
     * Updates all active enemies in the game.
     * Each enemy's `updateActor` method is called to handle their individual behavior and movement.
     */
    public void updateEnemies() {
        enemyUnits.forEach(ActiveActorDestructible::updateActor);
    }

    /**
     * Removes all enemies that have been marked as destroyed.
     * <p>
     * Destroyed enemies are removed from both the tracking list and the root group,
     * ensuring they are no longer part of the game.
     * </p>
     */
    public void removeDestroyedEnemies() {
        // Collect destroyed enemies into a separate list
        List<ActiveActorDestructible> destroyed = enemyUnits.stream()
                .filter(ActiveActorDestructible::isDestroyed)
                .toList();

        // Remove them from the scene graph
        if (root != null) {
            root.getChildren().removeAll(destroyed);
        }

        // Remove them from the tracking list
        enemyUnits.removeAll(destroyed);
    }

    /**
     * Clears all enemies from the game.
     * <p>
     * This method removes all enemies from the tracking list and the scene graph,
     * resetting the manager for a fresh start.
     * </p>
     */
    public void clearAllEnemies() {
        if (root != null) {
            root.getChildren().removeAll(enemyUnits); // Clear all enemies from the scene
        }
        enemyUnits.clear(); // Clear the tracking list
    }

    /**
     * Retrieves the current list of active enemies.
     *
     * @return A list of active {@link ActiveActorDestructible} enemies.
     */
    public List<ActiveActorDestructible> getEnemies() {
        return enemyUnits;
    }

    /**
     * Retrieves the number of currently active enemies.
     *
     * @return The number of active enemies in the game.
     */
    public int getEnemyCount() {
        return enemyUnits.size();
    }
}
