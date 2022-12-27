package org.enstabretagne.Component;

import org.enstabretagne.Core.Constant;
import org.enstabretagne.Core.Constant.Direction;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class AlienComponentTest {
    private AlienComponent alienComponent;
    private Double dx = 1.0;
    // private Double dy = 1.0;

    @BeforeEach
    public void setup() {
        this.alienComponent = new AlienComponent(0.0, 0.0, Constant.Direction.DOWN);
    }

    @Test
    public void initializationTest() {
        assertEquals(0.0, this.alienComponent.getX());
        assertEquals(0.0, this.alienComponent.getY());
    }

    @Test
    @DisplayName("Test de la propriété x")
    public void testX() {
        assertNotEquals(null, alienComponent.getX());
        alienComponent.setX(5);
        assertEquals(5, alienComponent.getX());
    }

    @Test
    @DisplayName("Test de la propriété y")
    public void testY() {
        assertNotEquals(null, alienComponent.getY());
        alienComponent.setY(5);
        assertEquals(5, alienComponent.getY());
    }

    @Test
    @DisplayName("Tests des limites de jeu")
    public void testExceptions() {
        assertThrows(IllegalArgumentException.class, () -> alienComponent.setX(-1));
        assertThrows(IllegalArgumentException.class, () -> alienComponent.setX(Constant.BOARD_WIDTH + 1));
        assertThrows(IllegalArgumentException.class, () -> alienComponent.setY(-1));
        assertThrows(IllegalArgumentException.class, () -> alienComponent.setY(Constant.BOARD_HEIGHT + 1));
    }

    @Test
    @DisplayName("Déplacement de l'alienComponent")
    public void moveRight() {
        assertEquals(0, alienComponent.getX());
        alienComponent.moveRight(dx);
        assertEquals(dx, alienComponent.getX()); // to change with decision on the movement
    }

    @Test
    public void moveLeft() {
        alienComponent.setX(dx);
        alienComponent.moveLeft(dx);
        assertEquals(0, alienComponent.getX()); // to change with decision on the movement
    }

    @Test
    public void move() {
        // TODO: vérifier le test. Il ne passe pas pour le moment.
        assertEquals(0, alienComponent.getX());
        assertEquals(alienComponent.movementDirection, Direction.RIGHT);
        for (double i = 0; i < Constant.BOARD_WIDTH; i++) {
            alienComponent.move(dx);
            assertEquals(dx * i + 1, alienComponent.getX());
        }
        assertEquals(1, alienComponent.getY());
        for (double i = 1; i < Constant.BOARD_WIDTH; i++) {
            alienComponent.move(i);
            assertEquals(Constant.BOARD_WIDTH - i, alienComponent.getX());
        }
        assertEquals(2, alienComponent.getY());
    }
}
