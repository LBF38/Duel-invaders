package org.enstabretagne.Game.GameModes;

import com.almasb.fxgl.input.Input;

public interface GameMode {
    public void initGameMode();
    public void initInput(Input input);
    public GameModeTypes getGameModeType();
}