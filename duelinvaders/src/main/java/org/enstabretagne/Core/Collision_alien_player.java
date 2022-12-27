package org.enstabretagne.Core;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import org.enstabretagne.Component.EntityType;
import org.enstabretagne.Component.Owner;

import static com.almasb.fxgl.dsl.FXGL.*;


public class Collision_alien_player extends CollisionHandler {
    //private Entity player;
    //private Entity alien;

    public Collision_alien_player(EntityType player, EntityType alien) {
        super(EntityType.PLAYER, EntityType.ALIEN);
    }

/*
    @Override
    protected void onCollisionBegin(Entity player, Entity alien) {
        Object owner = player.getComponent(Owner.class).getValue();
        if (owner == EntityType.ALIEN){
            return;
        }

        System.out.println("Collision");
    }

    */
    @Override
    protected void onCollisionBegin(Entity player, Entity alien) {
        System.out.println("Collision player alien");
    }
 /*   protected void initPhysics(){
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.ALIEN) {
            @Override
            protected void onCollisionBegin(Entity player, Entity alien) {
                //player.removeFromWorld();
                //alien.removeFromWorld();
                System.out.println("Collision");
            }
        });
    }*/
}
