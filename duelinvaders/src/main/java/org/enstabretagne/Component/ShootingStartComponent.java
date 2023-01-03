package org.enstabretagne.Component;

import static org.enstabretagne.Core.Constant.FIRE_DURATION;
import static org.enstabretagne.Core.Constant.SHOOTING_START_WIDTH;

import org.enstabretagne.Core.Constant.Direction;

import com.almasb.fxgl.dsl.components.ExpireCleanComponent;
import com.almasb.fxgl.entity.component.Component;

/**
 * Cette classe représente le départ d'un tir.
 *
 * @author MathieuDFS, jufch
 * @since 0.2.0
 */
public class ShootingStartComponent extends Component {
    /**
     * Initialise le composant départ du tir (feu)
     * Cette méthode ajoute le composant ExpireCleanComponent
     * 
     * @param direction
     */
    public void initialize(Direction direction) {
        this.entity.addComponent(new ExpireCleanComponent(FIRE_DURATION));
        double dx = SHOOTING_START_WIDTH / 2;
        double dy = 0;

        if (direction == Direction.DOWN) {
            this.entity.rotateBy(180);
        } else if (direction == Direction.UP) {
            dx = -dx;
        }
        this.entity.translate(dx, dy);
    }

}
