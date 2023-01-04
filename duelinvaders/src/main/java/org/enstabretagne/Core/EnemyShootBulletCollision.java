package org.enstabretagne.Core;

import static com.almasb.fxgl.dsl.FXGL.play;
import static com.almasb.fxgl.dsl.FXGL.spawn;

import org.enstabretagne.Component.EntityType;
import org.enstabretagne.Utils.assetNames;
import org.enstabretagne.Utils.entityNames;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;

/**
 * Gestion des collisions entre les tirs des joueurs et les lasers des ennemis
 * En cas de collision, le tir et le laser disparaissent
 *
 * @author jufch, MathieuDFS
 * @since 0.2.0
 */
public class EnemyShootBulletCollision extends CollisionHandler {
    /**
     * Constructeur de la classe EnemyShootBulletCollision
     */
    public EnemyShootBulletCollision() {
        super(EntityType.BULLET, EntityType.ENEMY_SHOOT);
    }

    /**
     * Gestion des collisions entre les tirs des aliens et les tirs des joueurs
     */
    @Override
    protected void onCollisionBegin(Entity bullet, Entity enemyShoot) {
        bullet.removeFromWorld();
        enemyShoot.removeFromWorld();
        spawn(entityNames.EXPLOSION_ALIEN, bullet.getPosition());
        play(assetNames.sounds.EXPLOSION_ALIEN);
    }
}

