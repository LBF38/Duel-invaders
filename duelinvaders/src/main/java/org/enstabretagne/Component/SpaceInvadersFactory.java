package org.enstabretagne.Component;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static com.almasb.fxgl.dsl.FXGLForKtKt.spawn;
import static com.almasb.fxgl.dsl.FXGLForKtKt.texture;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.OffscreenCleanComponent;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import java.util.concurrent.TimeUnit;
import com.almasb.fxgl.dsl.FXGL;

import com.almasb.fxgl.particle.ParticleComponent;
import javafx.animation.TranslateTransition;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
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
        spawn("shooting_start", data.getX(), data.getY());
        return entityBuilder()
                .type(EntityType.BULLET)
                .at(data.getX(), data.getY())
                .viewWithBBox(new Rectangle(5, 20, Color.BLACK))
                .with(new BulletComponent())
                .with(new OffscreenCleanComponent())
                .collidable()
                .build();
    }

    @Spawns("background")
    public Entity newBackground(SpawnData data) {
        return entityBuilder()
                .at(-10, -10)
                // bigger than game size to account for camera shake
                .view(texture("background_1.png", Constant.BOARD_WIDTH + 20, Constant.BOARD_HEIGHT + 20))
                .zIndex(-500) //todo a tester a quoi ca sert
                .build();
    }

    @Spawns("shooting_start")
    public Entity shooting_start(SpawnData data) {
        //play("shooting_start.wav");
        var bullet_width = 20;
        var bullet_height = 40;

        var texture = texture("fire.png", bullet_width, bullet_height);
        //tourne la texture de 180°
        texture.setRotate(180);

        var e = entityBuilder()
                .at(data.getX()-bullet_width/2, data.getY())
                .view(texture)
                .build();

        FXGL.runOnce(() -> e.removeFromWorld(), Duration.seconds(0.2));
        FXGL.runOnce(() -> spawn("shooting_smoke",data.getX(),data.getY()), Duration.seconds(0.2));
        return e;
    }

    @Spawns("shooting_smoke")
    public Entity shooting_smoke(SpawnData data) {
        var smoke_width = 40;
        var smoke_height = 40;
        var texture = texture("smoke.png", smoke_width, smoke_height);
        var e = entityBuilder()
                .at(data.getX()-smoke_width/2, data.getY()-30)
                .view(texture)
                .build();

        FXGL.runOnce(() -> e.removeFromWorld(), Duration.seconds(0.2));
        return e;
    }




}
