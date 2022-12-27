package org.enstabretagne.Component;

import org.enstabretagne.Core.Constant;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import static com.almasb.fxgl.dsl.FXGL.spawn;

public class PlayerComponent extends Component {
    private Double dx;

    @Override
    public void onUpdate(double tpf) {
        dx = tpf * Constant.SPEED_SPACESHIP;
    }

    public void moveRight() {
        if (this.entity.getRightX() + dx <= Constant.BOARD_WIDTH) {
            this.entity.translateX(dx);
        }
    }

    public void moveLeft() {
        if (this.entity.getX() - dx >= 0) {
            this.entity.translateX(-dx);
        }
    }

    public Entity shoot() {
        System.out.println("Player Shoot");
        Entity bullet = spawn("bullet", entity.getX() + (entity.getWidth() / 2), entity.getY() - 20);
        bullet.getComponent(BulletComponent.class).initialize();
        return bullet;
    }
}
