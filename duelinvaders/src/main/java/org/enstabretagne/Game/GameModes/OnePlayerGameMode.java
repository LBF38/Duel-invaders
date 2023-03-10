package org.enstabretagne.Game.GameModes;

import org.enstabretagne.Component.PlayerComponent;
import org.enstabretagne.Utils.GameModeTypes;
import org.enstabretagne.Utils.Settings;
import org.enstabretagne.Utils.entityNames;
import org.enstabretagne.Utils.Settings.Direction;

import static com.almasb.fxgl.dsl.FXGL.*;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.Input;

import javafx.scene.input.KeyCode;

public abstract class OnePlayerGameMode implements GameMode {
    protected Entity player1;
    protected PlayerComponent playerComponent1;

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
        if (playerComponent1 == null) {
            System.out.println("No PlayerComponent");
            return;
        } else {
            System.out.println("PlayerComponent OK");
        }
        onKey(KeyCode.ENTER, () -> playerComponent1.shoot());
        onKey(KeyCode.RIGHT, () -> playerComponent1.moveRight());
        onKey(KeyCode.LEFT, () -> playerComponent1.moveLeft());
        onKey(KeyCode.SPACE, () -> playerComponent1.shoot());
        onKey(KeyCode.D, () -> playerComponent1.moveRight());
        onKey(KeyCode.Q, () -> playerComponent1.moveLeft());
    }

}
