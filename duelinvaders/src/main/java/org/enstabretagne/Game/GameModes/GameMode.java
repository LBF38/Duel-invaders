package org.enstabretagne.Game.GameModes;

import org.enstabretagne.Utils.GameModeTypes;

public interface GameMode {
    public void initGameMode();
    public void initInput();
    public GameModeTypes getGameModeType();
}