package org.enstabretagne.Component;

import org.enstabretagne.Core.Constant;

import com.almasb.fxgl.entity.component.Component;

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

    public void shoot() {
        System.out.println("Player Shoot");
    }
}
