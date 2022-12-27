package org.enstabretagne.Core;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import org.enstabretagne.Component.EntityType;

public class Collision_bullet_alien extends CollisionHandler {

    public Collision_bullet_alien(EntityType bullet, EntityType alien) {
        super(EntityType.BULLET, EntityType.ALIEN);
    }

    @Override
    protected void onCollisionBegin(Entity bullet, Entity alien) {
        System.out.println("Collision bullet alien");
        bullet.removeFromWorld();
        alien.removeFromWorld();
    }
}
