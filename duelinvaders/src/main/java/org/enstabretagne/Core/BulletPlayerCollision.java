
package org.enstabretagne.Core;

import static com.almasb.fxgl.dsl.FXGL.geti;
import static com.almasb.fxgl.dsl.FXGL.inc;
import static com.almasb.fxgl.dsl.FXGL.play;
import static com.almasb.fxgl.dsl.FXGL.runOnce;
import static com.almasb.fxgl.dsl.FXGL.set;
import static com.almasb.fxgl.dsl.FXGL.spawn;

import org.enstabretagne.Component.EntityType;
import org.enstabretagne.Utils.assetNames;
import org.enstabretagne.Utils.entityNames;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;

import javafx.util.Duration;

/**
 * Gestion des collisions entre les tirs des joueurs et les joueurs
 * En cas de collision, le joueur perd un point de vie
 *
 * @author jufch, MathieuDFS
 * @since 0.2.0
 */
public class BulletPlayerCollision extends CollisionHandler {
    /**
     * Constructeur de la classe BulletPlayerCollision
     *
     * @param bullet
     * @param player
     */
    public BulletPlayerCollision() {
        super(EntityType.BULLET, EntityType.PLAYER);
    }

    /**
     * Gestion des collisions entre les tirs des joueurs et les joueurs
     */
    @Override
    protected void onCollisionBegin(Entity bullet, Entity player) {
        bullet.removeFromWorld();

        inc(GameVariableNames.PLAYERS_LIVES, -1);
        if (geti(GameVariableNames.PLAYERS_LIVES) == 0) {
            spawn(entityNames.EXPLOSION_PLAYER_DEATH, player.getPosition());
            player.removeFromWorld();
            play(assetNames.sounds.EXPLOSION_PLAYER_DEATH);
            runOnce(() -> set(GameVariableNames.isGameOver, true), Duration.seconds(1));
        } else {
            spawn(entityNames.EXPLOSION_PLAYER_BULLET, bullet.getPosition());
            play(assetNames.sounds.EXPLOSION_PLAYER_LIFE);
        }
    }
}