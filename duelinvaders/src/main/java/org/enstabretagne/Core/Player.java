package org.enstabretagne.Core;

import org.enstabretagne.Core.Constant.Direction;

public class Player extends Element {
    // Defines a player with a canon, life, score, etc.

    /**
     * Constructor
     */
    public Player() {
        super(0.0, 0.0, Direction.UP);
    }

    public Element setDirection(Direction direction) throws IllegalArgumentException {
        if (direction != Direction.DOWN || direction != Direction.UP) {
            throw new IllegalArgumentException("Illegal direction for Player : can only be UP or DOWN.");
        }
        this.direction = direction;
        return this;
    }

    /**
     * Abstract method to redefine.
     */
    public void move(Direction direction) {
        // TODO
    }
}
