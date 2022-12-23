package org.enstabretagne.Core;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.*;

public class AlienTest {
    private Alien alien;

    @BeforeEach
    public void setup() {
        alien = new Alien(0.0, 0.0, Constant.Direction.DOWN);
    }

    // Test sur les méthodes et propriétés d'un Element
    @Test
    @DisplayName("Test de la propriété x")
    public void testX() {
        assertTrue(alien.x != null);
        alien.x = 5.0;
        assertTrue(alien.x == 5.0);
    }

    @Test
    @DisplayName("Test de la propriété y")
    public void testY() {
        assertTrue(alien.y != null);
        alien.y = 5.0;
        assertTrue(alien.y == 5.0);
    }

    @Test
    @DisplayName("Déplacement de l'alien")
    public void movement() {
        assertTrue(alien.x == 0);
        alien.move(Constant.Direction.RIGHT);
        assertTrue(alien.x == 5); // to change with decision on the movement
    }
}

/*
 * Alien tests :
 * 
 * # What aliens are allowed to do ? (cf. Modelisation)
 * 
 * This is for the bottom player, we can easily mirror things.
 * - can only move from RTL and downward if limits are atteigned.
 * - can shoot randomly at a given pace. (to determine)
 * - if shot touch a player (Cannon), player and shot are destroyed.
 */