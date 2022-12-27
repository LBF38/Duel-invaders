package org.enstabretagne.Component;

import org.enstabretagne.Core.Constant;
import org.enstabretagne.Core.Constant.Direction;

import com.almasb.fxgl.entity.component.Component;

public class ShotComponent extends Component {
    private Double dy; //déplacement
    private Direction direction; // Up or down

    public ShotComponent(Double x, Double y, Direction direction) {
        super(); //todo faire test sur x,y et direction
        this.direction = direction;
    }




    /*
    Mouvement du tir
     */
    @Override
    public void onUpdate(double tpf) {
        Double dy = tpf * Constant.SPEED_SHOOT;
        this.move(dy);
    }

    public void move(Double dy) throws IllegalArgumentException {
        if (this.direction == Direction.UP){
            this.moveUp(dy);}
        else if (this.direction == Direction.DOWN){
            this.moveDown(dy);}
    }

    public void moveUp(Double dy) {
        getEntity().translateY(-dy);
    }

    public void moveDown(Double dy) {
        getEntity().translateY(dy);
    }

}
