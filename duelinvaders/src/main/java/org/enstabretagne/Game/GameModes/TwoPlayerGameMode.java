package org.enstabretagne.Game.GameModes;

import static com.almasb.fxgl.dsl.FXGL.getb;
import static org.enstabretagne.UI.UI_Factory.gameOverScreen;
import static org.enstabretagne.UI.UI_Factory.winScreen;

import java.util.HashMap;
import java.util.Map;

import org.enstabretagne.Component.PlayerComponent;
import org.enstabretagne.Utils.GameVariableNames;
import org.enstabretagne.Utils.Settings;
import org.enstabretagne.Utils.Settings.Direction;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;

import javafx.scene.input.KeyCode;

public abstract class TwoPlayerGameMode extends OnePlayerGameMode {
    protected Entity player2;
    protected PlayerComponent playerComponent2;
    protected UserAction player2_shoot = new UserAction("player2_shoot") {
        @Override
        protected void onAction() {
            playerComponent2.shoot();
        }
    };
    protected UserAction player2_moveLeft = new UserAction("player2_moveLeft") {
        @Override
        protected void onAction() {
            playerComponent2.moveLeft();
        }
    };
    protected UserAction player2_moveRight = new UserAction("player2_moveRight") {
        @Override
        protected void onAction() {
            playerComponent2.moveRight();
        }
    };
    protected Map<UserAction, KeyCode> inputMap = new HashMap<UserAction, KeyCode>() {
        {
            put(player1_shoot, KeyCode.ENTER);
            put(player1_moveLeft, KeyCode.LEFT);
            put(player1_moveRight, KeyCode.RIGHT);
            put(player2_shoot, KeyCode.SPACE);
            put(player2_moveLeft, KeyCode.Q);
            put(player2_moveRight, KeyCode.D);
        }
    };

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
        try {
            inputMap.forEach((action, key) -> {
                input.addAction(action, key);
            });
        } catch (Exception e) {
            System.out.println("Error while binding keys : " + e.getMessage());
            inputMap.forEach((action, key) -> {
                input.rebind(action, key);
            });
        }
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
