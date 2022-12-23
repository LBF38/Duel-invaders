package org.enstabretagne.Core;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.*;

public class AlienTest {
    private Alien alien;

    @BeforeEach
    public void setup() {
        alien = new Alien(0, 0, Constant.Direction.DOWN);
    }

    // Test sur les méthodes et propriétés d'un Element
    @Test
    @DisplayName("Test de la propriété x")
    public void testX() {
        assertTrue(alien.getX() != null);
        alien.setX(5);
        assertTrue(alien.getX() == 5);
    }

    @Test
    @DisplayName("Test de la propriété y")
    public void testY() {
        assertTrue(alien.getY() != null);
        alien.setY(5);
        assertTrue(alien.getY() == 5);
    }

    @Test
    @DisplayName("Tests des limites de jeu")
    public void testExceptions() {
        assertThrows(IllegalArgumentException.class, () -> alien.setX(-1));
        assertThrows(IllegalArgumentException.class, () -> alien.setX(Constant.BOARD_WIDTH + 1));
        assertThrows(IllegalArgumentException.class, () -> alien.setY(-1));
        assertThrows(IllegalArgumentException.class, () -> alien.setY(Constant.BOARD_HEIGHT + 1));
    }

    @Test
    @DisplayName("Déplacement de l'alien")
    public void moveRight() {
        assertTrue(alien.getX() == 0);
        alien.moveRight();
        assertTrue(alien.getX() == 1); // to change with decision on the movement
    }

    @Test
    public void moveLeft() {
        alien.setX(1);
        alien.moveLeft();
        assertTrue(alien.getX() == 0); // to change with decision on the movement
    }

    @Test
    public void move() {
        assertTrue(alien.getX() == 0);
        for (int i = 0; i < Constant.BOARD_WIDTH; i++) {
            alien.move();
            assertTrue(alien.getX() == i + 1);
        }
        assertTrue(alien.getY() == 1);
        for (int i = 0; i < Constant.BOARD_WIDTH; i++) {
            alien.move();
            assertTrue(alien.getX() == Constant.BOARD_WIDTH - i - 1);
        }
        assertTrue(alien.getY() == 2);
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