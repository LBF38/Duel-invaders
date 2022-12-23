package org.enstabretagne.Core;

import org.enstabretagne.Core.Constant.Direction;

public class Shot extends Element {
    public Shot(Double x, Double y, Direction direction) {
        super(x, y, direction);
    }

    @Override
    public String toString() {
        return "Tir [x=" + getX() + ", y=" + getY() + ", direction=" + getDirection() + "]";
    }

    public Element setDirection(Direction direction) {
        if (direction != Direction.DOWN || direction != Direction.UP) {
            throw new IllegalArgumentException(
                    "Illegal direction for the Shot element. Please provide a correct direction : UP or DOWN.");
        }
        this.direction = direction;
        return this;
    }

    public void move(Direction direction) {
        /*
         * Le missile se déplace vers l'avant d'un nombre de case déterminé
         */
        Double vitesseTir = 1.0;
        Double Y = getY();
        Direction direction2 = getDirection();
        if (direction2 == Direction.UP) {
            Y = Y + vitesseTir;
        } else if (direction2 == Direction.DOWN) {
            Y = Y - vitesseTir;
        }
        this.y = Y;
    }

}
