package org.enstabretagne.Core;

import org.enstabretagne.Core.Constant.Direction;

public abstract class Element {
    protected Double x;
    protected Double y;
    protected Direction direction;

    @Override
    public String toString() {
        return "Element [x=" + x + ", y=" + y + ", direction=" + direction + "]";
    }

    // Getters and setters
    public Double getX() {
        return x;
    }

    public Element x(Double x) throws IllegalArgumentException {
        if (x < 0 || x > Constant.BOARD_WIDTH) {
            throw new IllegalArgumentException("x is out of the board");
        }
        this.x = x;
        return this;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) throws IllegalArgumentException {
        if (y < 0 || y > Constant.BOARD_HEIGHT) {
            throw new IllegalArgumentException("y is out of the board");
        }
        this.y = y;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Element(Double x, Double y, Direction direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    public abstract void move(Direction direction);

    protected boolean testX(int x) {
        /*
         * Teste si x est dans les limites du plateau
         */
        if (x < 0 || x > Constant.BOARD_WIDTH) {
            return false;
        } else {
            return true;
        }
    }

    protected boolean testY(int y) {
        /*
         * Teste si y est dans les limites du plateau
         */
        if (y < 0 || y > Constant.BOARD_HEIGHT) {
            return false;
        } else {
            return true;
        }
    }
}