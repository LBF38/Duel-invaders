package org.enstabretagne.Component;

import org.enstabretagne.Core.Constant;

import com.almasb.fxgl.dsl.components.ExpireCleanComponent;
import com.almasb.fxgl.entity.component.Component;

import javafx.util.Duration;


/**
 * Cette classe représente le départ d'un tir.
 *
 * @author @MathieuDFS, @jufch
 */
public class ShootingStartComponent extends Component {
    private Duration duration = Constant.FIRE_DURATION;

    /**
     * Initialise le composant départ du tir (feu)
     * Cette méthode ajoute le composant ExpireCleanComponent
     */
    public void initialize(Constant.Direction direction) {
        this.entity.addComponent(new ExpireCleanComponent(duration));
        double dx = 0;
        double dy =0;

        if (direction == Constant.Direction.DOWN) {
            this.entity.rotateBy(180);
            dx=Constant.SHOOTING_START_WIDTH / 2;
        }
        else if(direction == Constant.Direction.UP){
            dx=-Constant.SHOOTING_START_WIDTH / 2;

        }
        this.entity.translate( dx,dy);
    }

}
