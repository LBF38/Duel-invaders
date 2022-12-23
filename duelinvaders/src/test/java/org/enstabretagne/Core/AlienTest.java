package org.enstabretagne.Core;

import static org.junit.jupiter.api.Assertions.assertThrows;
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
        assertTrue(alien.getX() != null);
        alien.setX(5.0);
        assertTrue(alien.getX() == 5.0);
    }

    @Test
    @DisplayName("Test de la propriété y")
    public void testY() {
        assertTrue(alien.getY() != null);
        alien.setY(5.0);
        assertTrue(alien.getY() == 5.0);
    }

    @Test
    @DisplayName("Tests des limites de jeu")
    public void testExceptions() {
        assertThrows(IllegalArgumentException.class, () -> alien.setX(-1.0));
        assertThrows(IllegalArgumentException.class, () -> alien.setX(Constant.BOARD_WIDTH + 1.0));
        assertThrows(IllegalArgumentException.class, () -> alien.setY(-1.0));
        assertThrows(IllegalArgumentException.class, () -> alien.setY(Constant.BOARD_HEIGHT + 1.0));
    }

    @Test
    @DisplayName("Déplacement de l'alien")
    public void movement() {
        assertTrue(alien.getX() == 0);
        alien.move(Constant.Direction.RIGHT);
        assertTrue(alien.getX() == 5); // to change with decision on the movement
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