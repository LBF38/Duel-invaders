
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

        inc(GameVariableNames.PLAYER1_LIVES, -1);
        if (geti(GameVariableNames.PLAYER1_LIVES) == 0)
            set(GameVariableNames.isGameOver, true);
            play("Explosion/finalExplosion.wav");//son explosion du vaiseau

        play("Explosion/strongExplosion.wav"); //son de l'explosion fort lorsque le joueur est touché
    }
}
