package org.enstabretagne.Game;

import static com.almasb.fxgl.dsl.FXGL.*;

import java.util.Map;

import org.enstabretagne.Component.PlayerComponent;
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
import javafx.util.Duration;

public class GameLauncher extends GameApplication {
    private PlayerComponent playerComponent;
    private Entity player;

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
            playerComponent.moveRight();// move right 5 pixels
            inc("pixelsMoved", +5);
        });

        onKey(KeyCode.LEFT, () -> {
            playerComponent.moveLeft(); // move left 5 pixels
            inc("pixelsMoved", -5);
        });

        onKey(KeyCode.UP, () -> {
            // player.translateY(-5); // move up 5 pixels
            // inc("pixelsMoved", -5);
        });

        onKey(KeyCode.DOWN, () -> {
            // player.translateY(5); // move down 5 pixels
            // inc("pixelsMoved", +5);
        });

        onKeyDown(KeyCode.SPACE, () -> {
            getGameWorld().addEntity(playerComponent.shoot());
        });
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("pixelsMoved", 0);
        vars.put("Player1_score", 0);
        vars.put("isGameOver", false);
    }

    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new SpaceInvadersFactory());
        player = spawn("player");
        spawn("alien");
        run(() -> {
            spawn("alien");
        }, Duration.seconds(2));
        player.setX(Constant.BOARD_WIDTH / 2);
        player.setY(Constant.BOARD_HEIGHT - player.getHeight());
        playerComponent = player.getComponent(PlayerComponent.class);
    }

    @Override
    protected void initPhysics() {
        getPhysicsWorld().addCollisionHandler(new Collision_alien_player(EntityType.PLAYER, EntityType.ALIEN));
        getPhysicsWorld().addCollisionHandler(new Collision_bullet_alien(EntityType.BULLET, EntityType.ALIEN));
        getPhysicsWorld()
                .addCollisionHandler(new Collision_EnemyShoot_player(EntityType.ENEMY_SHOOT, EntityType.PLAYER));
    }

    @Override
    protected void initUI() {
        Text textPixels = new Text();
        textPixels.setTranslateX(50); // x = 50
        textPixels.setTranslateY(100); // y = 100

        textPixels.textProperty().bind(getWorldProperties().intProperty("pixelsMoved").asString());

        getGameScene().addUINode(textPixels); // add to the scene graph

        Text textScore = new Text();
        textScore.setX(getAppWidth() - 100);
        textScore.setY(100);
        textScore.textProperty().bind(getWorldProperties().intProperty("Player1_score").asString());
        getGameScene().addUINode(textScore);
    }

    @Override
    protected void onUpdate(double tpf) {
        if (getb("isGameOver"))
            gameOverScreen();
    }

    private void gameOverScreen() {
        getDialogService().showMessageBox("Game Over!", () -> {
            getDialogService().showConfirmationBox("Do you want to play again?", (yes) -> playAgain(yes));
        });
    }

    private void playAgain(Boolean yes) {
        if (yes)
            getGameController().startNewGame();
        else
            getGameController().exit();
    }

    public static void main(String[] args) {
        launch(args);
    }
}