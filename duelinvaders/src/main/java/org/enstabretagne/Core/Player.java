package org.enstabretagne.Core;

import org.enstabretagne.Core.Constant.Direction;

public class Player extends Element {
    // Defines a player with a canon, life, score, etc.

    /**
     * Constructor
     */
    public Player() {
        super(0, 0, 0);
    }

    /**
     * Abstract method to redefine.
     */
    public void move(Direction direction) {
        // TODO
    }
}
