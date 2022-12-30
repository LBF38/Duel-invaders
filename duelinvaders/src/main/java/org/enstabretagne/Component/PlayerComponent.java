package org.enstabretagne.Component;

import static org.enstabretagne.Core.Constant.*;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.*;

/**
 * Définition du composant pour le joueur
 * 
 * @author @MathieuDFS, @jufch, @LBF38
 * @since 0.1.0
 */
public class PlayerComponent extends Component {
    private Double dx;
    private Double last_shot = 0.0;
    private int side_shoot = 0;

    /**
     * Mise à jour de la vitesse du joueur à chaque frame.
     */
    @Override
    public void onUpdate(double tpf) {
        dx = tpf * SPEED_SPACESHIP;
    }

    /**
     * Déplacement du vaisseau vers la droite
     * Cette méthode est appelée par le système de contrôle
     */
    public void moveRight() {
        if (this.entity.getRightX() + dx <= GAME_WIDTH) {
            this.entity.translateX(dx);
        }
    }

    /**
     * Déplacement du vaisseau vers la gauche
     * Cette méthode est appelée par le système de contrôle
     */
    public void moveLeft() {
        if (this.entity.getX() - dx >= 0) {
            this.entity.translateX(-dx);
        }
    }

    /**
     * Tir du vaisseau
     * Cette méthode est appelée par le système de contrôle
     * Le joueur est limitée dans la cadence de tir
     */
    public void shoot() {
        Boolean canShoot = getGameTimer().getNow() - last_shot.doubleValue() >= DELAY_BETWEEN_SHOOT.toSeconds();
        int decalage = 0;

        if (!canShoot)
            return;

        if (side_shoot == 0) {
            side_shoot = 1;
            decalage = 22;
        } else {
            side_shoot = 0;
            decalage = -18;
        }
        Entity bullet = spawn("bullet", entity.getX() + (entity.getWidth() / 2) + decalage, entity.getY() - 20);
        bullet.getComponent(BulletComponent.class).initialize();
        last_shot = getGameTimer().getNow();

        shootingRecoil();
    }

    /**
     * Simule le recul du vaisseau lors du tir
     */
    private void shootingRecoil() {
        entity.translateY(10);
        runOnce(() -> entity.translateY(-10), Duration.seconds(0.1));
    }
}
