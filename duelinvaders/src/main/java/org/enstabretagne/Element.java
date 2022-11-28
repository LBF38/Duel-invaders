package org.enstabretagne;

public class Element {
    private int x;
    private int y;
    private int direction;

    //print
    @Override
    public String toString() {
        return "Element [x=" + x + ", y=" + y + ", direction=" + direction + "]";
    }

    // Getters and setters
    public int getX() {
        return x;
    }
    public void setX(int x) {
        //faire un test pour vérifier que x est dans les limites du plateau
        this.x = x;
    }
    public int getY() {
        return y;
    }
    public void setY(int y) {
        //faire un test pour vérifier que y est dans les limites du plateau
        this.y = y;
    }
    public int getDirection() {
        return direction;
    }
    public void setDirection(int direction) {
        //faire un test pour vérifier que direction est cohérente
        this.direction = direction;
    }

    //constructeur
    public Element(int x, int y, int direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    //méthode pour déplacer l'élément
    public void deplacer(int x,int y, int direction) { //suppimer les set et garder que deplacement??????
        setX(x);
        setY(y);
        setDirection(direction);
    }
}
