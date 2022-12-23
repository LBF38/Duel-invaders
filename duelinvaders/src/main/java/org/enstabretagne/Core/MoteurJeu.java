package org.enstabretagne.Core;

import java.util.ArrayList;

public class MoteurJeu {
    private boolean play = false;
    private final Integer NB_PLAYERS = 2;
    private final Integer NB_ALIENS = 10;
    private ArrayList<Element> gameElements = new ArrayList<Element>();

    public MoteurJeu() {
        for (int i = 0; i < NB_PLAYERS; i++) {
            gameElements.add(new Player());
        }
        for (int i = 0; i < NB_ALIENS; i++) {
            gameElements.add(new Alien());
        }
    }

    public void start() {
        this.play = true;
        while (play) {

            // TODO
            // todo prendre les éléments rentrant
            // Todo effectuer les actions
            // Todo afficher le plateau
            // todo test fin de jeu
        }
    }
}
