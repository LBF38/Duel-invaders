package org.enstabretagne.Component;

import org.enstabretagne.Core.Constant;
import org.enstabretagne.Core.Constant.Direction;

import com.almasb.fxgl.entity.component.Component;

public class AlienComponent extends Component {
    private Double dx, dy;
    protected Direction movementDirection;

    public AlienComponent(Direction direction) {
        super();
        if (direction == Direction.DOWN)
            this.movementDirection = Direction.RIGHT;
        else
            this.movementDirection = Direction.DOWN;
    }

    public AlienComponent() {
        this(Direction.DOWN);
    }

    @Override
    public void onUpdate(double tpf) {
        dx = tpf * Constant.SPEED_ALIEN;
        dy = 0.5 * Constant.SPEED_ALIEN;
        this.move(dx);
    }

    public void move(Double dx) {
        if (this.entity.getBottomY() >= Constant.BOARD_HEIGHT) {
            System.out.println("Game Over");
            return;
        }

        if (this.movementDirection == Direction.RIGHT)
            moveRight(dx);
        else if (this.movementDirection == Direction.LEFT)
            moveLeft(dx);
    }

    public void moveRight(Double dx) {
        if (getEntity().getRightX() + dx <= Constant.BOARD_WIDTH) {
            getEntity().translateX(dx);
        } else {
            this.entity.translateY(dy);
            this.movementDirection = Direction.LEFT;
        }
    }

    public void moveLeft(Double dx) {
        if (getEntity().getX() - dx >= 0) {
            getEntity().translateX(-dx);
        } else {
            this.entity.translateY(dy);
            this.movementDirection = Direction.RIGHT;
        }
    }
}
