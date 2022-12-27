package org.enstabretagne.Component;

import org.enstabretagne.Core.Constant;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import static com.almasb.fxgl.dsl.FXGL.*;

public class AlienComponentTest {
    private AlienComponent alienComponent;
    private Double dx = 100.0;

    @BeforeEach
    public void setup() {
        alienComponent = new AlienComponent(Constant.Direction.DOWN);
        entityBuilder()
                .type(EntityType.ALIEN)
                .at(0, 0)
                .with(alienComponent)
                .build();

    }

    @Test
    public void initializationTest() {
        assertEquals(0.0, alienComponent.getEntity().getX());
        assertEquals(0.0, alienComponent.getEntity().getY());
    }

    @Test
    @DisplayName("Proriété x de l'entité")
    public void testX() {
        assertNotEquals(null, alienComponent.getEntity().getX());
        alienComponent.getEntity().setX(5);
        assertEquals(5, alienComponent.getEntity().getX());
    }

    @Test
    @DisplayName("Proriété y de l'entité")
    public void testY() {
        assertNotEquals(null, alienComponent.getEntity().getY());
        alienComponent.getEntity().setY(5);
        assertEquals(5, alienComponent.getEntity().getY());
    }

    @Test
    @DisplayName("Déplacement vers la droite")
    public void moveRight() {
        alienComponent.getEntity().setX(0);
        assertEquals(0, alienComponent.getEntity().getX());
        alienComponent.moveRight(dx);
        assertEquals(dx, alienComponent.getEntity().getX()); // to change with decision on the movement
    }

    @Test
    @DisplayName("Déplacement vers la gauche")
    public void moveLeft() {
        alienComponent.getEntity().setX(dx);
        assertEquals(dx, alienComponent.getEntity().getX()); // to change with decision on the movement
        alienComponent.moveLeft(dx);
        assertEquals(0, alienComponent.getEntity().getX()); // to change with decision on the movement
    }

    // Test d'intégration pour move (à voir si on le garde)
    // @Test
    // public void testMove() {
    // double x = alienComponent.getEntity().getX();
    // double y = alienComponent.getEntity().getY();
    // double tpf = 1.0;
    // double dx = tpf * Constant.SPEED_ALIEN;
    // double dy = 0.5 * Constant.SPEED_ALIEN;

    // // Déplacement à droite
    // for (int i = 0; i < Constant.BOARD_WIDTH / dx; i++) {
    // alienComponent.onUpdate(tpf);
    // x += dx;
    // assertEquals(x, alienComponent.getEntity().getX(), 0.001);
    // assertEquals(y, alienComponent.getEntity().getY(), 0.001);
    // }

    // assertEquals(Direction.LEFT, alienComponent.movementDirection);

    // // Changement de direction et déplacement vers la gauche
    // for (int i = 0; i < 5; i++) {
    // alienComponent.onUpdate(tpf);
    // x -= dx;
    // y += dy;
    // assertEquals(x, alienComponent.getEntity().getX(), 0.001);
    // assertEquals(y, alienComponent.getEntity().getY(), 0.001);
    // }

    // // Déplacement vers la droite
    // for (int i = 0; i < 5; i++) {
    // alienComponent.onUpdate(tpf);
    // x += dx;
    // assertEquals(x, alienComponent.getEntity().getX(), 0.001);
    // assertEquals(y, alienComponent.getEntity().getY(), 0.001);
    // }
    // }
}
