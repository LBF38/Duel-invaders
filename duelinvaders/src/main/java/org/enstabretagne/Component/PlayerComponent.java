package org.enstabretagne.Component;

import static com.almasb.fxgl.dsl.FXGL.getGameTimer;
import static com.almasb.fxgl.dsl.FXGL.geti;
import static com.almasb.fxgl.dsl.FXGL.runOnce;
import static com.almasb.fxgl.dsl.FXGL.set;
import static com.almasb.fxgl.dsl.FXGL.spawn;
import static org.enstabretagne.Core.Constant.DELAY_BETWEEN_SHOOT;
import static org.enstabretagne.Core.Constant.GAME_WIDTH;
import static org.enstabretagne.Core.Constant.PLAYER_HEIGHT;
import static org.enstabretagne.Core.Constant.SPEED_SPACESHIP;

import org.enstabretagne.Core.Constant;
import org.enstabretagne.Core.Constant.Direction;
import org.enstabretagne.Core.GameVariableNames;
import org.enstabretagne.Utils.entityNames;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;

import javafx.geometry.Point2D;
import javafx.util.Duration;

/**
 * Définition du composant pour le joueur
 * 
 * @author MathieuDFS, jufch, LBF38
 * @since 0.1.0
 */
public class PlayerComponent extends Component {
    private Double dx;
    private Double last_shot = 0.0;
    private Direction side_shoot = Direction.LEFT;
    private Constant.Direction direction = Constant.Direction.UP;
    private static int id;

    public int getId() {
        return id;
    }

    public PlayerComponent() {
        id += 1;
    }

    /**
     * Setter de la direction du joueur
     * Pour la rotation de l'image du joueur, on ne prend pas en compte les
     * directions LEFT et RIGHT
     * 
     * @param direction
     */
    public void setDirection(Constant.Direction direction) {
        if (direction == Constant.Direction.LEFT || direction == Constant.Direction.RIGHT)
            return;
        if (this.direction == direction)
            return;
        entity.rotateBy(180);
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

        if (!canShoot)
            return;
        Point2D bulletPosition = calculateShootPosition();
        createBullet(bulletPosition);
        createSmoke(bulletPosition);
    }

    private Point2D calculateShootPosition() {
        double x = entity.getX() + (entity.getWidth() / 2);
        double y = entity.getY();
        int decalage = 20;
        int shift_x;
        int shift_y;
        if (this.direction == Constant.Direction.UP) {
            shift_y = -decalage;
        } else {
            shift_y = PLAYER_HEIGHT.intValue() + decalage;
        }

        if (this.side_shoot == Direction.LEFT) {
            this.side_shoot = Direction.RIGHT;
            shift_x = -decalage;
        } else {
            this.side_shoot = Direction.LEFT;
            shift_x = decalage;
        }

        x += shift_x;
        y += shift_y;
        return new Point2D(x, y);
    }

    private void createBullet(Point2D position) {
        Entity bullet = spawn(entityNames.BULLET, position);
        bullet.getComponent(BulletComponent.class).initialize(this.direction);
        last_shot = getGameTimer().getNow();
        shootingRecoil();
    }

    /**
     * Simule le recul du vaisseau lors du tir
     */
    private void shootingRecoil() {
        if (this.direction == Constant.Direction.DOWN) {
            this.entity.translateY(-10);
            runOnce(() -> this.entity.translateY(10), Duration.seconds(0.1));
        } else if (this.direction == Direction.UP) {
            this.entity.translateY(10);
            runOnce(() -> this.entity.translateY(-10), Duration.seconds(0.1));
        }
    }

    private void createSmoke(Point2D position) {
        Entity shooting_start = spawn(entityNames.SHOOTING_START, position);
        shooting_start.getComponent(ShootingStartComponent.class).initialize(this.direction);
        runOnce(() -> {
            Entity shooting_smoke = spawn("shooting_smoke", position);
            shooting_smoke.getComponent(ShootingSmokeComponent.class).initialize(this.direction);
        }, Duration.seconds(0.2));
    }

    /**
     * Score du joueur
     */
    public void incrementScore() {
        if (id == 1)
            set(GameVariableNames.PLAYER1_SCORE, geti(GameVariableNames.PLAYER1_SCORE) + 1);
        else if (id == 2)
            set(GameVariableNames.PLAYER2_SCORE, geti(GameVariableNames.PLAYER2_SCORE) + 1);
    }

    public void initializeScore() {
        if (id == 1)
            set(GameVariableNames.PLAYER1_SCORE, 0);
        else if (id == 2)
            set(GameVariableNames.PLAYER2_SCORE, 0);
    }

    public int getScore() {
        if (id == 1)
            return geti(GameVariableNames.PLAYER1_SCORE);
        else if (id == 2)
            return geti(GameVariableNames.PLAYER2_SCORE);
        return -1;
    }

    public void setScore(int score) {
        if (id == 1)
            set(GameVariableNames.PLAYER1_SCORE, score);
        else if (id == 2)
            set(GameVariableNames.PLAYER2_SCORE, score);
    }

    public void decrementScore() {
        if (id == 1)
            set(GameVariableNames.PLAYER1_SCORE, geti(GameVariableNames.PLAYER1_SCORE) - 1);
        else if (id == 2)
            set(GameVariableNames.PLAYER2_SCORE, geti(GameVariableNames.PLAYER2_SCORE) - 1);
    }

    /**
     * Vies du joueur
     */
    public void incrementLife() {
        if (id == 1)
            set(GameVariableNames.PLAYER1_LIFE, geti(GameVariableNames.PLAYER1_LIFE) + 1);
        else if (id == 2)
            set(GameVariableNames.PLAYER2_LIFE, geti(GameVariableNames.PLAYER2_LIFE) + 1);
    }

    public void initializeLife() {
        if (id == 1)
            set(GameVariableNames.PLAYER1_LIFE, 3);
        else if (id == 2)
            set(GameVariableNames.PLAYER2_LIFE, 3);
    }

    public int getLife() {
        if (id == 1)
            return geti(GameVariableNames.PLAYER1_LIFE);
        else if (id == 2)
            return geti(GameVariableNames.PLAYER2_LIFE);
        return -1;
    }

    public void setLife(int life) {
        if (id == 1)
            set(GameVariableNames.PLAYER1_LIFE, life);
        else if (id == 2)
            set(GameVariableNames.PLAYER2_LIFE, life);
    }

    public void decrementLife() {
        if (id == 1)
            set(GameVariableNames.PLAYER1_LIFE, geti(GameVariableNames.PLAYER1_LIFE) - 1);
        else if (id == 2)
            set(GameVariableNames.PLAYER2_LIFE, geti(GameVariableNames.PLAYER2_LIFE) - 1);
    }
}
