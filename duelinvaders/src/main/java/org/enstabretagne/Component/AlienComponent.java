package org.enstabretagne.Component;

import org.enstabretagne.Core.Constant;
import org.enstabretagne.Core.Constant.Direction;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;

import javafx.geometry.Point2D;

import static com.almasb.fxgl.dsl.FXGL.*;

/**
 * Cette classe représente le comportement des aliens
 * Elle permet de définir les données associées à l'alien et son comportement
 * 
 * @author @LBF38
 * @since 0.1.0
 */
public class AlienComponent extends Component {
    private Double dx, dy;
    protected Direction movementDirection;
    private Double last_shot = 0.0;

    /**
     * Constructeur de la classe AlienComponent
     * La direction permet de définir l'orientation de l'alien.
     * Elle est définie comme étant la direction dans laquelle l'alien va se
     * déplacer.
     * 
     * @param direction {@code} Direction {@code} La direction dans laquelle l'alien
     *                  va se déplacer
     */
    public AlienComponent(Direction direction) {
        super();
        if (direction == Direction.DOWN)
            this.movementDirection = Direction.RIGHT;
        else
            this.movementDirection = Direction.DOWN;
    }

    /**
     * Constructeur de la classe AlienComponent
     * La direction par défaut est DOWN
     */
    public AlienComponent() {
        this(Direction.DOWN);
    }

    /**
     * Méthode héritée de la classe Component.
     * Elle permet de définir comment se met à jour l'alien.
     * Ainsi, le déplacement est automatique à chaque frame.
     */
    @Override
    public void onUpdate(double tpf) {
        dx = tpf * Constant.SPEED_ALIEN;
        dy = 0.5 * Constant.SPEED_ALIEN;
        this.move(dx);
    }

    /**
     * Déplacement de l'alien selon la direction et les limites du jeu.
     * L'alien se déplace de dx pixels.
     * 
     * @param dx
     */
    public void move(Double dx) {
        if (this.entity.getBottomY() >= Constant.BOARD_HEIGHT) {
            // TODO: to remove if unnecessary
            System.out.println("Game Over");
            return;
        }

        if (this.movementDirection == Direction.RIGHT)
            moveRight(dx);
        else if (this.movementDirection == Direction.LEFT)
            moveLeft(dx);
    }

    /**
     * Déplacement de l'alien vers la droite de dx pixels.
     * 
     * @param dx
     */
    public void moveRight(Double dx) {
        if (this.entity.getRightX() + dx <= Constant.BOARD_WIDTH) {
            this.entity.translateX(dx);
        } else {
            this.entity.translateY(dy);
            this.movementDirection = Direction.LEFT;
        }
    }

    /**
     * Déplacement de l'alien vers la gauche de dx pixels.
     * 
     * @param dx
     */
    public void moveLeft(Double dx) {
        if (this.entity.getX() - dx >= 0) {
            this.entity.translateX(-dx);
        } else {
            this.entity.translateY(dy);
            this.movementDirection = Direction.RIGHT;
        }
    }

    /**
     * Tir aléatoire de l'alien.
     * 
     * @param chance
     */
    public void randomShoot(double chance) {
        if (FXGLMath.randomBoolean(chance))
            shoot();
    }

    /**
     * Tir de l'alien.
     * L'alien tire un projectile à une cadence définie dans {@code}Constant{@code}.
     */
    public void shoot() {
        Boolean canShoot = getGameTimer().getNow() - last_shot.doubleValue() >= Constant.RATE_ALIEN_SHOOT.doubleValue();
        if (canShoot || last_shot == null) {
            double x = this.entity.getX() + this.entity.getWidth() / 2;
            double y = this.entity.getY() + this.entity.getHeight();
            Entity bullet = spawn("alienBullet", x, y);
            BulletComponent bulletComponent = bullet.getComponent(BulletComponent.class);
            // TODO: rendre le shotDirection dépendant de la direction de l'alien
            bulletComponent.setDirection(new Point2D(0, 1));
            bulletComponent.initialize();
            last_shot = getGameTimer().getNow();
        }
    }
}
