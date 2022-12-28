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
        inc("Player1_score", +1);
        bullet.removeFromWorld();
        alien.removeFromWorld();
    }
}
