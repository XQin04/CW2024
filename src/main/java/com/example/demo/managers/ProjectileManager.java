package com.example.demo.managers;

import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.actors.FighterSpider;
import javafx.scene.Group;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manages all projectiles in the game, including user and enemy projectiles.
 * <p>
 * This class tracks projectiles, handles updates and removals, and ensures proper collision management.
 * It uses the Singleton pattern to ensure there is only one instance of the {@code ProjectileManager}.
 * </p>
 */
public class ProjectileManager {

    // Singleton instance
    private static ProjectileManager instance;

    private List<ActiveActorDestructible> userProjectiles; // Projectiles fired by the user
    private List<ActiveActorDestructible> enemyProjectiles; // Projectiles fired by enemies
    private Group root; // Root group for rendering projectiles in the scene

    /**
     * Private constructor to enforce the Singleton pattern.
     */
    private ProjectileManager() {
        this.userProjectiles = new ArrayList<>();
        this.enemyProjectiles = new ArrayList<>();
    }

    /**
     * Retrieves the Singleton instance of the {@code ProjectileManager}.
     *
     * @return The Singleton instance of the {@code ProjectileManager}.
     */
    public static ProjectileManager getInstance() {
        if (instance == null) {
            instance = new ProjectileManager();
        }
        return instance;
    }

    /**
     * Initializes the {@code ProjectileManager} with the root group for rendering.
     * This method must be called before using the manager.
     *
     * @param root The root group to which projectiles will be added.
     */
    public void initialize(Group root) {
        this.root = root;
    }

    /**
     * Adds a user projectile to the scene and tracks it.
     *
     * @param projectile The projectile to be added.
     */
    public void addUserProjectile(ActiveActorDestructible projectile) {
        if (projectile != null && root != null) {
            userProjectiles.add(projectile);
            root.getChildren().add(projectile);
        }
    }

    /**
     * Adds an enemy projectile to the scene and tracks it.
     *
     * @param projectile The projectile to be added.
     */
    public void addEnemyProjectile(ActiveActorDestructible projectile) {
        if (projectile != null && root != null) {
            enemyProjectiles.add(projectile);
            root.getChildren().add(projectile);
        }
    }

    /**
     * Updates the state of all projectiles by invoking their {@code updateActor()} method.
     */
    public void updateProjectiles() {
        userProjectiles.forEach(ActiveActorDestructible::updateActor);
        enemyProjectiles.forEach(ActiveActorDestructible::updateActor);
    }

    /**
     * Removes destroyed projectiles from the scene and tracking lists.
     * This ensures that inactive projectiles do not clutter the scene or waste resources.
     */
    public void removeDestroyedProjectiles() {
        removeDestroyed(userProjectiles);
        removeDestroyed(enemyProjectiles);
    }

    /**
     * Helper method to remove destroyed projectiles from the specified list.
     *
     * @param projectiles The list of projectiles to check for destruction.
     */
    private void removeDestroyed(List<ActiveActorDestructible> projectiles) {
        List<ActiveActorDestructible> destroyed = projectiles.stream()
                .filter(ActiveActorDestructible::isDestroyed)
                .collect(Collectors.toList());
        if (root != null) {
            root.getChildren().removeAll(destroyed);
        }
        projectiles.removeAll(destroyed);
    }

    /**
     * Generates projectiles for enemies that are capable of firing.
     * <p>
     * This method checks each enemy in the list and invokes their {@code fireProjectile()} method.
     * If a projectile is fired, it is added to the scene and tracked.
     * </p>
     *
     * @param enemies The list of enemy actors capable of firing projectiles.
     */
    public void generateEnemyProjectiles(List<ActiveActorDestructible> enemies) {
        enemies.stream()
                .filter(enemy -> enemy instanceof FighterSpider)
                .map(enemy -> ((FighterSpider) enemy).fireProjectile())
                .forEach(this::addEnemyProjectile);
    }

    /**
     * Handles collisions between projectiles and enemy units.
     * <p>
     * Delegates collision detection to the provided {@link CollisionManager}.
     * </p>
     *
     * @param collisionManager The collision manager to handle interactions.
     * @param enemies          The list of enemies to check for collisions.
     */
    public void handleCollisions(CollisionManager collisionManager, List<ActiveActorDestructible> enemies) {
        collisionManager.handleUserProjectileCollisions(userProjectiles, enemies);
        collisionManager.handleEnemyProjectileCollisions(enemyProjectiles);
    }

    /**
     * Clears all projectiles from the scene and internal tracking lists.
     * <p>
     * This method is useful when transitioning levels or resetting the game.
     * </p>
     */
    public void clearAllProjectiles() {
        if (root != null) {
            root.getChildren().removeAll(userProjectiles);
            root.getChildren().removeAll(enemyProjectiles);
        }
        userProjectiles.clear();
        enemyProjectiles.clear();
    }
}
