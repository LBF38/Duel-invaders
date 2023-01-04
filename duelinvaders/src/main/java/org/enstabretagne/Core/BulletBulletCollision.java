package org.enstabretagne.Core;

import static com.almasb.fxgl.dsl.FXGL.play;
import static com.almasb.fxgl.dsl.FXGL.spawn;

import org.enstabretagne.Component.EntityType;
import org.enstabretagne.Utils.assetNames;
import org.enstabretagne.Utils.entityNames;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;

/**
 * Gestion des collisions entre les tirs des joueurs
 * En cas de collision, les tirs disparaissent
 *
 * @author jufch, MathieuDFS
 * @since 0.2.0
 */
public class BulletBulletCollision extends CollisionHandler {
    /**
     * Constructeur de la classe BulletBulletCollision
     *
     */
    public BulletBulletCollision() {
        super(EntityType.BULLET, EntityType.BULLET);
    }

    /**
     * Gestion des collisions entre les tirs des joueurs
     */
    @Override
    protected void onCollisionBegin(Entity bullet1, Entity bullet2) {
        bullet1.removeFromWorld();
        bullet2.removeFromWorld();
        spawn(entityNames.EXPLOSION_ALIEN, bullet1.getPosition());
        play(assetNames.sounds.EXPLOSION_ALIEN);
    }
}
