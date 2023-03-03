package org.enstabretagne.Game.GameModes;

import static com.almasb.fxgl.dsl.FXGL.onKey;
import static com.almasb.fxgl.dsl.FXGL.spawn;

import org.enstabretagne.Component.AlienComponent;
import org.enstabretagne.Component.PlayerComponent;
import org.enstabretagne.Utils.GameModeTypes;
import org.enstabretagne.Utils.Settings;
import org.enstabretagne.Utils.entityNames;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.Input;

import javafx.scene.input.KeyCode;

public class ClassicGameMode implements GameMode {
    private PlayerComponent playerComponent1;
    private PlayerComponent playerComponent2;
    private Entity player1;
    private Entity player2;
    private GameModeTypes gameModeType = GameModeTypes.CLASSIQUE;

    @Override
    public void initGameMode() {
        initPlayer1();
        initPlayer2();
        makeAlienBlock();
    }

    private void initPlayer1() {
        player1 = spawn(entityNames.PLAYER);
        player1.setX(Settings.GAME_WIDTH / 2);
        player1.setY(Settings.GAME_HEIGHT - player1.getHeight());
        playerComponent1 = player1.getComponent(PlayerComponent.class);
        playerComponent1.setDirection(Settings.Direction.UP);
        playerComponent1.initializeScore();
        playerComponent1.initializeLife();
    }

    private void initPlayer2() {
        player2 = spawn(entityNames.PLAYER);
        player2.setX(Settings.GAME_WIDTH / 2);
        player2.setY(0);
        playerComponent2 = player2.getComponent(PlayerComponent.class);
        playerComponent2.setDirection(Settings.Direction.DOWN);
        playerComponent2.initializeScore();
        playerComponent2.initializeLife();
    }

    // Getters and Setters
    @Override
    public GameModeTypes getGameModeType() {
        return gameModeType;
    }

    // Methods
    private void makeAlienBlock() {
        for (int i = 0; i < 2; i++) {
            makeAlienLine(i, Settings.Direction.DOWN);
            makeAlienLine(i, Settings.Direction.UP);
        }
    }

    private void makeAlienLine(int line, Settings.Direction direction) {
        for (int i = 0; i < Settings.ALIENS_NUMBER; i++) {
            if (direction == Settings.Direction.DOWN) {
                Entity alien = spawn(entityNames.ALIEN, i * Settings.ALIEN_WIDTH,
                        Settings.GAME_HEIGHT / 2 + (line - 1) * Settings.ALIEN_HEIGHT);
                alien.getComponent(AlienComponent.class).initialize(direction);
                alien.getComponent(AlienComponent.class).setAlienNumber(i);
            } else {
                Entity alien = spawn(entityNames.ALIEN, i * Settings.ALIEN_WIDTH,
                        Settings.GAME_HEIGHT / 2 + (line - 2) * Settings.ALIEN_HEIGHT);
                alien.getComponent(AlienComponent.class).initialize(direction);
                alien.getComponent(AlienComponent.class).setAlienNumber(i);
            }
        }
    }

    @Override
    public void initInput(Input input) {
        if (playerComponent1 == null || playerComponent2 == null) {
            System.out.println("No PlayerComponent");
            return;
        } else {
            System.out.println("PlayerComponent OK");
        }
        onKey(KeyCode.ENTER, () -> playerComponent1.shoot());
        onKey(KeyCode.RIGHT, () -> playerComponent1.moveRight());
        onKey(KeyCode.LEFT, () -> playerComponent1.moveLeft());
        onKey(KeyCode.SPACE, () -> playerComponent2.shoot());
        onKey(KeyCode.D, () -> playerComponent2.moveRight());
        onKey(KeyCode.Q, () -> playerComponent2.moveLeft());
    }
}
