package org.enstabretagne.Component;

import org.enstabretagne.Core.Constant;

import com.almasb.fxgl.dsl.components.ExpireCleanComponent;
import com.almasb.fxgl.entity.component.Component;

import javafx.util.Duration;

/**
 * Cette classe représente la fumée lors d'un tir.
 *
 * @author @MathieuDFS, @jufch
 */
public class ShootingSmokeComponent extends Component{
    private Duration duration = Constant.SMOKE_DURATION;

    /**
     * Initialise le composant fumée
     * Cette méthode ajoute le composant ExpireCleanComponent
     */
    public void initialize(Constant.Direction direction) {
        this.entity.addComponent(new ExpireCleanComponent(duration));
        double dx = 0;
        double dy =0;
        if (direction == Constant.Direction.DOWN) {
            this.entity.rotateBy(180);
            dx = Constant.SHOOTING_SMOKE_WIDTH / 2;
            dy = 30;
        }
        else if (direction == Constant.Direction.UP) {
            dx =- Constant.SHOOTING_SMOKE_WIDTH / 2;
            dy=- 30;
        }
        this.entity.translate(dx,dy);
    }
}
