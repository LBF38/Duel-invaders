
package org.enstabretagne.Core;

import static com.almasb.fxgl.dsl.FXGL.play;
import static com.almasb.fxgl.dsl.FXGL.set;
import static com.almasb.fxgl.dsl.FXGL.spawn;

import org.enstabretagne.Component.PlayerComponent;
import org.enstabretagne.Utils.EntityType;
import org.enstabretagne.Utils.GameVariableNames;
import org.enstabretagne.Utils.assetNames;
import org.enstabretagne.Utils.entityNames;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;

/**
 * Gestion des collisions entre les tirs des aliens et le joueur
 * En cas de collision, le joueur perd un point de vie
 * 
 * @author jufch, LBF38, MathieuDFS
 * @since 0.1.0
 */
public class EnemyShootPlayerCollision extends CollisionHandler {
    /**
     * Constructeur de la classe EnemyShootPlayerCollision
     */
    public EnemyShootPlayerCollision() {
        super(EntityType.ENEMY_SHOOT, EntityType.PLAYER);
    }

    /**
     * Gestion des collisions entre les tirs des aliens et le joueur
     */
    @Override
    protected void onCollisionBegin(Entity enemy_shoot, Entity player) {
        enemy_shoot.removeFromWorld();
        PlayerComponent playerComponent = player.getComponent(PlayerComponent.class);
        playerComponent.decrementLife();
        if (playerComponent.getLife() == 0) {
            spawn(entityNames.EXPLOSION_PLAYER_DEATH, player.getPosition());
            play(assetNames.sounds.EXPLOSION_PLAYER_DEATH);
            set(GameVariableNames.isGameOver, true);
            player.removeFromWorld();
        } else {
            spawn(entityNames.EXPLOSION_PLAYER_BULLET, enemy_shoot.getPosition());
            play(assetNames.sounds.EXPLOSION_PLAYER_LIFE);
        }
    }
}
