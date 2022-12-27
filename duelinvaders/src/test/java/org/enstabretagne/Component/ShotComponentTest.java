package org.enstabretagne.Component;

import org.enstabretagne.Core.Constant;
import org.enstabretagne.Core.Constant.Direction;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class ShotComponentTest {
    private ShotComponent shotComponent;
    private Double dy = 1.0;

    @BeforeEach
    public void setup() {
        this.shotComponent = new ShotComponent(0.0, 0.0, Constant.Direction.UP);
    }

    @Test
    public void initializationTest() {
        assertEquals(0.0, this.shotComponent.getX());
        assertEquals(0.0, this.shotComponent.getY());
    }

    @Test
    @DisplayName("Test de la propriété x")
    public void testX() {
        assertNotEquals(null, shotComponent.getX());
        shotComponent.setX(5.0);
        assertEquals(5, shotComponent.getX());
    }

    @Test
    @DisplayName("Test de la propriété y")
    public void testY() {
        assertNotEquals(null, shotComponent.getY());
        shotComponent.setY(5.0);
        assertEquals(5, shotComponent.getY());
    }

    @Test
    @DisplayName("Tests des limites de jeu")
    public void testExceptions() {
        assertThrows(IllegalArgumentException.class, () -> shotComponent.setX(-1.0));
        assertThrows(IllegalArgumentException.class, () -> shotComponent.setX(Constant.BOARD_WIDTH + 1.0));
        assertThrows(IllegalArgumentException.class, () -> shotComponent.setY(-1.0));
        assertThrows(IllegalArgumentException.class, () -> shotComponent.setY(Constant.BOARD_HEIGHT + 1.0));
    }

    @Test
    @DisplayName("Déplacement du shotComponent")
    public void moveUp() {
        shotComponent.setY(dy);
        shotComponent.moveUp(dy);
        assertEquals(0, shotComponent.getY());
    }

    @Test
    public void moveDown() {
        assertEquals(0, shotComponent.getY());
        shotComponent.moveDown(dy);
        assertEquals(dy, shotComponent.getY());
    }

}