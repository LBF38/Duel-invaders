package org.enstabretagne.Component;

import static org.enstabretagne.Utils.Settings.SMOKE_DURATION;

import org.enstabretagne.Utils.Settings;

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
    public void initialize(Settings.Direction direction) {
        this.entity.addComponent(new ExpireCleanComponent(SMOKE_DURATION));
        double dx = Settings.SHOOTING_SMOKE_WIDTH / 2;
        double dy = 30;
        if (direction == Settings.Direction.DOWN) {
            this.entity.rotateBy(180);
            this.entity.translate(dx, dy);
        }
        this.entity.translate(-dx, -dy);
    }
}
