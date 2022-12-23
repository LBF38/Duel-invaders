package org.enstabretagne.Core;

import org.enstabretagne.Core.Constant.Direction;

public abstract class Element {
    private Integer x;
    private Integer y;
    private Direction direction;

    @Override
    public String toString() {
        return "Element [x=" + x + ", y=" + y + ", direction=" + direction + "]";
    }

    // Getters and setters
    public Integer getX() {
        return x;
    }

    public Element setX(Integer x) throws IllegalArgumentException {
        if (x < 0 || x > Constant.BOARD_WIDTH) {
            throw new IllegalArgumentException("x is out of the board");
        }
        this.x = x;
        return this;
    }

    public Integer getY() {
        return y;
    }

    public Element setY(Integer y) throws IllegalArgumentException {
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

    public Element(Integer x, Integer y, Direction direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    public abstract void move();
}