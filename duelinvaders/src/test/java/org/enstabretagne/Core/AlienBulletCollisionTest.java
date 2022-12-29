package org.enstabretagne.Core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.almasb.fxgl.entity.Entity;


import static org.junit.jupiter.api.Assertions.*;
import org.enstabretagne.Component.*;


import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;





public class AlienBulletCollisionTest {

    @Test
    void initialization() {
        var collision_alien_bullet = new AlienBulletCollision(EntityType.ALIEN, EntityType.BULLET);
        assert collision_alien_bullet != null;

    }





    @Test
    void BonusCollision(){

        //vars.put(GameVariableNames.PLAYER1_SCORE, 0);
        //var GameVariableNames = new GameVariableNames();
        GameVariableNames.PLAYER1_SCORE = "0";
        //vars.put(GameVariableNames.PLAYER1_SCORE, 0);
        assertEquals(GameVariableNames.PLAYER1_SCORE, String.valueOf(0));

        //assert GameVariableNames.PLAYER1_SCORE = String.valueOf(0);
        var collision_alien_bullet = new AlienBulletCollision(EntityType.ALIEN, EntityType.BULLET);
        System.out.println(GameVariableNames.PLAYER1_SCORE);
        //assertEquals(GameVariableNames.PLAYER1_SCORE, String.valueOf(1));
    }

    @Test
    void AlienRemoveTest(){


        var collision_alien_bullet = new AlienBulletCollision(EntityType.ALIEN, EntityType.BULLET);
        //assertEquals(getGameWorld().getEntitiesByType(EntityType.ALIEN).isEmpty(), true);
        assert getGameWorld().getEntitiesByType(EntityType.ALIEN).isEmpty();
    }

    @Test
    void BulletRemoveTest(){
        var collision_alien_bullet = new AlienBulletCollision(EntityType.ALIEN, EntityType.BULLET);
        assert getGameWorld().getEntitiesByType(EntityType.BULLET).isEmpty();
    }

}
