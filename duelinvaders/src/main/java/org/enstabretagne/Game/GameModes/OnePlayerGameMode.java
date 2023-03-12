package org.enstabretagne.Game.GameModes;

import static com.almasb.fxgl.dsl.FXGL.getb;
import static com.almasb.fxgl.dsl.FXGL.*;
import static org.enstabretagne.UI.UI_Factory.gameOverScreen;
import static org.enstabretagne.UI.UI_Factory.*;

import org.enstabretagne.Component.PlayerComponent;
import org.enstabretagne.Utils.GameVariableNames;
import org.enstabretagne.Utils.Settings;
import org.enstabretagne.Utils.Settings.Direction;
import org.enstabretagne.Utils.entityNames;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.Entity;

public abstract class OnePlayerGameMode implements GameMode {
    protected Entity player1;
    protected PlayerComponent playerComponent1;
    protected long last_ambient_sound = System.currentTimeMillis();
    protected int delay_ambient_sound = FXGLMath.random(Settings.AMBIENT_SOUND_DELAY_MIN,
            Settings.AMBIENT_SOUND_DELAY_MAX);

    @Override
    public GameModeTypes getGameModeType() {
        return GameModeTypes.SOLO;
    }

    public void initOnePlayerGameMode() {
        player1 = initPlayer(player1, Settings.GAME_WIDTH / 2);
        player1.setY(Settings.GAME_HEIGHT - player1.getHeight());
        playerComponent1 = initPlayerComponent(player1, Direction.UP);
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

    @Override
    public void onUpdate(double tpf) {
        if (getb(GameVariableNames.isGameOver) || getb(GameVariableNames.isGameWon)) {
            gameFinished();
        }

        if ((System.currentTimeMillis() - last_ambient_sound) > delay_ambient_sound) {
            ambientSound();
            last_ambient_sound = System.currentTimeMillis();
            delay_ambient_sound = FXGLMath.random(Settings.AMBIENT_SOUND_DELAY_MIN, Settings.AMBIENT_SOUND_DELAY_MAX);
        }

        showPlayersLivesAndScores(getGameWorld(), getGameScene());

        AlienFactory.aliensRandomlyShoot();

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

}
