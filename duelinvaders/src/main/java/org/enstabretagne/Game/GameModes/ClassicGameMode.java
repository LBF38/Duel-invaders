package org.enstabretagne.Game.GameModes;

import static org.enstabretagne.Game.GameModes.AlienFactory.makeAlienBlock;

public class ClassicGameMode extends TwoPlayerGameMode {

    @Override
    public void initGameMode() {
        super.initTwoPlayerGameMode();
        makeAlienBlock();
    }

    @Override
    public GameModeTypes getGameModeType() {
        return GameModeTypes.CLASSIC;
    }

}
