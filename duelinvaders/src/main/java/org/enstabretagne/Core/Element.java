package org.enstabretagne.Core;

public abstract class Element {
    private int x;
    private int y;
    private int direction;

    // print
    @Override
    public String toString() {
        return "Element [x=" + x + ", y=" + y + ", direction=" + direction + "]";
    }

    // Getters and setters
    public int getX() {
        return x;
    }

    public void setX(int x) {
        // Todo faire un test pour vérifier que x est dans les limites du plateau
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        // Todo faire un test pour vérifier que y est dans les limites du plateau
        this.y = y;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        // Todo faire un test pour vérifier que direction est cohérente
        this.direction = direction;
    }

    // constructeur
    public Element(int x, int y, int direction) {
        if (testDirection(direction) == false || testX(x) == false || testY(y) == false) {
            throw new Error("Erreur dans les paramètres du constructeur");
        } else {
            this.x = x;
            this.y = y;
            this.direction = direction;
        }
    }

    // méthode abstraite pour déplacer l'élément
    abstract public void deplacer();

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

    protected boolean testDirection(int direction) {
        /*
         * Teste si direction est cohérente
         */
        if (direction != 1 && direction != 2 && direction != 3 && direction != 4) {
            return false;
        } else {
            return true;
        }
    }
}