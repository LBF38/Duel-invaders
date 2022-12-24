package org.enstabretagne.Core;

import org.enstabretagne.Core.Constant.Direction;

public class Shot extends Element {
    public Shot(Integer x, Integer y, Direction direction) {
        super(x, y, direction);
    }

    @Override
    public String toString() {
        return "Tir [x=" + getX() + ", y=" + getY() + ", direction=" + getDirection() + "]";
    }

    public void move() {
        /*
         * Le missile se déplace vers l'avant d'un nombre de case déterminé
         */
        // Integer vitesseTir = 1;
        // Integer Y = getY();
        // Direction direction2 = getDirection();
        // if (direction2 == Direction.UP) {
        // Y = Y + vitesseTir;
        // } else if (direction2 == Direction.DOWN) {
        // Y = Y - vitesseTir;
        // }
        // this.setY(Y);
    }

}
