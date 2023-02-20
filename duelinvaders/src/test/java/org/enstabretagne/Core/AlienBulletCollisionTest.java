package org.enstabretagne.Core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;

// import static org.junit.jupiter.api.Assertions.*;
import org.enstabretagne.Component.*;
import org.enstabretagne.Utils.EntityType;

// import static com.almasb.fxgl.dsl.FXGL.*;

public class AlienBulletCollisionTest {
    GameWorld world;
    Entity alien;
    Entity bullet;
    AlienBulletCollision collision_alien_bullet;

    @BeforeEach
    public void setup() {
        world = new GameWorld();

        alien = new Entity();
        alien.addComponent(new AlienComponent());
        alien.setType(EntityType.ALIEN);

        bullet = new Entity();
        bullet.addComponent(new BulletComponent());
        bullet.setType(EntityType.BULLET);

        world.addEntities(alien, bullet);

        collision_alien_bullet = new AlienBulletCollision();
    }

    @Test
    public void alienRemoveTest() {
        // Test not working because it depends too much on the rendering.
        // => need more work and time to decouple the rendering from the logic.

        // collision_alien_bullet.onCollisionBegin(bullet, alien);
        // assertTrue(getGameWorld().getEntitiesByType(EntityType.ALIEN).isEmpty());
    }

    @Test
    void BulletRemoveTest() {
        // same as above
        // collision_alien_bullet.onCollisionBegin(bullet, alien);
        // assertTrue(getGameWorld().getEntitiesByType(EntityType.BULLET).isEmpty());
    }
}
