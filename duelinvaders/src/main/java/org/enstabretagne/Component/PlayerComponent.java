package org.enstabretagne.Component;

import static org.enstabretagne.Core.Constant.*;

import org.enstabretagne.Core.Constant;
import org.enstabretagne.Utils.entityNames;

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
    private Constant.Direction direction = Constant.Direction.UP;

    /**
     * Setter de la direction du joueur
     */
    public void setDirection(Constant.Direction direction) {
        double angle;
        if (this.direction == direction) {
            return;
        }
        else{ //on passe de DOWN à UP ou l'inverse -> ne prend pas en compte les autres cas (LEFT à DOWN par exemple)
            angle = 180;
        }
        entity.rotateBy(angle);
        this.direction = direction;

    }


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
        else {
            double x=0.0;
            double y=0.0;
            if(this.direction == Constant.Direction.UP) {
                if (side_shoot == 0) {
                    side_shoot = 1;
                    decalage = 22;
                } else {
                    side_shoot = 0;
                    decalage = -18;
                }
                x = entity.getX() + (entity.getWidth() / 2) + decalage;
                y = entity.getY() - 20;
            }
            else if(this.direction == Direction.DOWN){
                if (side_shoot == 0) {
                    side_shoot = 1;
                    decalage = 18;
                } else {
                    side_shoot = 0;
                    decalage = -20;
                }
                x = entity.getX() + (entity.getWidth() / 2) +decalage;
                y = entity.getY()  + PLAYER_HEIGHT + 20;
            }

            //sauvegarde le lieu du tir pour l'apparition de la fumée
            var FinalX =x;
            var FinalY =y;

            //création de la balle
            Entity bullet = spawn(entityNames.BULLET, x, y);
            bullet.getComponent(BulletComponent.class).initialize(this.direction);
            last_shot = getGameTimer().getNow();
            shootingRecoil();

            //création de la fumée
            Entity shooting_start = spawn(entityNames.SHOOTING_START, FinalX, FinalY);
            shooting_start.getComponent(ShootingStartComponent.class).initialize(this.direction);

            //création de la fumée
            runOnce(() -> {
                Entity shooting_smoke = spawn("shooting_smoke",FinalX,FinalY);
                shooting_smoke.getComponent(ShootingSmokeComponent.class).initialize(this.direction);
            }, Duration.seconds(0.2));
        }
    }

    /**
     * Simule le recul du vaisseau lors du tir
     */
    private void shootingRecoil() {
        if (this.direction == Constant.Direction.DOWN) {
            this.entity.translateY(-10);
            runOnce(() -> this.entity.translateY(10), Duration.seconds(0.1));
        }
        else if (this.direction == Direction.UP) {
            this.entity.translateY(10);
            runOnce(() -> this.entity.translateY(-10), Duration.seconds(0.1));
        }
    }
}
