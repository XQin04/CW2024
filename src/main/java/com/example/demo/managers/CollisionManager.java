package com.example.demo.managers;

import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.actors.BossSpider;
import com.example.demo.actors.UserSuperman;
import com.example.demo.powerups.PowerUp;
import com.example.demo.powerups.SpreadshotPowerUp;

import java.util.List;

/**
 * Manages collision detection and response in the game.
 */
public class CollisionManager {

    private final UserSuperman user;
    private final SoundManager soundManager;

    public CollisionManager(UserSuperman user, SoundManager soundManager) {
        this.user = user;
        this.soundManager = soundManager;
    }

    /**
     * Handles generic collisions between two lists of actors.
     *
     * @param actorsA     The first list of actors.
     * @param actorsB     The second list of actors.
     * @param onCollision A lambda or functional interface defining what happens on collision.
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
     *
     * @param friendlyUnits List of friendly units.
     * @param enemyUnits    List of enemy units.
     */
    public void handleSpiderCollisions(List<ActiveActorDestructible> friendlyUnits, List<ActiveActorDestructible> enemyUnits) {
        handleCollisions(friendlyUnits, enemyUnits, (friendly, enemy) -> {
            friendly.takeDamage();
            enemy.takeDamage();
        });
    }

    /**
     * Handles collisions between user projectiles and enemy units.
     *
     * @param projectiles List of user projectiles.
     * @param enemies     List of enemy units.
     */
    public void handleUserProjectileCollisions(List<ActiveActorDestructible> projectiles, List<ActiveActorDestructible> enemies) {
        handleCollisions(projectiles, enemies, (projectile, enemy) -> {
            if (enemy instanceof BossSpider boss) {
                if (boss.getCustomHitbox().intersects(projectile.getBoundsInParent())) {
                    projectile.takeDamage();
                    boss.takeDamage();
                }
            } else {
                projectile.takeDamage();
                enemy.takeDamage();
            }
        });
    }

    /**
     * Handles collisions between enemy projectiles and the user.
     *
     * @param projectiles List of enemy projectiles.
     */
    public void handleEnemyProjectileCollisions(List<ActiveActorDestructible> projectiles) {
        for (ActiveActorDestructible projectile : projectiles) {
            if (projectile.getBoundsInParent().intersects(user.getBoundsInParent())) {
                projectile.takeDamage();
                user.takeDamage();
            }
        }
    }

    /**
     * Handles collisions between the user and power-ups.
     *
     * @param powerUps List of power-up objects.
     */
    public void handlePowerUpCollisions(List<ActiveActorDestructible> powerUps) {
        for (ActiveActorDestructible powerUp : powerUps) {
            if (powerUp.getBoundsInParent().intersects(user.getBoundsInParent())) {
                if (powerUp instanceof SpreadshotPowerUp spreadshotPowerUp) {
                    spreadshotPowerUp.activate(user); // Activate spreadshot power-up
                } else if (powerUp instanceof PowerUp genericPowerUp) {
                    genericPowerUp.activate(user); // Activate generic power-ups
                }

                // Play the power-up collection sound
                soundManager.playPowerUpSound();

                // Remove the power-up after activation
                powerUp.destroy();
            }
        }
    }

    /**
     * Functional interface for custom collision logic.
     */
    @FunctionalInterface
    public interface CollisionAction {
        void apply(ActiveActorDestructible actorA, ActiveActorDestructible actorB);
    }
}
