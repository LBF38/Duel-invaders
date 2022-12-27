package org.enstabretagne.Core;

import java.util.ArrayList;
// import java.util.Random;

public class GameLogic {
    private boolean PLAY = false;
    private final Integer NB_PLAYERS = 2;
    private final Integer NB_ALIENS = 10;
    private ArrayList<Element> gameElements = new ArrayList<Element>();

    public GameLogic() {
        for (int i = 0; i < NB_PLAYERS; i++) {
            gameElements.add(new Player());
        }
        for (int i = 0; i < NB_ALIENS; i++) {
            // Random rand = new Random();
            // gameElements.add(new Alien(rand.nextInt(), rand.nextInt(), Direction.RIGHT));
        }
    }

    public void start() {
        this.PLAY = true;
        while (PLAY) {
            for (Element element : gameElements) {
                System.out.println(element);
                element.move();
            }

            // TODO
            // todo prendre les éléments rentrant
            // Todo effectuer les actions
            // Todo afficher le plateau
            // todo test fin de jeu
        }
    }
}
