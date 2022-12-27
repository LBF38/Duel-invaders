package org.enstabretagne.Component;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.enstabretagne.Core.Constant;
import org.enstabretagne.Core.Constant.Direction;
import org.junit.jupiter.api.*;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static org.junit.jupiter.api.Assertions.*;

public class ShotComponentTest {
    private ShotComponent shotComponent;
    private Double dy = 1.0;

    @BeforeEach
    public void setup() {

        shotComponent = new ShotComponent(0.0, 0.0, Constant.Direction.UP);
        entityBuilder()
                .type(EntityType.SHOT)
                .at(0, 0)
                .with(shotComponent)
                .build();
    }

    @Test
    public void initializationTest() {
        assertEquals(0.0, this.shotComponent.getEntity().getX());
        assertEquals(0.0, this.shotComponent.getEntity().getY());
    }

    @Test
    @DisplayName("Déplacement du shotComponent vers le haut")
    public void moveUp() {
        shotComponent.getEntity().setY(dy);
        shotComponent.moveUp(dy);
        assertEquals(0, shotComponent.getEntity().getY());
    }

    @Test
    @DisplayName("Déplacement du shotComponent vers le bas")
    public void moveDown() {
        assertEquals(0, shotComponent.getEntity().getY());
        shotComponent.moveDown(dy);
        assertEquals(dy, shotComponent.getEntity().getY());
    }

}