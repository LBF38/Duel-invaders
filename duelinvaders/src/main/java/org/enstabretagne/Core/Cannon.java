package org.enstabretagne.Core;

// import java.util.Date;

import org.enstabretagne.Core.Constant.Direction;

public class Cannon extends Element {
    private int directionVaisseau; // 1: vers la droite, -1: vers la gauche
    // private Date lastShoot = new Date();

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
    public Cannon() {
        super(Constant.random.nextInt(), Constant.random.nextInt(), Direction.UP);
    }

    // Print
    @Override
    public String toString() {
        return "Canon [x=" + getX() + ", y=" + getY() + ", direction=" + getDirection() + "]";
    }

    public void move() {
        /*
         * Le vaisseau se déplace vers la gauche ou la droite (direction_vaiseau) d'un
         * nombre de case déterminé
         */
        // int directionVaisseau = getDirectionVaisseau();
        // int vitesseVaisseau = Constant.SPEED_SPACESHIP;
        // int X = getX();
        // if (directionVaisseau == 1) {
        // X = X + vitesseVaisseau;
        // } else if (directionVaisseau == -1) {
        // X = X - vitesseVaisseau;
        // }

        // if (super.testX(X) == true) {
        // setX(X);
        // }
    }

    // public Shot tirer() {
    //     /*
    //      * Le vaisseau tire un missile
    //      * Le missile part de la position du vaiseau dans la direction du vaisseau
    //      */
    //     int delayBetweenShoot = Constant.DELAY_BETWEEN_SHOOT;
    //     Date now = new Date();
    //     if (now.compareTo(this.lastShoot) > delayBetweenShoot) { // todo vérifier le delay (now - this.lastShoot) >
    //                                                              // delayBetweenShoot
    //         Shot tir = new Shot(getX(), getY(), getDirection());
    //         // tir.move(Constant.Direction.UP); // TODO: change for direction of the canon
    //         this.lastShoot = now;
    //         return tir;
    //     }
    //     return null;
    // }
}