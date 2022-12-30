package org.enstabretagne.Component;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static com.almasb.fxgl.dsl.FXGL.play;
import static com.almasb.fxgl.dsl.FXGL.runOnce;
import static com.almasb.fxgl.dsl.FXGL.spawn;
import static com.almasb.fxgl.dsl.FXGL.texture;

import org.enstabretagne.Core.Constant;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.components.ExpireCleanComponent;
import com.almasb.fxgl.dsl.components.OffscreenCleanComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.texture.Texture;

import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * Définition des entités du jeu. Chaque entité est définie par une méthode avec
 * l'annotation {@code}@Spawns{@code}.
 * 
 * @author @LBF38, @jufch, @MathieuDFS
 * @since 0.1.0
 */
public class SpaceInvadersFactory implements EntityFactory {

    /**
     * Définition de l'entité joueur, nommé player
     * 
     * @param data
     * @return Entity
     */
    @Spawns("player")
    public Entity newPlayer(SpawnData data) {
        Texture texture = texture("SpaceShip.png", 100, 100);
        return entityBuilder()
                .type(EntityType.PLAYER)
                .at(data.getX(), data.getY())
                .viewWithBBox(texture)
                .with(new PlayerComponent())
                .collidable()
                .build();
    }

    /**
     * Définition de l'entité alien, nommé alien
     * 
     * @param data
     * @return Entity
     */
    @Spawns("alien")
    public Entity newAlien(SpawnData data) {
        int randomIndex = Constant.random.nextInt(Constant.AlienColor.values().length);
        Constant.AlienColor randomColor = Constant.AlienColor.values()[randomIndex];
        Color color = Color.valueOf(randomColor.name());

        Texture texture = texture("alien.png", 60, 60).multiplyColor(color);
        return entityBuilder()
                .type(EntityType.ALIEN)
                .at(data.getX(), data.getY())
                .viewWithBBox(texture)
                .with(new AlienComponent())
                .collidable()
                .build();
    }

