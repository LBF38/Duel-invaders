package org.enstabretagne.Core;

import java.util.Date;

public class Canon extends Element {
    private int directionVaisseau; // 1: vers la droite, -1: vers la gauche
    private Date lastShoot = new Date();

    public int getDirectionVaisseau() {
        return directionVaisseau;
    }

    public void setDirectionVaisseau(int directionVaisseau) {
        if (directionVaisseau != 1 && directionVaisseau != -1) {
            throw new Error("la direction du vaisseau doit être 1 ou -1");
        } else {
            this.directionVaisseau = directionVaisseau;
        }
    }

    // constructeur
    public Canon(int x, int y, int direction) {
        super(x, y, direction);
    }

    // Print
    @Override
    public String toString() {
        return "Canon [x=" + getX() + ", y=" + getY() + ", direction=" + getDirection() + "]";
    }

    public void deplacer() {
        /*
         * Le vaisseau se déplace vers la gauche ou la droite (direction_vaiseau) d'un
         * nombre de case déterminé
         */
        int directionVaisseau = getDirectionVaisseau();
        int vitesseVaisseau = Constant.SPEED_SPACESHIP;
        int X = getX();
        if (directionVaisseau == 1) {
            X = X + vitesseVaisseau;
        } else if (directionVaisseau == -1) {
            X = X - vitesseVaisseau;
        }

        if (super.testX(X) == true) {
            setX(X);
        }
    }

    public Tir tirer() {
        /*
         * Le vaisseau tire un missile
         * Le missile part de la position du vaiseau dans la direction du vaisseau
         */
        int delayBetweenShoot = Constant.DELAY_BETWEEN_SHOOT;
        Date now = new Date();
        if (now.compareTo(this.lastShoot) > delayBetweenShoot) { // todo vérifier le delay (now - this.lastShoot) >
                                                                 // delayBetweenShoot
            Tir tir = new Tir(getX(), getY(), getDirection());
            tir.deplacer();
            this.lastShoot = now;
            return tir;
        }
        return null;
    }
}