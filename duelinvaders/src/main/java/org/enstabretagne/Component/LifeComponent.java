package org.enstabretagne.Component;

import org.enstabretagne.Utils.Settings;

import com.almasb.fxgl.entity.component.Component;

/**
 * Cette classe représente l'affichage du nombre de vies restantes.
 *
 * @author MathieuDFS, jufch
 * @since 0.2.0
 * @deprecated since 0.3.0
 */
public class LifeComponent extends Component {
    /**
     * Initialise le composant affichage de vie
     */
    public void initialize(Settings.Direction direction) {
        if (direction == Settings.Direction.UP) {
            entity.setX(Settings.GAME_WIDTH - Settings.LIFE_DISPLAY_WIDTH);
            entity.setY(0);
        } else if (direction == Settings.Direction.DOWN) {
            entity.setX(Settings.GAME_WIDTH - Settings.LIFE_DISPLAY_WIDTH);
            entity.setY(Settings.GAME_HEIGHT - Settings.LIFE_DISPLAY_HEIGHT);
        }
    }

    public void updateLife(boolean isActive) {
        entity.getViewComponent().setOpacity(isActive ? 1 : 0);
    }
}
