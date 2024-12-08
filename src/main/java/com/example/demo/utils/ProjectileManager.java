package com.example.demo.utils;

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

    private final List<ActiveActorDestructible> userProjectiles;
    private final List<ActiveActorDestructible> enemyProjectiles;
    private final Group root;

    public ProjectileManager(Group root) {
        this.root = root;
        this.userProjectiles = new ArrayList<>();
        this.enemyProjectiles = new ArrayList<>();
    }

    // Adds a user projectile to the scene and tracks it
    public void addUserProjectile(ActiveActorDestructible projectile) {
        if (projectile != null) {
            userProjectiles.add(projectile);
            root.getChildren().add(projectile);
        }
    }

    // Adds an enemy projectile to the scene and tracks it
    public void addEnemyProjectile(ActiveActorDestructible projectile) {
        if (projectile != null) {
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
        root.getChildren().removeAll(destroyed);
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
        root.getChildren().removeAll(userProjectiles);
        root.getChildren().removeAll(enemyProjectiles);
        userProjectiles.clear();
        enemyProjectiles.clear();
    }
}
