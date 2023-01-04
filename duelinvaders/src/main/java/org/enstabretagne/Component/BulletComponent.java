package org.enstabretagne.Component;

import org.enstabretagne.Core.Constant;

import com.almasb.fxgl.dsl.components.ExpireCleanComponent;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.component.Component;

import javafx.geometry.Point2D;
import javafx.util.Duration;

/**
 * Cette classe représente le comportement des balles dans le jeu.
 * Elle permet de définir le comportement de base d'une balle dans le jeu
 * 
 * @author LBF38
 * @since 0.1.0
 */
public class BulletComponent extends Component {
    private Double speed = Constant.SPEED_SHOOT;
    private Duration duration = Constant.BULLET_DURATION;
    private Point2D direction = new Point2D(0, -1);
    private int playerId;

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int player_id) {
        this.playerId = player_id;
    }

    public BulletComponent() {
        super();
    }

    /**
     * Initialise le composant de la balle
     * Cette méthode ajoute les composants ProjectileComponent et
     * ExpireCleanComponent
     * Ce sont des composants prédéfinis dans la librairie FXGL qui facilite la
     * création de projectiles
     */
    public void initialize(Constant.Direction UporDown) {
        this.direction = new Point2D(0, UporDown == Constant.Direction.UP ? -1 : 1);
        this.entity.addComponent(new ProjectileComponent(direction, speed));
        this.entity.addComponent(new ExpireCleanComponent(duration));
    }

    /**
     * Méthode pour changer la direction d'affichage de la balle
     * 
     * @param angle
     */
    public void rotate(double angle) {
        this.entity.rotateBy(angle);
    }

    /**
     * Méthode pour changer la direction de déplacement de la balle
     * 
     * @param direction
     */
    public void setDirection(Point2D direction) {
        this.direction = direction;
    }
}
