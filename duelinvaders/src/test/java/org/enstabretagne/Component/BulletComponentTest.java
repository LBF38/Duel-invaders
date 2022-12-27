package org.enstabretagne.Component;

import org.junit.jupiter.api.*;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static org.junit.jupiter.api.Assertions.*;

public class BulletComponentTest {
    private BulletComponent bulletComponent;
    // private Double dy = 1.0;

    @BeforeEach
    public void setup() {

        bulletComponent = new BulletComponent();
        entityBuilder()
                .type(EntityType.SHOT)
                .at(0, 0)
                .with(bulletComponent)
                .build();
    }

    @Test
    public void initializationTest() {
        assertEquals(0.0, this.bulletComponent.getEntity().getX());
        assertEquals(0.0, this.bulletComponent.getEntity().getY());
    }

    // TODO: Fix the test for the BulletComponent (with ProjectileComponent)
    // @Test
    // @DisplayName("Déplacement du shotComponent vers le haut")
    // public void moveUp() {
    // bulletComponent.getEntity().setY(dy);
    // bulletComponent.moveUp(dy);
    // assertEquals(0, bulletComponent.getEntity().getY());
    // }

    // @Test
    // @DisplayName("Déplacement du shotComponent vers le bas")
    // public void moveDown() {
    // assertEquals(0, bulletComponent.getEntity().getY());
    // bulletComponent.moveDown(dy);
    // assertEquals(dy, bulletComponent.getEntity().getY());
    // }

}