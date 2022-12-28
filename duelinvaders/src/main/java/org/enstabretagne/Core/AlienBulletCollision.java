package org.enstabretagne.Core;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import org.enstabretagne.Component.EntityType;

import static com.almasb.fxgl.dsl.FXGL.*;

public class AlienBulletCollision extends CollisionHandler {

    public AlienBulletCollision(EntityType bullet, EntityType alien) {
        super(EntityType.BULLET, EntityType.ALIEN);
    }

    @Override
    protected void onCollisionBegin(Entity bullet, Entity alien) {
        inc(GameVariableNames.PLAYER1_SCORE, +1);
        spawn("explosion_alien", alien.getPosition());
        bullet.removeFromWorld();
        alien.removeFromWorld();
        play("Explosion/mediumExplosion.wav"); //son de l'explosion moyen lorsque l'alien est touché
        set(GameVariableNames.isGameWon, getGameWorld().getEntitiesByType(EntityType.ALIEN).isEmpty());

    }
}
