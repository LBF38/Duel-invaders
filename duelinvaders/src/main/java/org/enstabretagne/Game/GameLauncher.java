package org.enstabretagne.Game;

import static com.almasb.fxgl.dsl.FXGL.*;

import java.util.Map;

import com.almasb.fxgl.dsl.FXGL;
import org.enstabretagne.Component.AlienComponent;
import org.enstabretagne.Component.EntityType;
import org.enstabretagne.Component.PlayerComponent;
import org.enstabretagne.Component.SpaceInvadersFactory;
import org.enstabretagne.Core.AlienBulletCollision;
import org.enstabretagne.Core.AlienPlayerCollision;
import org.enstabretagne.Core.Constant;
import org.enstabretagne.Core.EnemyShootPlayerCollision;
import org.enstabretagne.Core.GameVariableNames;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.Entity;

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
            playerComponent.moveRight();
        });

        onKey(KeyCode.LEFT, () -> {
            playerComponent.moveLeft();
        });

        onKey(KeyCode.SPACE, () -> {
            playerComponent.shoot();
        });
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put(GameVariableNames.PLAYER1_SCORE, 0);
        vars.put(GameVariableNames.PLAYER1_LIVES, Constant.START_LIVES.intValue());
        vars.put(GameVariableNames.isGameOver, false);
        vars.put(GameVariableNames.isGameWon, false);
    }

    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new SpaceInvadersFactory());
        player = spawn("player");
        spawn("alien");
        spawn("background");
        run(() -> {
            spawn("alien");
        }, Duration.seconds(2));
        player.setX(Constant.BOARD_WIDTH / 2);
        player.setY(Constant.BOARD_HEIGHT - player.getHeight());
        playerComponent = player.getComponent(PlayerComponent.class);

        loopBGM("Across_the_Universe_-_Oleg_O._Kachanko.mp3");//lance la musique todo: sélectionner la musique
    }

    @Override
    protected void initPhysics() {
        getPhysicsWorld().addCollisionHandler(new AlienPlayerCollision(EntityType.PLAYER, EntityType.ALIEN));
        getPhysicsWorld().addCollisionHandler(new AlienBulletCollision(EntityType.BULLET, EntityType.ALIEN));
        getPhysicsWorld()
                .addCollisionHandler(new EnemyShootPlayerCollision(EntityType.ENEMY_SHOOT, EntityType.PLAYER));
    }

    @Override
    protected void initUI() {
        Text textScore = new Text();
        textScore.setX(getAppWidth() - 100);
        textScore.setY(100);
        textScore.textProperty().bind(getWorldProperties().intProperty(GameVariableNames.PLAYER1_SCORE).asString());
        getGameScene().addUINode(textScore);

        Text textLives = new Text();
        textLives.setX(getAppWidth() - 100);
        textLives.setY(200);
        textLives.textProperty().bind(getWorldProperties().intProperty(GameVariableNames.PLAYER1_LIVES).asString());
        getGameScene().addUINode(textLives);
    }

    @Override
    protected void onUpdate(double tpf) {
        if (getb(GameVariableNames.isGameOver))
            gameOverScreen();
        if (getb(GameVariableNames.isGameWon))
            winScreen();
        run(() -> {
            getGameWorld().getEntitiesByType(EntityType.ALIEN).forEach((alien) -> {
                if (FXGLMath.randomBoolean(0.01))
                    alien.getComponent(AlienComponent.class).randomShoot(Constant.ALIEN_SHOOT_CHANCE);
            });
        }, Duration.seconds(Constant.random.nextDouble() * 10));
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

    private void winScreen() {
        getDialogService().showMessageBox("You win!", () -> {
            getDialogService().showConfirmationBox("Do you want to play again?", (yes) -> playAgain(yes));
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}