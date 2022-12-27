package org.enstabretagne.Component;

import org.enstabretagne.Core.Constant;
import org.enstabretagne.Core.Constant.Direction;

import com.almasb.fxgl.entity.component.Component;

//test new organization
public class AlienComponent extends Component {
    private Double x, y;
    private Direction direction;
    protected Direction movementDirection;

    public AlienComponent(Double x, Double y, Direction direction) {
        super();
        this.x = x;
        this.y = y;
        this.direction = direction;
        if (direction == Direction.DOWN)
            this.movementDirection = Direction.RIGHT;
        else
            this.movementDirection = Direction.DOWN;
    }

    @Override
    public void onUpdate(double tpf) {
        // FIXME: the alien is moving out of bounds.
        // This is because we update `translateX` and `translateY` after the alien has
        // moved.
        // We should update the position of the component with getX and getY.
        Double dx = tpf * Constant.SPEED_ALIEN;
        this.move(dx);
        this.entity.translateX(this.x);
        this.entity.translateY(this.y);
    }

    public void move(Double dx) throws IllegalArgumentException {
        if (this.y + dx == Constant.BOARD_HEIGHT)
            System.out.println("Game Over");

        if (this.x + dx == Constant.BOARD_WIDTH) {
            this.moveDown(dx); // Could be changed to make the aliens move slower.
            // this.moveLeft(dx);
            this.movementDirection = Direction.LEFT;
        } else if (this.x + dx == 0) {
            this.moveDown(dx);
            // this.moveRight(dx);
            this.movementDirection = Direction.RIGHT;
        }

        if (this.movementDirection == Direction.RIGHT)
            this.moveRight(dx);
        else if (this.movementDirection == Direction.LEFT)
            this.moveLeft(dx);
    }

    public void moveRight(Double dx) {
        this.x = this.x + dx;
    }

    public void moveLeft(Double dx) {
        this.x = this.x - dx;
    }

    public void moveUp(Double dy) {
        this.y = this.y - dy;
    }

    public void moveDown(Double dy) {
        this.y = this.y + dy;
    }

    public Double getX() {
        return x;
    }

    public AlienComponent setX(Double x) throws IllegalArgumentException {
        if (x < 0 || x > Constant.BOARD_WIDTH) {
            throw new IllegalArgumentException("x is out of the board");
        }
        this.x = x;
        return this;
    }

    public AlienComponent setX(Integer x) throws IllegalArgumentException {
        this.setX(x.doubleValue());
        return this;
    }

    public Double getY() {
        return y;
    }

    public AlienComponent setY(Double y) throws IllegalArgumentException {
        if (y < 0 || y > Constant.BOARD_HEIGHT) {
            throw new IllegalArgumentException("y is out of the board");
        }
        this.y = y;
        return this;
    }

    public AlienComponent setY(Integer y) throws IllegalArgumentException {
        this.setY(y.doubleValue());
        return this;
    }

    // TODO: verify if this methods are necessary, otherwise remove them.
    // public Direction getDirection() {
    // return direction;
    // }

    // public AlienComponent setDirection(Direction direction) throws
    // IllegalArgumentException {
    // if (direction != Direction.DOWN || direction != Direction.UP) {
    // throw new IllegalArgumentException("Illegal direction : The Cannon can only
    // be UP or DOWN");
    // }
    // this.direction = direction;
    // return this;
    // }
}
