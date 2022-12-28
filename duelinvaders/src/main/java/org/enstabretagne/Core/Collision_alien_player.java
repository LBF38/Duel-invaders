package org.enstabretagne.Core;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;

import org.enstabretagne.Component.EntityType;

import static com.almasb.fxgl.dsl.FXGL.*;

public class Collision_alien_player extends CollisionHandler {
    public Collision_alien_player(EntityType player, EntityType alien) {
        super(EntityType.PLAYER, EntityType.ALIEN);
    }

    @Override
    protected void onCollisionBegin(Entity player, Entity alien) {
        player.removeFromWorld();
        set("isGameOver", true);
    }
}
