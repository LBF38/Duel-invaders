package org.enstabretagne.Game;

import static com.almasb.fxgl.dsl.FXGL.*;

import java.util.Map;

import org.enstabretagne.Component.SpaceInvadersFactory;
import org.enstabretagne.Core.Collision_EnemyShoot_player;
import org.enstabretagne.Core.Collision_bullet_alien;
import org.enstabretagne.Core.Constant;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.entity.Entity;
import org.enstabretagne.Core.Collision_alien_player;
import org.enstabretagne.Component.EntityType;

import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;

public class GameLauncher extends GameApplication {
    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(Constant.BOARD_WIDTH.intValue());
        settings.setHeight(Constant.BOARD_HEIGHT.intValue());
        settings.setTitle("Basic Game App");
        settings.setVersion("0.1");
    }

    @Override
    protected void initInput() {
        onKey(KeyCode.N, () -> {
            getNotificationService().pushNotification("Hello World!");
        });

        onKey(KeyCode.RIGHT, () -> {
            player.translateX(5); // move right 5 pixels
            inc("pixelsMoved", +5);
        });

        onKey(KeyCode.LEFT, () -> {
            player.translateX(-5); // move left 5 pixels
            inc("pixelsMoved", -5);
        });

        onKey(KeyCode.UP, () -> {
            player.translateY(-5); // move up 5 pixels
            inc("pixelsMoved", +5);
        });

        onKey(KeyCode.DOWN, () -> {
            player.translateY(5); // move down 5 pixels
            inc("pixelsMoved", +5);
        });

        onKeyDown(KeyCode.SPACE, () -> {
            // player.getComponent(EntityType.PLAYER).shoot();
            System.out.println("Shoot");
        });
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("pixelsMoved", 0);
    }

    private Entity player;
    private Entity alien;

    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new SpaceInvadersFactory());
        //spawn("alien");
        player = spawn("player");
        alien = spawn("alien");

    }

    @Override
    protected void initPhysics() {
        getPhysicsWorld().addCollisionHandler(new Collision_alien_player(EntityType.PLAYER, EntityType.ALIEN));
        getPhysicsWorld().addCollisionHandler(new Collision_bullet_alien(EntityType.BULLET, EntityType.ALIEN));
        getPhysicsWorld().addCollisionHandler(new Collision_EnemyShoot_player(EntityType.ENEMY_SHOOT, EntityType.PLAYER));
    }

    @Override
    protected void initUI() {
        Text textPixels = new Text();
        textPixels.setTranslateX(50); // x = 50
        textPixels.setTranslateY(100); // y = 100

        textPixels.textProperty().bind(getWorldProperties().intProperty("pixelsMoved").asString());

        getGameScene().addUINode(textPixels); // add to the scene graph
    }

    public static void main(String[] args) {
        launch(args);
    }
}