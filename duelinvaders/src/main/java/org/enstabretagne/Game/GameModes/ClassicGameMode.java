package org.enstabretagne.Game.GameModes;

import static org.enstabretagne.Game.GameModes.AlienFactory.*;

public class ClassicGameMode extends TwoPlayerGameMode {

    @Override
    public void initGameMode() {
        super.initGameMode();
        makeAlienBlock();
    }

}
