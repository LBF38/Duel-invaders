package org.enstabretagne.Component;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static com.almasb.fxgl.dsl.FXGL.play;
import static com.almasb.fxgl.dsl.FXGL.spawn;
import static com.almasb.fxgl.dsl.FXGL.texture;

import org.enstabretagne.Core.Constant;
import org.enstabretagne.Utils.assetNames;
import org.enstabretagne.Utils.entityNames;

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
 * @author LBF38, jufch, MathieuDFS
 * @since 0.1.0
 */
public class SpaceInvadersFactory implements EntityFactory {
    /**
     * Définition de l'entité joueur, nommé player
     * 
     * @param data
     * @return Entity
     */
    @Spawns(entityNames.PLAYER)
    public Entity newPlayer(SpawnData data) {
        Texture texture = texture(assetNames.textures.SPACESHIP, Constant.PLAYER_WIDTH, Constant.PLAYER_HEIGHT);

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
    @Spawns(entityNames.ALIEN)
    public Entity newAlien(SpawnData data) {
        int randomIndex = Constant.random.nextInt(Constant.AlienColor.values().length);
        Constant.AlienColor randomColor = Constant.AlienColor.values()[randomIndex];
        Color color = Color.valueOf(randomColor.name());

        Texture texture = texture(assetNames.textures.ALIEN, Constant.ALIEN_WIDTH, Constant.ALIEN_HEIGHT)
                .multiplyColor(color);
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
    @Spawns(entityNames.BULLET)
    public Entity newBullet(SpawnData data) {
        int bulletWidth = 20;
        int bulletHeight = 20;
        Texture texture = texture(assetNames.textures.ROCKET, bulletWidth, bulletHeight);
        texture.setRotate(90);
        play(assetNames.sounds.CANNON_SHOT);

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
    @Spawns(entityNames.BULLET_ALIEN)
    public Entity newAlienBullet(SpawnData data) {
        int laserWidth = 20;
        int laserHeight = 20;
        Texture texture = texture(assetNames.textures.LASER, laserWidth, laserHeight);
        String randomLaserSound = assetNames.sounds.LASER_SOUNDS
                .get(FXGLMath.random(0, Constant.NUMBER_OF_LASER_SOUNDS - 1));
        play(randomLaserSound);

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
     * Décoration pour agrémenter le jeu lors d'un tir alien
     * 
     * @param data
     * @return Entity
     */
    @Spawns(entityNames.ECLAT)
    public Entity newEclat(SpawnData data) {
        int eclatWidth = 150;
        int eclatHeight = 150;
        Texture texture = texture(assetNames.textures.ECLAT2, eclatWidth, eclatHeight);
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
    @Spawns(entityNames.BACKGROUND)
    public Entity newBackground(SpawnData data) {
        return entityBuilder()
                .at(-10, -10)
                .view(texture(assetNames.textures.GAME_BACKGROUND, Constant.GAME_WIDTH + 20, Constant.GAME_HEIGHT + 20))
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
    @Spawns(entityNames.SHOOTING_START)
    public Entity shooting_start(SpawnData data) {
        Texture texture = texture(assetNames.textures.FIRE, Constant.SHOOTING_START_WIDTH,
                Constant.SHOOTING_START_HEIGHT);
        texture.setRotate(180);

        return entityBuilder()
                .at(data.getX(), data.getY())
                .view(texture)
                .with(new ShootingStartComponent())
                .build();
    }

    /**
     * Définition de l'entité pour le tir du joueur, nommé shooting_smoke
     * A but décoratif pour agrémenter le jeu
     * 
     * @param data
     * @return Entity
     */
    @Spawns(entityNames.SHOOTING_SMOKE)
    public Entity shooting_smoke(SpawnData data) {
        Texture texture = texture(assetNames.textures.SMOKE, Constant.SHOOTING_SMOKE_WIDTH,
                Constant.SHOOTING_SMOKE_HEIGHT);
        return entityBuilder()
                .at(data.getX(), data.getY())
                .view(texture)
                .with(new ShootingSmokeComponent())
                .build();
    }

    /**
     * Définition de l'entité pour l'explosion, nommé explosion_alien
     * A but décoratif pour agrémenter le jeu
     * 
     * @param data
     * @return Entity
     */
    @Spawns(entityNames.EXPLOSION_ALIEN)
    public Entity explosion_alien(SpawnData data) {
        int explosion_width = 60;
        int explosion_height = 60;
        String randomTexture = assetNames.textures.EXPLOSIONS
                .get(FXGLMath.random(0, Constant.NUMBER_OF_EXPLOSIONS - 1));
        Texture texture = texture(randomTexture, explosion_width, explosion_height);
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
    @Spawns(entityNames.EXPLOSION_PLAYER_BULLET)
    public Entity explosion_player_bullet(SpawnData data) {
        int explosion_width = 70;
        int explosion_height = 60;
        Texture texture = texture(assetNames.textures.EXPLOSION_PLAYER, explosion_width, explosion_height);
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
    @Spawns(entityNames.EXPLOSION_PLAYER_DEATH)
    public Entity explosion_player_death(SpawnData data) {
        int explosion_width = 200;
        int explosion_height = 200;
        Texture texture = texture(assetNames.textures.EXPLOSION_FINAL, explosion_width, explosion_height);

        for (int i = 0; i < 20; i++) {
            double x = data.getX() + FXGLMath.random(-100, 100);
            double y = data.getY() + FXGLMath.random(-100, 100);
            spawn(entityNames.EXPLOSION_ALIEN, x, y);
        }

        return entityBuilder()
                .at(data.getX(), data.getY())
                .view(texture)
                .zIndex(-100)
                .build();
    }

    /**
     * Définition de l'entité affichage des vies restantes
     *
     * @param data
     * @return Entity
     */
    @Spawns(entityNames.LIFE)
    public Entity life(SpawnData data) {
        int x = (int) data.getX();
        Texture texture = texture(assetNames.textures.LIFES.get(x - 1), Constant.LIFE_DISPLAY_WIDTH,
                Constant.LIFE_DISPLAY_HEIGHT);
        return entityBuilder()
                .at(data.getX(), data.getY())
                .view(texture)
                .with(new LifeComponent())
                .build();
    }
}
