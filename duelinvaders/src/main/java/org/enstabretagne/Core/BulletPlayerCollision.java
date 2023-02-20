
package org.enstabretagne.Core;

import static com.almasb.fxgl.dsl.FXGL.play;
import static com.almasb.fxgl.dsl.FXGL.runOnce;
import static com.almasb.fxgl.dsl.FXGL.set;
import static com.almasb.fxgl.dsl.FXGL.spawn;

import org.enstabretagne.Component.PlayerComponent;
import org.enstabretagne.Utils.EntityType;
import org.enstabretagne.Utils.GameVariableNames;
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
        PlayerComponent playerComponent = player.getComponent(PlayerComponent.class);
        playerComponent.decrementLife();
        if (bullet.hasComponent(PlayerComponent.class)) {
            bullet.getComponent(PlayerComponent.class).incrementScore();
        }
        if (playerComponent.getLife() == 0) {
            spawn(entityNames.EXPLOSION_PLAYER_DEATH, player.getPosition());
            player.removeFromWorld();
            play(assetNames.sounds.EXPLOSION_PLAYER_DEATH);
            runOnce(() -> set(GameVariableNames.isGameOver, true), Duration.seconds(2));
        } else {
            spawn(entityNames.EXPLOSION_PLAYER_BULLET, bullet.getPosition());
            play(assetNames.sounds.EXPLOSION_PLAYER_LIFE);
        }
    }
}
