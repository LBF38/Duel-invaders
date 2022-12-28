package org.enstabretagne.Component;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

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

    @Spawns("bullet")
    public Entity newBullet(SpawnData data) {
        return entityBuilder()
                .type(EntityType.BULLET)
                .at(data.getX(), data.getY())
                .viewWithBBox(new Rectangle(5, 20, Color.BLACK))
                .with(new BulletComponent())
                .collidable()
                .build();
    }

    @Spawns("alienBullet")
    public Entity newAlienBullet(SpawnData data) {
        return entityBuilder()
                .type(EntityType.ENEMY_SHOOT)
                .at(data.getX(), data.getY())
                .viewWithBBox(new Rectangle(5, 20, Color.BLACK))
                .with(new BulletComponent())
                .collidable()
                .build();
    }
}
