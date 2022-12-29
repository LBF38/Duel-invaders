package org.enstabretagne.Core;

import static com.almasb.fxgl.dsl.FXGL.getGameWorld;
import static com.almasb.fxgl.dsl.FXGL.inc;
import static com.almasb.fxgl.dsl.FXGL.play;
import static com.almasb.fxgl.dsl.FXGL.set;
import static com.almasb.fxgl.dsl.FXGL.spawn;

import org.enstabretagne.Component.EntityType;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;

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
        play("Explosion/mediumExplosion.wav");
        set(GameVariableNames.isGameWon, getGameWorld().getEntitiesByType(EntityType.ALIEN).isEmpty());

    }
}
