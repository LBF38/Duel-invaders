package org.enstabretagne.Game.GameModes;

import org.enstabretagne.Component.PlayerComponent;
import org.enstabretagne.Utils.GameVariableNames;
import org.enstabretagne.Utils.Settings;
import org.enstabretagne.Utils.Settings.Direction;
import static org.enstabretagne.UI.UI_Factory.*;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.Input;
import static com.almasb.fxgl.dsl.FXGL.*;

import javafx.scene.input.KeyCode;

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
    public void initInput(Input input) {
        onKey(KeyCode.ENTER, () -> playerComponent1.shoot());
        onKey(KeyCode.RIGHT, () -> playerComponent1.moveRight());
        onKey(KeyCode.LEFT, () -> playerComponent1.moveLeft());
        onKey(KeyCode.SPACE, () -> playerComponent2.shoot());
        onKey(KeyCode.D, () -> playerComponent2.moveRight());
        onKey(KeyCode.Q, () -> playerComponent2.moveLeft());
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
}
