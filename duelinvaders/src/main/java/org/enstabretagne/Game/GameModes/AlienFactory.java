package org.enstabretagne.Game.GameModes;

import org.enstabretagne.Component.AlienComponent;
import org.enstabretagne.Utils.Settings;
import org.enstabretagne.Utils.entityNames;
import static com.almasb.fxgl.dsl.FXGL.*;

import com.almasb.fxgl.entity.Entity;

public class AlienFactory {
    static protected void makeAlienBlock() {
        for (int i = 0; i < 2; i++) {
            makeAlienLine(i, Settings.Direction.DOWN);
            makeAlienLine(i, Settings.Direction.UP);
        }
    }

    static protected void makeAlienLine(int line, Settings.Direction direction) {
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

    static protected void makeAlienBlockSolo() {
        for (int line = 0; line < 4; line++) {
            for (int k = 0; k < Settings.ALIENS_NUMBER; k++) {
                Entity alien = spawn(entityNames.ALIEN, k * Settings.ALIEN_WIDTH, (line - 1) * Settings.ALIEN_HEIGHT);
                alien.getComponent(AlienComponent.class).initialize(Settings.Direction.DOWN);
                alien.getComponent(AlienComponent.class).setAlienNumber(k);
            }
        }
    }
}
