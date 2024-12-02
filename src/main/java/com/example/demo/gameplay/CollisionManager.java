package com.example.demo.gameplay;

import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.actors.BossSpider;

import java.util.List;

public class CollisionManager {
    private final List<ActiveActorDestructible> friendlyUnits;
    private final List<ActiveActorDestructible> enemyUnits;
    private final List<ActiveActorDestructible> userProjectiles;
    private final List<ActiveActorDestructible> enemyProjectiles;
    private final List<ActiveActorDestructible> powerUps;
    private final ActiveActorDestructible user;

    public CollisionManager(
            List<ActiveActorDestructible> friendlyUnits,
            List<ActiveActorDestructible> enemyUnits,
            List<ActiveActorDestructible> userProjectiles,
            List<ActiveActorDestructible> enemyProjectiles,
            List<ActiveActorDestructible> powerUps,
            ActiveActorDestructible user
    ) {
        this.friendlyUnits = friendlyUnits;
        this.enemyUnits = enemyUnits;
        this.userProjectiles = userProjectiles;
        this.enemyProjectiles = enemyProjectiles;
        this.powerUps = powerUps;
        this.user = user;
    }

    public void handleSpiderCollisions() {
        handleCollisions(friendlyUnits, enemyUnits);
    }

    public void handleUserProjectileCollisions() {
        handleCollisions(userProjectiles, enemyUnits);
    }

    public void handleEnemyProjectileCollisions() {
        for (ActiveActorDestructible projectile : enemyProjectiles) {
            if (projectile.getBoundsInParent().intersects(user.getBoundsInParent())) {
                projectile.takeDamage();
                user.takeDamage();
            }
        }
    }

    public void handlePowerUpCollisions() {
        for (ActiveActorDestructible powerUp : powerUps) {
            if (powerUp.getBoundsInParent().intersects(user.getBoundsInParent())) {
                powerUp.takeDamage(); // Modify if you need custom activation logic.
            }
        }
    }

    private void handleCollisions(List<ActiveActorDestructible> projectiles, List<ActiveActorDestructible> enemies) {
        for (ActiveActorDestructible projectile : projectiles) {
            for (ActiveActorDestructible enemy : enemies) {
                if (enemy instanceof BossSpider boss) {
                    if (boss.getCustomHitbox().intersects(projectile.getBoundsInParent())) {
                        projectile.takeDamage();
                        boss.takeDamage();
                    }
                } else if (enemy.getBoundsInParent().intersects(projectile.getBoundsInParent())) {
                    projectile.takeDamage();
                    enemy.takeDamage();
                }
            }
        }
    }
}
