package org.enstabretagne.Component;

import static com.almasb.fxgl.dsl.FXGL.getGameTimer;
import static com.almasb.fxgl.dsl.FXGL.runOnce;
import static com.almasb.fxgl.dsl.FXGL.spawn;
import static org.enstabretagne.Utils.Settings.DELAY_BETWEEN_SHOOT;
import static org.enstabretagne.Utils.Settings.GAME_WIDTH;
import static org.enstabretagne.Utils.Settings.PLAYER_HEIGHT;
import static org.enstabretagne.Utils.Settings.SPEED_SPACESHIP;

import org.enstabretagne.Utils.Settings;
import org.enstabretagne.Utils.Settings.Direction;
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
    private Settings.Direction direction = Settings.Direction.UP;
    private int id;
    private static int counter = 1;
    private int score = 0;
    private int life = 5;

    public int getId() {
        return id;
    }

    public PlayerComponent() {
        super();
        this.id = counter++;
    }

    /**
     * Setter de la direction du joueur
     * Pour la rotation de l'image du joueur, on ne prend pas en compte les
     * directions LEFT et RIGHT
     * 
     * @param direction
     */
    public void setDirection(Settings.Direction direction) {
        if (direction == Settings.Direction.LEFT || direction == Settings.Direction.RIGHT)
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
        if (this.direction == Settings.Direction.UP) {
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
        bullet.getComponent(BulletComponent.class).setPlayer(this);
        last_shot = getGameTimer().getNow();
        shootingRecoil();
    }

    /**
     * Simule le recul du vaisseau lors du tir
     */
    private void shootingRecoil() {
        if (this.direction == Settings.Direction.DOWN) {
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
     * Incrémente le score du joueur
     */
    public void incrementScore() {
        setScore(getScore() + 1);
        System.out.println("Score Player " + getId() + ": " + getScore());
    }

    /**
     * @return int
     */
    public int getScore() {
        return score;
    }

    /**
     * @param score
     */
    public void setScore(int newScore) {
        score = Math.max(0, newScore);
    }

    public void decrementScore() {
        setScore(getScore() - 1);
    }

    /**
     * Incrémente le nombre de vie du joueur
     */
    public void incrementLife() {
        setLife(getLife() + 1);
    }

    /**
     * @return int
     */
    public int getLife() {
        return life;
    }

    /**
     * @param life
     */
    public void setLife(int newLife) {
        life = Math.max(0, newLife);
    }

    /**
     * Décrémente le nombre de vie du joueur
     */
    public void decrementLife() {
        setLife(getLife() - 1);
        System.out.println("Player " + getId() + " lost a life" + " (" + getLife() + " left)");
    }
}
