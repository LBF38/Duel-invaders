package org.enstabretagne.Component;

import org.enstabretagne.Core.Constant;
import org.enstabretagne.Core.Constant.Direction;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;

import javafx.geometry.Point2D;

import static com.almasb.fxgl.dsl.FXGL.*;

public class AlienComponent extends Component {
    private Double dx, dy;
    protected Direction movementDirection;
    private Double last_shot = 0.0;
    private int AlienNumber;
    private double limit_right = Constant.BOARD_WIDTH;
    private double limit_left =0.0;

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
        dy = entity.getHeight();
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
        if (getEntity().getRightX() + dx <= limit_right) {
            getEntity().translateX(dx);
        } else {
            this.entity.translateY(dy);
            this.movementDirection = Direction.LEFT;
        }
    }

    public void moveLeft(Double dx) {
        if (getEntity().getX() - dx >= limit_left) {
            getEntity().translateX(-dx);
        } else {
            this.entity.translateY(dy);
            this.movementDirection = Direction.RIGHT;
        }
    }

    public void setAlienNumber(int AlienNumber) {
        this.AlienNumber = AlienNumber;
        //calcul les limites de dépalcement de l'alien
        limit_right=Constant.BOARD_WIDTH-(Constant.ALIEN_WIDTH*(Constant.ALIENS_NUMBER-AlienNumber-1));
        limit_left=0.0+(Constant.ALIEN_WIDTH*AlienNumber);
    }

    public void shoot() {
        Boolean canShoot = getGameTimer().getNow() - last_shot.doubleValue() >= Constant.RATE_ALIEN_SHOOT.doubleValue();
        if (canShoot || last_shot == null) {
            Entity bullet = spawn("alienBullet", this.entity.getX() + this.entity.getWidth() / 2,
                    this.entity.getY() + this.entity.getHeight());
            BulletComponent bulletComponent = bullet.getComponent(BulletComponent.class);
            bulletComponent.setDirection(new Point2D(0, 1));
            bulletComponent.initialize();
            last_shot = getGameTimer().getNow();
        }
    }

    public void randomShoot(double chance) {
        if (FXGLMath.randomBoolean(chance))
            shoot();
    }
}
