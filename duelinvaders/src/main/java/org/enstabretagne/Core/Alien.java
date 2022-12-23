package org.enstabretagne.Core;

import org.enstabretagne.Core.Constant.Direction;

public class Alien extends Element {
    public Alien(Double x, Double y, Direction direction) {
        super(x, y, direction);
    }

    public void move(Direction direction) {
        // TODO
    }

    public static void main(String[] args) {
        Alien alien = new Alien(0.0, 0.0, Direction.DOWN);
        System.out.println(alien);
        // alien.setX(-1.0);
        // alien.y = -1.0;
        // alien.setY(-1.0);
        System.out.println(alien);
    }
}
