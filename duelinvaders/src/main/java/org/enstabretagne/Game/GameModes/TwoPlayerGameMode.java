package org.enstabretagne.Game.GameModes;

import static com.almasb.fxgl.dsl.FXGL.getb;
import static org.enstabretagne.UI.UI_Factory.gameOverScreen;
import static org.enstabretagne.UI.UI_Factory.winScreen;

import org.enstabretagne.Component.PlayerComponent;
import org.enstabretagne.Utils.GameVariableNames;
import org.enstabretagne.Utils.Settings;
import org.enstabretagne.Utils.Settings.Direction;

import com.almasb.fxgl.entity.Entity;

public abstract class TwoPlayerGameMode extends OnePlayerGameMode {
    protected Entity player2;
    protected PlayerComponent playerComponent2;

    @Override
    public GameModeTypes getGameModeType() {
        return GameModeTypes.DUO;
    }

    public void initTwoPlayerGameMode() {
        super.initOnePlayerGameMode();
        player2 = initPlayer(player2, Settings.GAME_WIDTH / 2, 0);
        playerComponent2 = initPlayerComponent(player2, Direction.DOWN);
    }

    @Override
    public void gameFinished() {
        if (getb(GameVariableNames.isGameOver)) {
            gameOverScreen(playerComponent1.getScore(), playerComponent2.getScore());
        }
        if (getb(GameVariableNames.isGameWon)) {
            winScreen(playerComponent1.getScore(), playerComponent2.getScore());
        }
    }

    @Override
    public PlayerComponent getPlayerComponent2() {
        return playerComponent2;
    }
}
