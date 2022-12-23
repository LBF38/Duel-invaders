package org.enstabretagne.Core;

import org.enstabretagne.Core.Constant.Direction;

public abstract class Element {
    private Double x;
    private Double y;
    private Direction direction;

    @Override
    public String toString() {
        return "Element [x=" + x + ", y=" + y + ", direction=" + direction + "]";
    }

    // Getters and setters
    public Double getX() {
        return x;
    }

    public Element setX(Double x) throws IllegalArgumentException {
        if (x < 0 || x > Constant.BOARD_WIDTH) {
            throw new IllegalArgumentException("x is out of the board");
        }
        this.x = x;
        return this;
    }

    public Double getY() {
        return y;
    }

    public Element setY(Double y) throws IllegalArgumentException {
        if (y < 0 || y > Constant.BOARD_HEIGHT) {
            throw new IllegalArgumentException("y is out of the board");
        }
        this.y = y;
        return this;
    }

    public Direction getDirection() {
        return direction;
    }

    public Element setDirection(Direction direction) throws IllegalArgumentException {
        if (direction != Direction.DOWN || direction != Direction.UP) {
            throw new IllegalArgumentException("Illegal direction : The Cannon can only be UP or DOWN");
        }
        this.direction = direction;
        return this;
    }

    public Element(Double x, Double y, Direction direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    public abstract void move(Direction direction);
}