package org.enstabretagne.Game.GameModes;

import org.enstabretagne.Utils.GameModeTypes;

import com.almasb.fxgl.input.Input;

public interface GameMode {
    public void initGameMode();
    public void initInput(Input input);
    public void rebindInput(Input input);
    public GameModeTypes getGameModeType();
}