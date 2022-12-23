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
    @DisplayName("Déplacement de l'alien")
    public void movement() {
        System.out.println(alien);
        assertTrue(alien.getX() == 0);
        alien.move(Constant.Direction.RIGHT);
        assertTrue(alien.getX() == 5); // to change with decision on the movement
    }
}
