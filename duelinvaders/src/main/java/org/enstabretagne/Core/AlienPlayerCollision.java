package org.enstabretagne.Core;

import static com.almasb.fxgl.dsl.FXGL.play;
import static com.almasb.fxgl.dsl.FXGL.set;
import static com.almasb.fxgl.dsl.FXGL.spawn;

import org.enstabretagne.Utils.EntityType;
import org.enstabretagne.Utils.GameVariableNames;
import org.enstabretagne.Utils.assetNames;
import org.enstabretagne.Utils.entityNames;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;

/**
 * Gestion des collisions entre les aliens et le joueur
 * En cas de collision, le joueur perd la partie
 * 
 * @author jufch, LBF38, MathieuDFS
 * @since 0.1.0
 */
public class AlienPlayerCollision extends CollisionHandler {
    /**
     * Constructeur de la classe AlienPlayerCollision
     * Création d'une collision entre les entités player et alien
     */
    public AlienPlayerCollision() {
        super(EntityType.PLAYER, EntityType.ALIEN);
    }

    /**
     * Gestion des collisions entre les aliens et le joueur
     */
    @Override
    protected void onCollisionBegin(Entity player, Entity alien) {
        player.removeFromWorld();
        spawn(entityNames.EXPLOSION_PLAYER_DEATH, player.getPosition());
        set(GameVariableNames.isGameOver, true);
        play(assetNames.sounds.EXPLOSION_PLAYER_DEATH);
    }
}
