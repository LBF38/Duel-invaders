package org.enstabretagne.Game.GameModes;

import org.enstabretagne.Component.AlienComponent;
import org.enstabretagne.Utils.Settings;
import org.enstabretagne.Utils.entityNames;

import com.almasb.fxgl.entity.Entity;
import static com.almasb.fxgl.dsl.FXGL.*;

import javafx.util.Duration;

public class InfinityGameMode extends TwoPlayerGameMode {

    @Override
    public void initGameMode() {
        super.initTwoPlayerGameMode();
        // spawn Aliens pour infinity mode
        Entity alien1 = spawn(entityNames.ALIEN, 0, Settings.GAME_HEIGHT / 2 -
                Settings.ALIEN_HEIGHT);
        alien1.getComponent(AlienComponent.class).initialize(Settings.Direction.UP);
        Entity alien2 = spawn(entityNames.ALIEN, 0, Settings.GAME_HEIGHT / 2 -
                Settings.ALIEN_HEIGHT);
        alien2.getComponent(AlienComponent.class).initialize(Settings.Direction.DOWN);
        run(() -> {
            Entity alien = spawn(entityNames.ALIEN, 0, Settings.GAME_HEIGHT / 2 -
                    Settings.ALIEN_HEIGHT);
            alien.getComponent(AlienComponent.class).initialize(Settings.Direction.UP);
        }, Duration.seconds(1.4));
        run(() -> {
            Entity alien = spawn(entityNames.ALIEN, 0, Settings.GAME_HEIGHT / 2 -
                    Settings.ALIEN_HEIGHT);
            alien.getComponent(AlienComponent.class).initialize(Settings.Direction.DOWN);
        }, Duration.seconds(1.5));
    }

    @Override
    public GameModeTypes getGameModeType() {
        return GameModeTypes.INFINITY;
    }

}
