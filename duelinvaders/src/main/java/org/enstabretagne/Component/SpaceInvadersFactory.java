package org.enstabretagne.Component;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static com.almasb.fxgl.dsl.FXGL.*;

public class SpaceInvadersFactory implements EntityFactory {
    @Spawns("player")
    public Entity newPlayer(SpawnData data) {
        return entityBuilder()
                .type(EntityType.PLAYER)
                .at(data.getX(), data.getY())
                .viewWithBBox(new Rectangle(40, 40, Color.BLUE))
                .with(new PlayerComponent())
                .with(new CollidableComponent(true))
                //.collidable()
                .build();
    }

    @Spawns("alien")
    public Entity newAlien(SpawnData data) {
        return entityBuilder()
                .type(EntityType.ALIEN)
                .at(data.getX(), data.getY())
                .viewWithBBox(new Rectangle(40, 40, Color.RED))
                .with(new AlienComponent())
                .with(new CollidableComponent(true))
                //.collidable()
                .build();
    }
}
