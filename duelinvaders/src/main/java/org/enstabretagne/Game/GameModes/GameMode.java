package org.enstabretagne.Game.GameModes;

import com.almasb.fxgl.input.Input;

/**
 * Defines the contract for a game mode.
 * 
 * @author LBF38
 */
public interface GameMode {
    public void initGameMode();
    public void initInput(Input input);
    public GameModeTypes getGameModeType();
}