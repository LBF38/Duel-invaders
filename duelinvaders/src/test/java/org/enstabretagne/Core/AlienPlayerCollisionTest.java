package org.enstabretagne.Core;

import org.enstabretagne.Component.AlienComponent;
import org.enstabretagne.Component.PlayerComponent;
import org.enstabretagne.Component.EntityType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getWorldProperties;

public class AlienPlayerCollisionTest {
    private AlienPlayerCollision collision_alien_player;

/*
    @BeforeEach
    public void setUp() {
        collision_alien_player = new AlienPlayerCollision(EntityType.PLAYER, EntityType.ALIEN);
    }
*/

    private AlienComponent alienComponent;
    private PlayerComponent playerComponent;

    @BeforeEach
    public void setup() {
        alienComponent = new AlienComponent(Constant.Direction.DOWN);
        entityBuilder()
            .type(EntityType.ALIEN)
            .at(0, 0)
            .with(alienComponent)
            .build();

        playerComponent = new PlayerComponent();
        entityBuilder()
            .type(EntityType.PLAYER)
            .at(0, 0)
            .with(playerComponent)
            .build();
    }
    @Test
    void initialization() {
        assert collision_alien_player != null;
    }

    @Test
    void testRemoveFromWorld(){
        //getWorldProperties();
        assert playerComponent != null;
        assert alienComponent != null;

        collision_alien_player.onCollisionBegin(playerComponent.getEntity(), alienComponent.getEntity());
        //var collision = collision_alien_player.onCollisionBegin(playerComponent.getEntity(), alienComponent.getEntity());
        assert playerComponent.getEntity() == null;
        assert alienComponent.getEntity() != null;
    }

}
