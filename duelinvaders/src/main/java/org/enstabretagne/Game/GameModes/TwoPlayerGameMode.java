package org.enstabretagne.Game.GameModes;

import static com.almasb.fxgl.dsl.FXGL.getb;
import static com.almasb.fxgl.dsl.FXGL.onKey;
import static org.enstabretagne.UI.UI_Factory.gameOverScreen;
import static org.enstabretagne.UI.UI_Factory.winScreen;

import java.util.Map;

import org.enstabretagne.Component.PlayerComponent;
import org.enstabretagne.Utils.GameVariableNames;
import org.enstabretagne.Utils.Settings;
import org.enstabretagne.Utils.Settings.Direction;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.Input;

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
        keyBindings = Map.of(KeyCode.ENTER, () -> playerComponent1.shoot(), KeyCode.RIGHT,
                () -> playerComponent1.moveRight(), KeyCode.LEFT, () -> playerComponent1.moveLeft(),
                KeyCode.SPACE, () -> playerComponent2.shoot(), KeyCode.D, () -> playerComponent2.moveRight(),
                KeyCode.Q, () -> playerComponent2.moveLeft());

        try {
            input.clearAll();
            input.getAllBindings().clear();
            keyBindings.forEach((keycode, action) -> onKey(keycode, action));
        } catch (Exception e) {
            System.out.println("Error while initializing input :" + e.getMessage());
            // keyBindings.forEach((keycode, action) ->{
            // if(input.getAllBindings().containsValue(new KeyTrigger(keycode))) {
            // System.out.println("Key " + keycode + " is already binded");
            // input.getAllBindings().replace(null, null, null);
            // }
            // });
            input.getAllBindings().forEach((useraction, trigger) -> {
                System.out.println("UserAction: " + useraction + " Trigger: " + trigger);
                if (trigger.isKey()) {
                    if (keyBindings.containsKey(KeyCode.getKeyCode(trigger.getName()))) {
                        System.out.println("Key " + trigger.getName() + " is already binded");
                        boolean isremoved = input.getAllBindings().remove(useraction, trigger);
                        System.out.println("UserAction: " + useraction + " Trigger: " + trigger + " removed : "
                                + isremoved);
                        onKey(KeyCode.getKeyCode(trigger.getName()),
                                keyBindings.get(KeyCode.getKeyCode(trigger.getName())));
                        System.out.println("Input: " + trigger.getName() + " reinitialized");
                    }
                }
            });
            keyBindings.forEach((keycode, action) -> onKey(keycode, action));
            System.out.println("Input reinitialized");
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
