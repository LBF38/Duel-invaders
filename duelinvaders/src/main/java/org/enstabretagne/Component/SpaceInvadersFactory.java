package org.enstabretagne.Component;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.enstabretagne.Core.Constant;

import static com.almasb.fxgl.dsl.FXGL.*;

public class SpaceInvadersFactory implements EntityFactory {
    @Spawns("player")
    public Entity newPlayer(SpawnData data) {
        return entityBuilder()
                .type(EntityType.PLAYER)
                .at(data.getX(), data.getY())
                .viewWithBBox(new Rectangle(40, 40, Color.BLUE))
                .with(new PlayerComponent())
                .collidable()
                .build();
    }

    @Spawns("alien")
    public Entity newAlien(SpawnData data) {
        return entityBuilder()
                .type(EntityType.ALIEN)
                .at(data.getX(), data.getY())
                .viewWithBBox(new Rectangle(40, 40, Color.RED))
                .with(new AlienComponent())
                .collidable()
                .build();
    }

    @Spawns("shot")
    public Entity newShot(SpawnData data) {
        return entityBuilder()
                .type(EntityType.SHOT)
                .at(data.getX(), data.getY())
                .viewWithBBox(new Rectangle(5, 15, Color.BLACK))
                .with(new ShotComponent(data.getX(), data.getY(), Constant.Direction.UP))
                .collidable()
                .build();
    }
}
