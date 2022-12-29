package org.enstabretagne.Component;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static com.almasb.fxgl.dsl.FXGL.play;
import static com.almasb.fxgl.dsl.FXGL.spawn;

import org.enstabretagne.Core.Constant;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.OffscreenCleanComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;

import javafx.scene.paint.Color;
import javafx.util.Duration;

public class SpaceInvadersFactory implements EntityFactory {
    @Spawns("player")
    public Entity newPlayer(SpawnData data) {
        var texture = FXGL.texture("SpaceShip.png", 100, 100);
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
        // Extrait une couleur aléatoire dans la liste des couleurs
        int randomIndex = Constant.random.nextInt(Constant.AlienColor.values().length);
        Constant.AlienColor randomColor = Constant.AlienColor.values()[randomIndex];
        // la convertit en couleur JavaFX
        Color color = Color.valueOf(randomColor.name());

        var texture = FXGL.texture("alien.png", 60, 60).multiplyColor(color);
        return entityBuilder()
                .type(EntityType.ALIEN)
                .at(data.getX(), data.getY())
                .viewWithBBox(texture)
                .with(new AlienComponent())
                .collidable()
                .build();
    }

    @Spawns("bullet")
    public Entity newBullet(SpawnData data) {
        var bulletWidth = 20;
        var bulletHeight = 20;
        var texture = FXGL.texture("rocket.png", bulletWidth, bulletHeight);
        FXGL.spawn("shooting_start", data.getX(), data.getY());
        play("Tir/canon.wav");

        return entityBuilder()
                .type(EntityType.BULLET)
                .at(data.getX() - bulletWidth / 2, data.getY())
                .viewWithBBox(texture)
                .with(new BulletComponent())
                .with(new OffscreenCleanComponent())
                .collidable()
                .build();
    }

    @Spawns("alienBullet")
    public Entity newAlienBullet(SpawnData data) {
        FXGL.runOnce(() -> spawn("eclat", data.getX(), data.getY()), Duration.seconds(0.5));

        var laserWidth = 20;
        var laserHeight = 20;
        var texture = FXGL.texture("laser.png", laserWidth, laserHeight);
        play("Tir/laser" + (int) (Math.random() * 4 + 1) + ".wav");

        return entityBuilder()
                .type(EntityType.ENEMY_SHOOT)
                .at(data.getX() - laserWidth / 2, data.getY())
                .viewWithBBox(texture)
                .with(new BulletComponent())
                .collidable()
                .build();
    }

    @Spawns("eclat")
    public Entity newEclat(SpawnData data) {
        var eclatWidth = 150;
        var eclatHeight = 150;
        var texture = FXGL.texture("eclat2.png", eclatWidth, eclatHeight);
        texture.setRotate(180);
        var e = entityBuilder()
                .at(data.getX() - eclatWidth / 2, data.getY() - eclatHeight / 2)
                .viewWithBBox(texture)
                .build();
        FXGL.runOnce(() -> e.removeFromWorld(), Duration.seconds(0.05));
        return e;
    }

    @Spawns("background")
    public Entity newBackground(SpawnData data) {
        return entityBuilder()
                .at(-10, -10)
                // bigger than game size to account for camera shake
                .view(FXGL.texture("Background.png", Constant.BOARD_WIDTH + 20, Constant.BOARD_HEIGHT + 20))
                .zIndex(-500) // todo a tester a quoi ca sert
                .build();
    }

    @Spawns("shooting_start")
    public Entity shooting_start(SpawnData data) {
        var bullet_width = 20;
        var bullet_height = 40;

        var texture = FXGL.texture("Fire.png", bullet_width, bullet_height);
        // tourne la texture de 180°
        texture.setRotate(180);

        var e = entityBuilder()
                .at(data.getX() - bullet_width / 2, data.getY())
                .view(texture)
                .build();

        FXGL.runOnce(() -> e.removeFromWorld(), Duration.seconds(0.2));
        FXGL.runOnce(() -> FXGL.spawn("shooting_smoke", data.getX(), data.getY()), Duration.seconds(0.2));
        return e;
    }

    @Spawns("shooting_smoke")
    public Entity shooting_smoke(SpawnData data) {
        var smoke_width = 40;
        var smoke_height = 40;
        var texture = FXGL.texture("Smoke.png", smoke_width, smoke_height);
        var e = entityBuilder()
                .at(data.getX() - smoke_width / 2, data.getY() - 30)
                .view(texture)
                .build();

        FXGL.runOnce(() -> e.removeFromWorld(), Duration.seconds(0.2));
        return e;
    }

    @Spawns("explosion_alien")
    public Entity explosion_alien(SpawnData data) {
        var explosion_width = 60;
        var explosion_height = 60;

        var texture = FXGL.texture("explosion" + FXGLMath.random(1, 7) + ".png", explosion_width, explosion_height);
        var e = entityBuilder()
                .at(data.getX(), data.getY())
                .view(texture)
                .build();

        FXGL.runOnce(() -> e.removeFromWorld(), Duration.seconds(0.3));
        return e;
    }

    @Spawns("explosion_player_bullet")
    public Entity explosion_player_bullet(SpawnData data) {
        var explosion_width = 70;
        var explosion_height = 60;
        var texture = FXGL.texture("explosion_player.png", explosion_width, explosion_height);
        var e = entityBuilder()
                .at(data.getX() - explosion_width / 2, data.getY() - explosion_height / 2)
                .view(texture)
                .build();

        FXGL.runOnce(() -> e.removeFromWorld(), Duration.seconds(0.3));
        return e;
    }

    @Spawns("explosion_player_death")
    public Entity explosion_player_death(SpawnData data) {
        var explosion_width = 200;
        var explosion_height = 200;
        var texture = FXGL.texture("finalExplosion.png", explosion_width, explosion_height);
        var e = entityBuilder()
                .at(data.getX(), data.getY())
                .view(texture)
                .build();
        for (int i = 0; i < 10; i++) {
            FXGL.spawn("explosion_alien", data.getX() + FXGLMath.random(-100, 100),
                    data.getY() + FXGLMath.random(-100, 100));
        }
        return e;
    }
}
