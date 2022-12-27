package org.enstabretagne.Core;

import org.enstabretagne.Component.EntityType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Collision_alien_player_test {
    private Collision_alien_player collision_alien_player;


    @BeforeEach
    public void setUp() {
        collision_alien_player = new Collision_alien_player(EntityType.PLAYER, EntityType.ALIEN);
    }

    @Test
    void initialization() {
        assert collision_alien_player != null;

    }


}
