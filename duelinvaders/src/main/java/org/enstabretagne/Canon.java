package org.enstabretagne;

public class Canon extends Element{
    private int direction_vaiseau; // 1: vers la droite, -1: vers la gauche

    public int getDirection_vaiseau() {
        return direction_vaiseau;
    }
    public void setDirection_vaiseau(int direction_vaiseau) {
        //todo test si =1 ou -1
        this.direction_vaiseau = direction_vaiseau;
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
        int direction_vaiseau=getDirection_vaiseau();
        int vitesse_vaiseau =1;
        int X=getX();
        if(direction_vaiseau==1) {
            X=X+vitesse_vaiseau;
        }
        else if(direction_vaiseau==-1) {
            X=X-vitesse_vaiseau;
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
