package org.enstabretagne;

import java.time.LocalDate;

public class Canon extends Element{
    private int directionVaisseau; // 1: vers la droite, -1: vers la gauche
    private LocalDate lastShoot = LocalDate.now();

    public int getDirectionVaisseau() {
        return directionVaisseau;
    }
    public void setDirectionVaisseau(int directionVaisseau) {
        if(directionVaisseau!=1 && directionVaisseau!=-1){
            throw new Error("la direction du vaisseau doit être 1 ou -1");
        }
        else{
            this.directionVaisseau = directionVaisseau;
        }
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
        int vitesseVaisseau =Constant.VITESSE_VAISSEAU;
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
        int delayBetweenShoot = Constant.DElAY_BETWEEN_SHOOT;
        LocalDate now = LocalDate.now();
        if(true) { //todo vérifier le delay (now - this.lastShoot) > delayBetweenShoot
            Tir tir = new Tir(getX(), getY(), getDirection());
            this.lastShoot = now;
            return tir;
        }
        else{
            return null;
        }


    }
}