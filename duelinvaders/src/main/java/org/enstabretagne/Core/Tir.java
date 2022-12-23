package org.enstabretagne.Core;

import org.enstabretagne.Core.Constant.Direction;

public class Tir extends Element {
    public Tir(int x, int y, int direction) {
        super(x, y, direction);
    }

    @Override
    public String toString() {
        return "Tir [x=" + getX() + ", y=" + getY() + ", direction=" + getDirection() + "]";
    }

    public void move(Direction direction) {
        /*
        Le missile se déplace vers l'avant d'un nombre de case déterminé
        */
        int vitesseTir = 1;
        int Y = getY();
        int direction2 = getDirection();
        if (direction2 > 0) {
            Y = Y + vitesseTir;
        } else if (direction2 < 0) {
            Y = Y - vitesseTir;
        }
        if (super.testY(Y) == true) {
            setY(Y);
        }
    }
    
}
