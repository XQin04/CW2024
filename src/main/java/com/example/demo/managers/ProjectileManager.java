package com.example.demo.managers;

import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.actors.FighterSpider;
import javafx.scene.Group;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manages all projectiles in the game, including user and enemy projectiles.
 */
public class ProjectileManager {

    // Singleton instance
    private static ProjectileManager instance;

    private List<ActiveActorDestructible> userProjectiles;
    private List<ActiveActorDestructible> enemyProjectiles;
    private Group root;

    // Private constructor for Singleton pattern
    private ProjectileManager() {
        this.userProjectiles = new ArrayList<>();
        this.enemyProjectiles = new ArrayList<>();
    }

    // Public method to get the Singleton instance
    public static ProjectileManager getInstance() {
        if (instance == null) {
            instance = new ProjectileManager();
        }
        return instance;
    }

    // Initialize the root (must be called before usage)
    public void initialize(Group root) {
        this.root = root;
    }

    // Adds a user projectile to the scene and tracks it
    public void addUserProjectile(ActiveActorDestructible projectile) {
        if (projectile != null && root != null) {
            userProjectiles.add(projectile);
            root.getChildren().add(projectile);
        }
    }

    // Adds an enemy projectile to the scene and tracks it
    public void addEnemyProjectile(ActiveActorDestructible projectile) {
        if (projectile != null && root != null) {
            enemyProjectiles.add(projectile);
            root.getChildren().add(projectile);
        }
    }

    // Updates all projectiles
    public void updateProjectiles() {
        userProjectiles.forEach(ActiveActorDestructible::updateActor);
        enemyProjectiles.forEach(ActiveActorDestructible::updateActor);
    }

    // Removes destroyed projectiles from the scene and tracking lists
    public void removeDestroyedProjectiles() {
        removeDestroyed(userProjectiles);
        removeDestroyed(enemyProjectiles);
    }

    // Helper method to remove destroyed projectiles
    private void removeDestroyed(List<ActiveActorDestructible> projectiles) {
        List<ActiveActorDestructible> destroyed = projectiles.stream()
                .filter(ActiveActorDestructible::isDestroyed)
                .collect(Collectors.toList());
        if (root != null) {
            root.getChildren().removeAll(destroyed);
        }
        projectiles.removeAll(destroyed);
    }

    // Generates enemy projectiles based on the enemy units
    public void generateEnemyProjectiles(List<ActiveActorDestructible> enemies) {
        enemies.stream()
                .filter(enemy -> enemy instanceof FighterSpider)
                .map(enemy -> ((FighterSpider) enemy).fireProjectile())
                .forEach(this::addEnemyProjectile);
    }

    // Handles collisions between projectiles and enemies
    public void handleCollisions(CollisionManager collisionManager, List<ActiveActorDestructible> enemies) {
        collisionManager.handleUserProjectileCollisions(userProjectiles, enemies);
        collisionManager.handleEnemyProjectileCollisions(enemyProjectiles);
    }

    // Clears all projectiles from the scene and lists
    public void clearAllProjectiles() {
        if (root != null) {
            root.getChildren().removeAll(userProjectiles);
            root.getChildren().removeAll(enemyProjectiles);
        }
        userProjectiles.clear();
        enemyProjectiles.clear();
    }
}
