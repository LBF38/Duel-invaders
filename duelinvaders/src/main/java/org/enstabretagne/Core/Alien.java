package org.enstabretagne.Core;

import org.enstabretagne.Core.Constant.Direction;

public class Alien extends Element {
    public Alien(Double x, Double y, Direction direction) {
        super(x, y, direction);
    }

    public Element setDirection(Direction direction) throws IllegalArgumentException {
        if (direction != Direction.DOWN || direction != Direction.UP)
            throw new IllegalArgumentException("Illegal Alien direction : can only be UP or DOWN");
        this.direction = direction;
        return this;
    }

    public void move(Direction direction) {
        // TODO
    }
}
