
package org.enstabretagne.Core;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import org.enstabretagne.Component.EntityType;

import static com.almasb.fxgl.dsl.FXGL.*;

public class EnemyShootPlayerCollision extends CollisionHandler {

    public EnemyShootPlayerCollision(EntityType enemy_shoot, EntityType player) {
        super(EntityType.ENEMY_SHOOT, EntityType.PLAYER);
    }

    @Override
    protected void onCollisionBegin(Entity enemy_shoot, Entity player) {
        enemy_shoot.removeFromWorld();
        player.removeFromWorld();
        inc(GameVariableNames.PLAYER1_LIVES, -1);
    }
}
