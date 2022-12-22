package org.enstabretagne;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;

public class GameLauncher extends GameApplication {
    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(800);
        settings.setHeight(600);
        settings.setTitle("Duel Invaders");
        settings.setVersion("0.1");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
