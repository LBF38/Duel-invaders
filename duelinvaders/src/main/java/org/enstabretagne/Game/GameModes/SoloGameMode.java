package org.enstabretagne.Game.GameModes;

import static org.enstabretagne.Game.GameModes.AlienFactory.makeAlienBlockSolo;

public class SoloGameMode extends OnePlayerGameMode {

    @Override
    public void initGameMode() {
        super.initOnePlayerGameMode();
        makeAlienBlockSolo();
    }

    @Override
    public GameModeTypes getGameModeType() {
        return GameModeTypes.SOLO;
    }

}
