

package org.enstabretagne.Core;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import org.enstabretagne.Component.EntityType;

public class Collision_EnemyShoot_player extends CollisionHandler {

    public Collision_EnemyShoot_player(EntityType enemy_shoot, EntityType player) {
        super(EntityType.ENEMY_SHOOT, EntityType.PLAYER);
    }
    @Override
    protected void onCollisionBegin(Entity enemy_shoot, Entity player) {
        System.out.println("Collision enemy_shoot player");
    }
}
