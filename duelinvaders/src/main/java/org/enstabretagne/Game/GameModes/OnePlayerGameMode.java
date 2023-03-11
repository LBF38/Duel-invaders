package org.enstabretagne.Game.GameModes;

import static com.almasb.fxgl.dsl.FXGL.getb;
import static com.almasb.fxgl.dsl.FXGL.spawn;
import static org.enstabretagne.UI.UI_Factory.gameOverScreen;
import static org.enstabretagne.UI.UI_Factory.winScreen;

import java.util.HashMap;
import java.util.Map;

import org.enstabretagne.Component.PlayerComponent;
import org.enstabretagne.Utils.GameVariableNames;
import org.enstabretagne.Utils.Settings;
import org.enstabretagne.Utils.Settings.Direction;
import org.enstabretagne.Utils.entityNames;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;

import javafx.scene.input.KeyCode;

public abstract class OnePlayerGameMode implements GameMode {
    protected Entity player1;
    protected PlayerComponent playerComponent1;
    protected UserAction player1_shoot = new UserAction("player1_shoot") {
        @Override
        protected void onAction() {
            playerComponent1.shoot();
        }
    };
    protected UserAction player1_moveLeft = new UserAction("player1_moveLeft") {
        @Override
        protected void onAction() {
            playerComponent1.moveLeft();
        }
    };
    protected UserAction player1_moveRight = new UserAction("player1_moveRight") {
        @Override
        protected void onAction() {
            playerComponent1.moveRight();
        }
    };

    protected Map<UserAction, KeyCode> inputMap = new HashMap<UserAction, KeyCode>() {
        {
            put(player1_shoot, KeyCode.ENTER);
            put(player1_moveLeft, KeyCode.LEFT);
            put(player1_moveRight, KeyCode.RIGHT);
        }
    };

    @Override
    public GameModeTypes getGameModeType() {
        return GameModeTypes.SOLO;
    }

    public void initOnePlayerGameMode() {
        player1 = initPlayer(player1, Settings.GAME_WIDTH / 2);
        player1.setY(Settings.GAME_HEIGHT - player1.getHeight());
        playerComponent1 = initPlayerComponent(player1, Direction.UP);
    }

    protected Entity initPlayer(Entity player, double positionX, double positionY) {
        player = spawn(entityNames.PLAYER);
        player.setX(positionX);
        player.setY(positionY);
        return player;
    }

    protected Entity initPlayer(Entity player, double positionX) {
        return initPlayer(player, positionX, 0);
    }

    protected PlayerComponent initPlayerComponent(Entity player, Direction direction) {
        PlayerComponent playerComponent = player.getComponent(PlayerComponent.class);
        playerComponent.setDirection(direction);
        return playerComponent;
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
            gameOverScreen(Integer.toString(playerComponent1.getScore()));
        }
        if (getb(GameVariableNames.isGameWon)) {
            winScreen(Integer.toString(playerComponent1.getScore()));
        }
    }

}