    /**
     * Définition de l'entité tir du joueur, nommé bullet
     * Elle est uniquement utilisée par le joueur
     * 
     * @param data
     * @return Entity
     */
    @Spawns("bullet")
    public Entity newBullet(SpawnData data) {
        int bulletWidth = 20;
        int bulletHeight = 20;
        Texture texture = texture("rocket.png", bulletWidth, bulletHeight);
        spawn("shooting_start", data.getX(), data.getY());
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

    /**
     * Définition de l'entité tir de l'alien, nommé alienBullet
     * Elle est uniquement utilisée par les aliens
     * 
     * @param data
     * @return Entity
     */
    @Spawns("alienBullet")
    public Entity newAlienBullet(SpawnData data) {
        runOnce(() -> spawn("eclat", data.getX(), data.getY()), Duration.seconds(0.5));

        int laserWidth = 20;
        int laserHeight = 20;
        Texture texture = texture("laser.png", laserWidth, laserHeight);
        play("Tir/laser" + (int) (Math.random() * 4 + 1) + ".wav");

        return entityBuilder()
                .type(EntityType.ENEMY_SHOOT)
                .at(data.getX() - laserWidth / 2, data.getY())
                .viewWithBBox(texture)
                .with(new BulletComponent())
                .collidable()
                .build();
    }

    /**
     * Définition de l'entité eclat, nommé eclat
     * Décoration pour agrémenter le jeu lors d'un tir
     * 
     * @param data
     * @return Entity
     */
    @Spawns("eclat")
    public Entity newEclat(SpawnData data) {
        int eclatWidth = 150;
        int eclatHeight = 150;
        Texture texture = texture("eclat2.png", eclatWidth, eclatHeight);
        texture.setRotate(180);
        return entityBuilder()
                .at(data.getX() - eclatWidth / 2, data.getY() - eclatHeight / 2)
                .viewWithBBox(texture)
                .with(new ExpireCleanComponent(Duration.seconds(0.05)))
                .build();
    }

    /**
     * Définition de l'entité pour l'arrière-plan, nommé background
     * Elle est utilisée pour l'arrière-plan du jeu
     * 
     * @param data
     * @return Entity
     */
    @Spawns("background")
    public Entity newBackground(SpawnData data) {
        return entityBuilder()
                .at(-10, -10)
                .view(texture("Background.png", Constant.BOARD_WIDTH + 20, Constant.BOARD_HEIGHT + 20))
                .zIndex(-500)
                .build();
    }

    /**
     * Définition de l'entité pour le tir du joueur, nommé shooting_start
     * A but décoratif pour agrémenter le jeu
     * 
     * @param data
     * @return Entity
     */
    @Spawns("shooting_start")
    public Entity shooting_start(SpawnData data) {
        int bullet_width = 20;
        int bullet_height = 40;

        Texture texture = texture("Fire.png", bullet_width, bullet_height);
        texture.setRotate(180);

        return entityBuilder()
                .at(data.getX() - bullet_width / 2, data.getY())
                .view(texture)
                .with(new ExpireCleanComponent(Duration.seconds(0.2)))
                .with("shooting_smoke", spawn("shooting_smoke", data.getX(), data.getY()))
                .build();
    }

    /**
     * Définition de l'entité pour le tir du joueur, nommé shooting_smoke
     * A but décoratif pour agrémenter le jeu
     * 
     * @param data
     * @return Entity
     */
    @Spawns("shooting_smoke")
    public Entity shooting_smoke(SpawnData data) {
        int smoke_width = 40;
        int smoke_height = 40;
        Texture texture = texture("Smoke.png", smoke_width, smoke_height);
        return entityBuilder()
                .at(data.getX() - smoke_width / 2, data.getY() - 30)
                .view(texture)
                .with(new ExpireCleanComponent(Duration.seconds(0.2)))
                .build();
    }

    /**
     * Définition de l'entité pour l'explosion, nommé explosion_alien
     * A but décoratif pour agrémenter le jeu
     * 
     * @param data
     * @return Entity
     */
    @Spawns("explosion_alien")
    public Entity explosion_alien(SpawnData data) {
        int explosion_width = 60;
        int explosion_height = 60;

        Texture texture = texture("explosion" + FXGLMath.random(1, 7) + ".png", explosion_width, explosion_height);
        return entityBuilder()
                .at(data.getX(), data.getY())
                .view(texture)
                .with(new ExpireCleanComponent(Duration.seconds(0.3)))
                .build();
    }

    /**
     * Définition de l'entité pour l'explosion, nommé explosion_player_bullet
     * A but décoratif pour agrémenter le jeu
     * 
     * @param data
     * @return Entity
     */
    @Spawns("explosion_player_bullet")
    public Entity explosion_player_bullet(SpawnData data) {
        int explosion_width = 70;
        int explosion_height = 60;
        Texture texture = texture("explosion_player.png", explosion_width, explosion_height);
        return entityBuilder()
                .at(data.getX() - explosion_width / 2, data.getY() - explosion_height / 2)
                .view(texture)
                .with(new ExpireCleanComponent(Duration.seconds(0.3)))
                .build();
    }

    /**
     * Définition de l'entité pour l'explosion, nommé explosion_player_death
     * A but décoratif pour agrémenter le jeu
     * 
     * @param data
     * @return Entity
     */
    @Spawns("explosion_player_death")
    public Entity explosion_player_death(SpawnData data) {
        int explosion_width = 200;
        int explosion_height = 200;
        Texture texture = texture("finalExplosion.png", explosion_width, explosion_height);
        for (int i = 0; i < 10; i++) {
            double x = data.getX() + FXGLMath.random(-100, 100);
            double y = data.getY() + FXGLMath.random(-100, 100);
            spawn("explosion_alien", x, y);
        }
        return entityBuilder()
                .at(data.getX(), data.getY())
                .view(texture)
                .zIndex(-100)
                .build();
    }
}
