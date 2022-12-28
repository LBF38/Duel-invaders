package org.enstabretagne.Component;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static com.almasb.fxgl.dsl.FXGLForKtKt.texture;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.enstabretagne.Core.Constant;

public class SpaceInvadersFactory implements EntityFactory {
    @Spawns("player")
    public Entity newPlayer(SpawnData data) {
        var texture = FXGL.texture("Space_ship_5.png", 100, 100);
        return entityBuilder()
                .type(EntityType.PLAYER)
                .at(data.getX(), data.getY())
                .viewWithBBox(texture)
                .with(new PlayerComponent())
                .collidable()
                .build();
    }

    @Spawns("alien")
    public Entity newAlien(SpawnData data) {
        //Extrait une couleur aléatoire dans la liste des couleurs
        int randomIndex = Constant.random.nextInt(Constant.AlienColor.values().length);
        Constant.AlienColor randomColor = Constant.AlienColor.values()[randomIndex];
        //la convertit en couleur JavaFX
        Color color = Color.valueOf(randomColor.name());

        var texture = FXGL.texture("alien2.png", 60, 60).multiplyColor(color);
        return entityBuilder()
                .type(EntityType.ALIEN)
                .at(data.getX(), data.getY())
                //.viewWithBBox(new Rectangle(40, 40, Color.RED))
                .viewWithBBox(texture)
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

    @Spawns("background")
    public Entity newBackground(SpawnData data) {
        return entityBuilder()
                .at(-10, -10)
                // bigger than game size to account for camera shake
                .view(texture("background_1.png", Constant.BOARD_WIDTH + 20, Constant.BOARD_HEIGHT + 20))
                .zIndex(-500)
                .build();
    }
}
