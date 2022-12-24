package org.enstabretagne.Core;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.*;

public class ShotTest {
    private Shot shot;

    @BeforeEach
    public void setup() {
        shot = new Shot(0, 0, Constant.Direction.DOWN);
    }

    // Test sur les méthodes et propriétés d'un Element
    @Test
    @DisplayName("Test de la propriété x")
    public void testX() {
        assertTrue(shot.getX() != null);
        shot.setX(5);
        assertTrue(shot.getX() == 5);
    }

    @Test
    @DisplayName("Test de la propriété y")
    public void testY() {
        assertTrue(shot.getY() != null);
        shot.setY(5);
        assertTrue(shot.getY() == 5);
    }

    @Test
    @DisplayName("Tests des limites de jeu")
    public void testExceptions() {
        assertThrows(IllegalArgumentException.class, () -> shot.setX(-1));
        assertThrows(IllegalArgumentException.class, () -> shot.setX(Constant.BOARD_WIDTH + 1));
        assertThrows(IllegalArgumentException.class, () -> shot.setY(-1));
        assertThrows(IllegalArgumentException.class, () -> shot.setY(Constant.BOARD_HEIGHT + 1));
    }

    @Test
    @DisplayName("Déplacement du shot")
    public void moveUp() {
        assertTrue(shot.getY() == 0);
        // shot.moveUp();
        assertTrue(shot.getY() == 1); // to change with decision on the movement
    }

    @Test
    public void moveDown() {
        shot.setX(1);
        // shot.moveDown();
        assertTrue(shot.getX() == 0); // to change with decision on the movement
    }

    @Test
    public void move() {
        assertTrue(shot.getX() == 0);
        for (int i = 0; i < Constant.BOARD_WIDTH; i++) {
            shot.move();
            assertTrue(shot.getX() == i + 1);
        }
        assertTrue(shot.getY() == 1);
        for (int i = 0; i < Constant.BOARD_WIDTH; i++) {
            shot.move();
            assertTrue(shot.getX() == Constant.BOARD_WIDTH - i - 1);
        }
        assertTrue(shot.getY() == 2);
    }
}

/*
 * Shot tests :
 * 
 * # What shots are allowed to do ? (cf. Modelisation)
 * 
 * This is for the bottom player, we can easily mirror things.
 * - can only move vertically (up or down)
 * - if it hits the border of the board, it disappears
 * - if it hits an alien, it disappears and the alien dies
 * - if it hits a player, it disappears and the player dies
 */