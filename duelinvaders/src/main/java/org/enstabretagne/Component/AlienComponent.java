package org.enstabretagne.Component;

import com.almasb.fxgl.dsl.components.ExpireCleanComponent;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import org.enstabretagne.Core.Constant;
import org.enstabretagne.Core.Constant.Direction;
import org.enstabretagne.Utils.entityNames;

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

    private Direction globalDirection;
    private Double last_shot = 0.0;
    private int AlienNumber;
    private double limit_right = Constant.BOARD_WIDTH;
    private double limit_left =0.0;

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
     * Initialise l'alien

     */
    public void initialize(Constant.Direction direction) {
        this.globalDirection = direction;
        if (direction == Constant.Direction.UP){
            entity.rotateBy(180);
            this.movementDirection = Direction.RIGHT;
        }
        else if(direction == Constant.Direction.DOWN){
            this.movementDirection = Direction.LEFT;
        }


    }

    /**
     * Méthode héritée de la classe Component.
     * Elle permet de définir comment se met à jour l'alien.
     * Ainsi, le déplacement est automatique à chaque frame.
     */
    @Override
    public void onUpdate(double tpf) {
        dx = tpf * Constant.SPEED_ALIEN;
        dy = entity.getHeight();
        this.move(dx);
    }

    /**
     * Déplacement de l'alien selon la direction et les limites du jeu.
     * L'alien se déplace de dx pixels.
     * 
     * @param dx
     */
    public void move(Double dx) {
        if (this.entity.getBottomY() >= Constant.GAME_HEIGHT) {
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
        if (getEntity().getRightX() + dx <= limit_right) {
            getEntity().translateX(dx);
        } else {
            if(this.globalDirection == Constant.Direction.DOWN){
                this.entity.translateY(dy);
            }
            else if(this.globalDirection == Constant.Direction.UP){
                this.entity.translateY(-dy);
            }
            this.movementDirection = Direction.LEFT;
        }
    }

    /**
     * Déplacement de l'alien vers la gauche de dx pixels.
     * 
     * @param dx
     */
    public void moveLeft(Double dx) {
        if (getEntity().getX() - dx >= limit_left) {
            getEntity().translateX(-dx);
        if (this.entity.getX() - dx >= 0) {
            this.entity.translateX(-dx);
        } else {
            if(this.globalDirection == Constant.Direction.DOWN){
                this.entity.translateY(dy);
            }
            else if(this.globalDirection == Constant.Direction.UP){
                this.entity.translateY(-dy);
            }
            this.movementDirection = Direction.RIGHT;
        }
    }

    public void setAlienNumber(int AlienNumber) {
        this.AlienNumber = AlienNumber;
        //calcul les limites de dépalcement de l'alien
        limit_right=Constant.BOARD_WIDTH-(Constant.ALIEN_WIDTH*(Constant.ALIENS_NUMBER-AlienNumber-1));
        limit_left=0.0+(Constant.ALIEN_WIDTH*AlienNumber);
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
            double y = this.entity.getY();
            if(this.globalDirection == Constant.Direction.DOWN)
                y+=this.entity.getHeight();

            spawn(entityNames.ECLAT,x,y);
            Entity bullet = spawn(entityNames.BULLET_ALIEN, x, y);

            bullet.getComponent(BulletComponent.class).initialize(this.globalDirection);
            bullet.rotateBy(90.0);
            last_shot = getGameTimer().getNow();
        }
    }
}
