package org.enstabretagne;

public class Canon extends Element{
    private int directionVaisseau; // 1: vers la droite, -1: vers la gauche

    public int getDirectionVaisseau() {
        return directionVaisseau;
    }
    public void setDirectionVaisseau(int directionVaisseau) {
        //todo test si =1 ou -1
        this.directionVaisseau = directionVaisseau;
    }

    //constructeur
    public Canon(int x, int y, int direction) {
        super(x, y, direction);
    }

    //Print
    @Override
    public String toString() {
        return "Canon [x=" + getX() + ", y=" + getY() + ", direction=" + getDirection() + "]";
    }


    public void deplacer() {

        /*
        Le vaisseau se déplace vers la gauche ou la droite (direction_vaiseau) d'un nombre de case déterminé
         */
        int directionVaisseau=getDirectionVaisseau();
        int vitesseVaisseau =1;
        int X=getX();
        if(directionVaisseau==1) {
            X=X+vitesseVaisseau;
        }
        else if(directionVaisseau==-1) {
            X=X-vitesseVaisseau;
        }
        super.testX(X);
        setX(X);
    }

    public Tir tirer() {
        /*
            * Le vaisseau tire un missile
            * Le missile part de la position du vaiseau dans la direction du vaisseau
        */

        Tir tir = new Tir(getX(), getY(), getDirection());
        return tir;
    }
}
