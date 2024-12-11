package com.example.demo.managers;

import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.actors.enemies.BossSpider;
import com.example.demo.actors.player.UserSuperman;
import com.example.demo.powerups.PowerUp;
import com.example.demo.powerups.SpreadshotPowerUp;

import java.util.List;

/**
 * Manages collision detection and response in the game.
 * <p>
 * This class handles interactions between various game entities such as user characters, enemies,
 * projectiles, and power-ups. It provides utility methods for detecting and responding to collisions
 * with custom logic defined through functional interfaces.
 * </p>
 */
public class CollisionManager {

    private final UserSuperman user;       // Reference to the player's character
    private final SoundManager soundManager; // Reference to the SoundManager for audio effects

    /**
     * Constructs a CollisionManager with the specified user and SoundManager.
     *
     * @param user         The player's character.
     * @param soundManager The manager for handling game sound effects.
     */
    public CollisionManager(UserSuperman user, SoundManager soundManager) {
        this.user = user;
        this.soundManager = soundManager;
    }

    /**
     * Handles generic collisions between two lists of game entities.
     * <p>
     * This method iterates through two lists of entities and applies a custom collision
     * action whenever an intersection is detected.
     * </p>
     *
     * @param actorsA     The first list of entities.
     * @param actorsB     The second list of entities.
     * @param onCollision A functional interface defining the action to perform on collision.
     */
    private void handleCollisions(
            List<ActiveActorDestructible> actorsA,
            List<ActiveActorDestructible> actorsB,
            CollisionAction onCollision
    ) {
        for (ActiveActorDestructible actorA : actorsA) {
            for (ActiveActorDestructible actorB : actorsB) {
                if (actorA.getBoundsInParent().intersects(actorB.getBoundsInParent())) {
                    onCollision.apply(actorA, actorB);
                }
            }
        }
    }

    /**
     * Handles collisions between friendly units (e.g., user's character) and enemy units.
     * <p>
     * Both units take damage when a collision occurs.
     * </p>
     *
     * @param friendlyUnits List of friendly units.
     * @param enemyUnits    List of enemy units.
     */
    public void handleSpiderCollisions(List<ActiveActorDestructible> friendlyUnits, List<ActiveActorDestructible> enemyUnits) {
        handleCollisions(friendlyUnits, enemyUnits, (friendly, enemy) -> {
            friendly.takeDamage(); // Friendly unit takes damage
            enemy.takeDamage();    // Enemy unit takes damage
        });
    }

    /**
     * Handles collisions between user projectiles and enemy units.
     * <p>
     * Special collision logic is applied for {@link BossSpider} to account for its custom hit box.
     * </p>
     *
     * @param projectiles List of user projectiles.
     * @param enemies     List of enemy units.
     */
    public void handleUserProjectileCollisions(List<ActiveActorDestructible> projectiles, List<ActiveActorDestructible> enemies) {
        handleCollisions(projectiles, enemies, (projectile, enemy) -> {
            if (enemy instanceof BossSpider boss) {
                if (boss.getCustomHitbox().intersects(projectile.getBoundsInParent())) {
                    projectile.takeDamage(); // Destroy projectile on collision
                    boss.takeDamage();       // Damage the boss
                }
            } else {
                projectile.takeDamage(); // Destroy projectile on collision
                enemy.takeDamage();      // Damage the enemy
            }
        });
    }

    /**
     * Handles collisions between enemy projectiles and the user.
     * <p>
     * The user takes damage when hit by an enemy projectile.
     * </p>
     *
     * @param projectiles List of enemy projectiles.
     */
    public void handleEnemyProjectileCollisions(List<ActiveActorDestructible> projectiles) {
        for (ActiveActorDestructible projectile : projectiles) {
            if (projectile.getBoundsInParent().intersects(user.getBoundsInParent())) {
                projectile.takeDamage(); // Destroy the projectile
                user.takeDamage();       // Damage the user
            }
        }
    }

    /**
     * Handles collisions between the user and power-ups.
     * <p>
     * Activates the corresponding power-up effect upon collision and destroys the power-up.
     * </p>
     *
     * @param powerUps List of power-up objects.
     */
    public void handlePowerUpCollisions(List<ActiveActorDestructible> powerUps) {
        for (ActiveActorDestructible powerUp : powerUps) {
            if (powerUp.getBoundsInParent().intersects(user.getBoundsInParent())) {
                if (powerUp instanceof SpreadshotPowerUp spreadshotPowerUp) {
                    spreadshotPowerUp.activate(user); // Activate spread shot power-up
                } else if (powerUp instanceof PowerUp genericPowerUp) {
                    genericPowerUp.activate(user); // Activate generic power-up
                }

                // Play the power-up collection sound
                soundManager.playPowerUpSound();

                // Destroy the power-up after activation
                powerUp.destroy();
            }
        }
    }

    /**
     * Functional interface for defining custom collision logic.
     */
    @FunctionalInterface
    public interface CollisionAction {
        /**
         * Applies custom collision logic between two game entities.
         *
         * @param actorA The first actor involved in the collision.
         * @param actorB The second actor involved in the collision.
         */
        void apply(ActiveActorDestructible actorA, ActiveActorDestructible actorB);
    }
}
