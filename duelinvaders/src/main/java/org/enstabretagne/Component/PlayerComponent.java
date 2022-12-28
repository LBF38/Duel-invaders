package org.enstabretagne.Component;

import static org.enstabretagne.Core.Constant.*;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;

import static com.almasb.fxgl.dsl.FXGL.*;

public class PlayerComponent extends Component {
    private Double dx;
    private Double last_shot = 0.0;

    @Override
    public void onUpdate(double tpf) {
        dx = tpf * SPEED_SPACESHIP;
    }

    public void moveRight() {
        if (this.entity.getRightX() + dx <= BOARD_WIDTH) {
            this.entity.translateX(dx);
        }
    }

    public void moveLeft() {
        if (this.entity.getX() - dx >= 0) {
            this.entity.translateX(-dx);
        }
    }

    public void shoot() {
        Boolean canShoot = getGameTimer().getNow() - last_shot.doubleValue() >= DELAY_BETWEEN_SHOOT.toSeconds();
        if (canShoot || last_shot == null) {
            Entity bullet = spawn("bullet", entity.getX() + (entity.getWidth() / 2), entity.getY() - 20);
            bullet.getComponent(BulletComponent.class).initialize();
            last_shot = getGameTimer().getNow();
        }
    }
}
