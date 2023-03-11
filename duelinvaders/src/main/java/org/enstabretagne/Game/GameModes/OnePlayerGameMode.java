package org.enstabretagne.Game.GameModes;

import static com.almasb.fxgl.dsl.FXGL.getb;
import static com.almasb.fxgl.dsl.FXGL.spawn;
import static org.enstabretagne.UI.UI_Factory.gameOverScreen;
import static org.enstabretagne.UI.UI_Factory.winScreen;

import org.enstabretagne.Component.PlayerComponent;
import org.enstabretagne.Utils.GameVariableNames;
import org.enstabretagne.Utils.Settings;
import org.enstabretagne.Utils.Settings.Direction;
import org.enstabretagne.Utils.entityNames;

import com.almasb.fxgl.entity.Entity;

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
    public void gameFinished() {
        if (getb(GameVariableNames.isGameOver)) {
            gameOverScreen(Integer.toString(playerComponent1.getScore()));
        }
        if (getb(GameVariableNames.isGameWon)) {
            winScreen(Integer.toString(playerComponent1.getScore()));
        }
    }

    @Override
    public PlayerComponent getPlayerComponent1() {
        return playerComponent1;
    }

    @Override
    public PlayerComponent getPlayerComponent2() {
        return playerComponent1;
    }

}
