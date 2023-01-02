package org.enstabretagne.Core;

import static com.almasb.fxgl.dsl.FXGL.getGameWorld;
import static com.almasb.fxgl.dsl.FXGL.inc;
import static com.almasb.fxgl.dsl.FXGL.play;
import static com.almasb.fxgl.dsl.FXGL.set;
import static com.almasb.fxgl.dsl.FXGL.spawn;

import org.enstabretagne.Component.EntityType;
import org.enstabretagne.Utils.entityNames;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;

/**
 * Gestion des collisions entre les balles et les aliens
 * En cas de collision, le joueur gagne un point et l'alien est détruit
 * 
 * @author @jufch, @LBF38, @MathieuDFS
 * @since 0.1.0
 */
public class AlienBulletCollision extends CollisionHandler {
    /**
     * Constructeur de la classe AlienBulletCollision
     * Elle hérite de la classe CollisionHandler de FXGL
     * 
     * @param bullet
     * @param alien
     */
    public AlienBulletCollision(EntityType bullet, EntityType alien) {
        super(EntityType.BULLET, EntityType.ALIEN);
    }

    /**
     * Gestion des collisions entre les balles et les aliens
     * Cette méthode est appelée à chaque fois qu'une collision est détectée entre
     * les entités bullet et alien
     * Cela signifie que le joueur a touché un alien
     * 
     * @param bullet
     * @param alien
     */
    @Override
    protected void onCollisionBegin(Entity bullet, Entity alien) {
        inc(GameVariableNames.PLAYERS_SCORE, +1);
        spawn(entityNames.EXPLOSION_ALIEN, alien.getPosition());
        bullet.removeFromWorld();
        alien.removeFromWorld();
        play("Explosion/mediumExplosion.wav");
        set(GameVariableNames.isGameWon, getGameWorld().getEntitiesByType(EntityType.ALIEN).isEmpty());
    }
}
