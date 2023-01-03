package org.enstabretagne.Component;

import org.enstabretagne.Core.Constant;

import com.almasb.fxgl.entity.component.Component;

/**
 * Cette classe représente l'affichage du nombre de vies restantes.
 *
 * @author MathieuDFS, jufch
 * @since 0.2.0
 */
public class LifeComponent extends Component {
    /**
     * Initialise le composant affichage de vie
     */
    public void initialize(Constant.Direction direction) {
        if (direction == Constant.Direction.UP) {
            entity.setX(Constant.GAME_WIDTH - Constant.LIFE_DISPLAY_WIDTH);
            entity.setY(0);
        } else if (direction == Constant.Direction.DOWN) {
            entity.setX(Constant.GAME_WIDTH - Constant.LIFE_DISPLAY_WIDTH);
            entity.setY(Constant.GAME_HEIGHT - Constant.LIFE_DISPLAY_HEIGHT);
        }
    }

    public void updateLife(boolean isActive) {
        entity.getViewComponent().setOpacity(isActive ? 1 : 0);
    }
}
