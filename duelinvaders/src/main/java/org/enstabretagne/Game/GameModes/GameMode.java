package org.enstabretagne.Game.GameModes;

import org.enstabretagne.Component.PlayerComponent;

/**
 * Defines the contract for a game mode.
 * 
 * @author LBF38
 */
public interface GameMode {
    public PlayerComponent getPlayerComponent1();

    public PlayerComponent getPlayerComponent2();

    public void initGameMode();

    public GameModeTypes getGameModeType();

    public void onUpdate(double tpf);

    public void gameFinished();
}