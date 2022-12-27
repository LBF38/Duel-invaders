package org.enstabretagne.Component;

import org.enstabretagne.Core.Constant;
import org.enstabretagne.Core.Constant.Direction;

import com.almasb.fxgl.entity.component.Component;

public class ShotComponent extends Component {
    private Double x, y; //position
    private Direction direction; // Up or down

    public ShotComponent(Double x, Double y, Direction direction) {
        super(); //todo faire test sur x,y et direction
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    /*
    Mouvement du tir
     */
    @Override
    public void onUpdate(double tpf) {
        Double dy = tpf * Constant.SPEED_SHOOT;
        this.move(dy);
        this.entity.setX(this.x); //Je ne sais pas pourquoi on doit faire ça mais sinon le tir ne bouge pas
        this.entity.setY(this.y);
    }

    public void move(Double dy) throws IllegalArgumentException {
        if (this.direction == Direction.UP){
            this.moveUp(dy);}
        else if (this.direction == Direction.DOWN){
            this.moveDown(dy);}
    }

    public void moveUp(Double dy) {
        this.y = this.y - dy;
    }

    public void moveDown(Double dy) {
        this.y = this.y + dy;
    }


    //Getters et Setters todo->conserver ou pas? Car est utile uniquement dans les tests mais est en publiques
    public Double getX() {
        return this.x;
    }

    public Double getY() {
        return this.y;
    }

    public void setX(Double x) {
        if (x < 0 || x > Constant.BOARD_WIDTH) {
            throw new IllegalArgumentException("x is out of the board");
        }
        this.x = x;
    }

    public void setY(Double y) {
        if (y < 0 || y > Constant.BOARD_HEIGHT) {
            throw new IllegalArgumentException("y is out of the board");
        }
        this.y = y;
    }
}
