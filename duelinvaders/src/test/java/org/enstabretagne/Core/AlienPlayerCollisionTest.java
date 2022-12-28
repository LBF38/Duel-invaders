package org.enstabretagne.Core;

import org.enstabretagne.Component.EntityType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AlienPlayerCollisionTest {
    private AlienPlayerCollision collision_alien_player;


    @BeforeEach
    public void setUp() {
        collision_alien_player = new AlienPlayerCollision(EntityType.PLAYER, EntityType.ALIEN);
    }

    @Test
    void initialization() {
        assert collision_alien_player != null;

    }


}
