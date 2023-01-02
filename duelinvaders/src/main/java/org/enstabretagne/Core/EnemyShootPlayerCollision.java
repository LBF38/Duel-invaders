
package org.enstabretagne.Core;

import static com.almasb.fxgl.dsl.FXGL.geti;
import static com.almasb.fxgl.dsl.FXGL.inc;
import static com.almasb.fxgl.dsl.FXGL.play;
import static com.almasb.fxgl.dsl.FXGL.set;
import static com.almasb.fxgl.dsl.FXGL.spawn;

import org.enstabretagne.Component.EntityType;
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
     * 
     * @param enemy_shoot
     * @param player
     */
    public EnemyShootPlayerCollision(EntityType enemy_shoot, EntityType player) {
        super(EntityType.ENEMY_SHOOT, EntityType.PLAYER);
    }

    /**
     * Gestion des collisions entre les tirs des aliens et le joueur
     */
    @Override
    protected void onCollisionBegin(Entity enemy_shoot, Entity player) {
        enemy_shoot.removeFromWorld();

        inc(GameVariableNames.PLAYER1_LIVES, -1);
        if (geti(GameVariableNames.PLAYER1_LIVES) == 0) {
            spawn(entityNames.EXPLOSION_PLAYER_DEATH, player.getPosition());
            play("Explosion/finalExplosion.wav");
            set(GameVariableNames.isGameOver, true);
            player.removeFromWorld();
        } else {
            spawn(entityNames.EXPLOSION_PLAYER_BULLET, enemy_shoot.getPosition());
            play("Explosion/strongExplosion.wav");
        }
    }
}
