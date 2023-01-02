package org.enstabretagne.Core;

import static com.almasb.fxgl.dsl.FXGL.play;
import static com.almasb.fxgl.dsl.FXGL.set;
import static com.almasb.fxgl.dsl.FXGL.spawn;

import org.enstabretagne.Component.EntityType;
import org.enstabretagne.Utils.entityNames;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;

/**
 * Gestion des collisions entre les aliens et le joueur
 * En cas de collision, le joueur perd la partie
 * 
 * @author @jufch, @LBF38, @MathieuDFS
 * @since 0.1.0
 */
public class AlienPlayerCollision extends CollisionHandler {
    /**
     * Constructeur de la classe AlienPlayerCollision
     * Création d'une collision entre les entités player et alien
     * 
     * @param player
     * @param alien
     */
    public AlienPlayerCollision() {
        super(EntityType.PLAYER, EntityType.ALIEN);
    }

    /**
     * Gestion des collisions entre les aliens et le joueur
     */
    @Override
    protected void onCollisionBegin(Entity player, Entity alien) {
        spawn(entityNames.EXPLOSION_PLAYER_DEATH, player.getPosition());
        player.removeFromWorld();
        set(GameVariableNames.isGameOver, true);
        play("Explosion/finalExplosion.wav");
    }
}
