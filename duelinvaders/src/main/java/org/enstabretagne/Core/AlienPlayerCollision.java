package org.enstabretagne.Core;

import static com.almasb.fxgl.dsl.FXGL.play;
import static com.almasb.fxgl.dsl.FXGL.set;
import static com.almasb.fxgl.dsl.FXGL.spawn;

import org.enstabretagne.Component.EntityType;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;

public class AlienPlayerCollision extends CollisionHandler {
    public AlienPlayerCollision(EntityType player, EntityType alien) {
        super(EntityType.PLAYER, EntityType.ALIEN);
    }

    @Override
    protected void onCollisionBegin(Entity player, Entity alien) {
        spawn("explosion_player_death", player.getPosition());
        player.removeFromWorld();
        set(GameVariableNames.isGameOver, true);
        play("Explosion/finalExplosion.wav");
    }
}
