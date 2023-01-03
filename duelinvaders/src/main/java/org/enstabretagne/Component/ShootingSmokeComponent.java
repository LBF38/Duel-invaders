package org.enstabretagne.Component;

import static org.enstabretagne.Core.Constant.SMOKE_DURATION;

import org.enstabretagne.Core.Constant;

import com.almasb.fxgl.dsl.components.ExpireCleanComponent;
import com.almasb.fxgl.entity.component.Component;

/**
 * Cette classe représente la fumée lors d'un tir.
 *
 * @author MathieuDFS, jufch
 * @since 0.2.0
 */
public class ShootingSmokeComponent extends Component {
    /**
     * Initialise le composant fumée
     * Cette méthode ajoute le composant ExpireCleanComponent
     * 
     * @param direction
     */
    public void initialize(Constant.Direction direction) {
        this.entity.addComponent(new ExpireCleanComponent(SMOKE_DURATION));
        double dx = Constant.SHOOTING_SMOKE_WIDTH / 2;
        double dy = 30;
        if (direction == Constant.Direction.DOWN) {
            this.entity.rotateBy(180);
            this.entity.translate(dx, dy);
        }
        this.entity.translate(-dx, -dy);
    }
}
