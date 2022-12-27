package org.enstabretagne.Component;

import org.enstabretagne.Core.Constant;

import com.almasb.fxgl.dsl.components.ExpireCleanComponent;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.component.Component;

import javafx.geometry.Point2D;
import javafx.util.Duration;

public class BulletComponent extends Component {
    private Double speed = Constant.SPEED_SHOOT;
    private Duration duration = Constant.BULLET_DURATION;
    private Point2D direction = new Point2D(0, -1);

    public BulletComponent() {
        super();

        // this.entity.addComponent(new ProjectileComponent(direction, speed));
        // this.entity.addComponent(new ExpireCleanComponent(duration));
    }

    public void initialize() {
        this.entity.addComponent(new ProjectileComponent(direction, speed));
        this.entity.addComponent(new ExpireCleanComponent(duration));
        this.entity.rotateBy(90);
    }

    public void rotate(double angle) {
        this.entity.rotateBy(angle);
    }
}
