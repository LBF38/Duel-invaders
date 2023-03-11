package org.enstabretagne.Component;

import org.enstabretagne.Utils.EntityType;
import org.enstabretagne.Utils.Settings;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import static com.almasb.fxgl.dsl.FXGL.*;

public class PlayerComponentTest {
    private PlayerComponent playerComponent;
    private Double dx = Settings.SPEED_SPACESHIP;

    @BeforeEach
    public void setup() {
        playerComponent = new PlayerComponent();
        entityBuilder()
                .type(EntityType.PLAYER)
                .at(0, 0)
                .with(playerComponent)
                .build();
        playerComponent.onUpdate(1.0);
    }

    @Test
    public void initializationTest() {
        assertEquals(0.0, playerComponent.getEntity().getX());
        assertEquals(0.0, playerComponent.getEntity().getY());
    }

    @Test
    @DisplayName("Proriété x de l'entité")
    public void testX() {
        assertNotEquals(null, playerComponent.getEntity().getX());
        playerComponent.getEntity().setX(5);
        assertEquals(5, playerComponent.getEntity().getX());
    }

    @Test
    @DisplayName("Proriété y de l'entité")
    public void testY() {
        assertNotEquals(null, playerComponent.getEntity().getY());
        playerComponent.getEntity().setY(5);
        assertEquals(5, playerComponent.getEntity().getY());
    }

    @Test
    @DisplayName("Déplacement vers la droite")
    public void moveRight() {
        playerComponent.getEntity().setX(0);
        assertEquals(0, playerComponent.getEntity().getX());
        playerComponent.moveRight();
        assertEquals(dx, playerComponent.getEntity().getX()); // to change with decision on the movement
    }

    @Test
    @DisplayName("Déplacement vers la gauche")
    public void moveLeft() {
        playerComponent.getEntity().setX(dx);
        assertEquals(dx, playerComponent.getEntity().getX()); // to change with decision on the movement
        playerComponent.moveLeft();
        assertEquals(0, playerComponent.getEntity().getX()); // to change with decision on the movement
    }
}
