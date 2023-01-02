package org.enstabretagne.Component;

import org.enstabretagne.Core.Constant;

import com.almasb.fxgl.dsl.components.ExpireCleanComponent;
import com.almasb.fxgl.entity.component.Component;

import javafx.util.Duration;
import org.enstabretagne.Core.GameVariableNames;

/**
 * Cette classe représente l'affichage du nombre de vies restantes.
 *
 * @author @MathieuDFS, @jufch
 */
public class LifeComponent extends Component{
    private Constant.Direction direction;
    private boolean isActive = true;
    /**
     * Initialise le composant affichage de vie
     */
    public void initialize(Constant.Direction direction) {
        this.direction = direction;
        if(direction == Constant.Direction.UP){
            entity.setX(Constant.GAME_WIDTH-Constant.LIFE_DISPLAY_WIDTH);
            entity.setY(0);
        }
        else if(direction == Constant.Direction.DOWN){
            entity.setX(Constant.GAME_WIDTH-Constant.LIFE_DISPLAY_WIDTH);
            entity.setY(Constant.GAME_HEIGHT-Constant.LIFE_DISPLAY_HEIGHT);
        }
    }

    public void updateLife(boolean isActive){
        this.isActive = isActive;
        if(this.isActive){
            entity.getViewComponent().setOpacity(1);
        }
        else{
            entity.getViewComponent().setOpacity(0);
        }
    }
}

