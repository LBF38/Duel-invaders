package org.enstabretagne.Game.GameModes;

import static org.enstabretagne.Game.GameModes.AlienFactory.makeAlienBlock;

import org.enstabretagne.Utils.GameModeTypes;

public class ClassicGameMode extends TwoPlayerGameMode {

    @Override
    public void initGameMode() {
        System.out.println("Init Classic Game Mode");
        super.initTwoPlayerGameMode();
        System.out.println("Init Two Player Game Mode");
        makeAlienBlock();
        System.out.println("Init Alien Block");
        System.out.println("Player1 initialized ? " + (player1 != null));
        System.out.println("Game infos : \nPlayer1 :" + playerComponent1.getId() + "\nPlayer2 :" + playerComponent2.getId());
    }

    @Override
    public GameModeTypes getGameModeType() {
        return GameModeTypes.CLASSIQUE;
    }

}
