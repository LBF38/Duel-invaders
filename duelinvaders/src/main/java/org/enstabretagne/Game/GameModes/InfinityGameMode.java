package org.enstabretagne.Game.GameModes;

import static org.enstabretagne.Game.GameModes.AlienFactory.makeAliensInfinitely;

public class InfinityGameMode extends TwoPlayerGameMode {

    @Override
    public void initGameMode() {
        super.initTwoPlayerGameMode();
        makeAliensInfinitely();
    }

    @Override
    public GameModeTypes getGameModeType() {
        return GameModeTypes.INFINITY;
    }

}
