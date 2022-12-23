package org.enstabretagne.Core;

import org.enstabretagne.Core.Constant.Direction;

public class Alien extends Element {
    private Direction movementDirection;

    public Alien(Integer x, Integer y, Direction direction) {
        super(x, y, direction);
    }

    public void move() throws IllegalArgumentException {
        if (this.getX() == Constant.BOARD_WIDTH) {
            this.setY(this.getY() + 1);
            this.movementDirection = Direction.LEFT;
        } else if (this.getX() == 0) {
            this.setY(this.getY() + 1);
            this.movementDirection = Direction.RIGHT;
        }

        if (movementDirection == Direction.RIGHT)
            this.moveRight();
        else if (movementDirection == Direction.LEFT)
            this.moveLeft();
    }

    public void moveRight() {
        this.setX(this.getX() + 1);
    }

    public void moveLeft() {
        this.setX(this.getX() - 1);
    }

    /**
     * A supprimer. Alien sandbox.
     * 
     * @param args
     */
    public static void main(String[] args) {
        Alien alien = new Alien(0, 0, Direction.DOWN);
        System.out.println(alien);
        // alien.setX(-1);
        // alien.y = -1;
        // alien.setY(-1);
        System.out.println(alien);
    }
}
