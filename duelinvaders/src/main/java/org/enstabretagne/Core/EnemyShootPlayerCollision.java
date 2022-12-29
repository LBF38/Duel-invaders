
package org.enstabretagne.Core;

import static com.almasb.fxgl.dsl.FXGL.geti;
import static com.almasb.fxgl.dsl.FXGL.inc;
import static com.almasb.fxgl.dsl.FXGL.play;
import static com.almasb.fxgl.dsl.FXGL.set;
import static com.almasb.fxgl.dsl.FXGL.spawn;

import org.enstabretagne.Component.EntityType;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;

public class EnemyShootPlayerCollision extends CollisionHandler {

    public EnemyShootPlayerCollision(EntityType enemy_shoot, EntityType player) {
        super(EntityType.ENEMY_SHOOT, EntityType.PLAYER);
    }

    @Override
    protected void onCollisionBegin(Entity enemy_shoot, Entity player) {
        enemy_shoot.removeFromWorld();

        inc(GameVariableNames.PLAYER1_LIVES, -1);
        if (geti(GameVariableNames.PLAYER1_LIVES) == 0) {
            spawn("explosion_player_death", player.getPosition());
            play("Explosion/finalExplosion.wav");
            set(GameVariableNames.isGameOver, true);
            player.removeFromWorld();
        } else {
            spawn("explosion_player_bullet", enemy_shoot.getPosition());
            play("Explosion/strongExplosion.wav");
        }
    }
}
