package org.enstabretagne.Game;

import static com.almasb.fxgl.dsl.FXGL.getAppWidth;
import static com.almasb.fxgl.dsl.FXGL.getDialogService;
import static com.almasb.fxgl.dsl.FXGL.getGameController;
import static com.almasb.fxgl.dsl.FXGL.getGameScene;
import static com.almasb.fxgl.dsl.FXGL.getGameWorld;
import static com.almasb.fxgl.dsl.FXGL.getNotificationService;
import static com.almasb.fxgl.dsl.FXGL.getPhysicsWorld;
import static com.almasb.fxgl.dsl.FXGL.getWorldProperties;
import static com.almasb.fxgl.dsl.FXGL.getb;
import static com.almasb.fxgl.dsl.FXGL.loopBGM;
import static com.almasb.fxgl.dsl.FXGL.onKey;
import static com.almasb.fxgl.dsl.FXGL.play;
import static com.almasb.fxgl.dsl.FXGL.run;
import static com.almasb.fxgl.dsl.FXGL.spawn;
import static com.almasb.fxgl.dsl.FXGLForKtKt.geti;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.enstabretagne.Component.AlienComponent;
import org.enstabretagne.Component.EntityType;
import org.enstabretagne.Component.PlayerComponent;
import org.enstabretagne.Component.SpaceInvadersFactory;
import org.enstabretagne.Core.*;
import org.enstabretagne.Utils.assetNames;
import org.enstabretagne.Utils.entityNames;

import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.MenuItem;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.Entity;

import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * Classe principale du jeu
 * C'est la classe qui lance le jeu et gère les fonctions principales
 * 
 * @author @jufch, @LBF38, @MathieuDFS
 * @since 0.1.0
 */
public class GameLauncher extends GameApplication {
    private PlayerComponent playerComponent1;
    private PlayerComponent playerComponent2;
    private Entity player1;
    private Entity player2;
    private long last_ambient_sound = System.currentTimeMillis();;
    private int delay_ambient_sound = FXGLMath.random(Constant.AMBIENT_SOUND_DELAY_MIN,
            Constant.AMBIENT_SOUND_DELAY_MAX);

    private Boolean InfinityMode = true;

    /**
     * Initialisation des paramètres du jeu
     * @param settings
     */
    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(Constant.GAME_WIDTH.intValue());
        settings.setHeight(Constant.GAME_HEIGHT.intValue());
        settings.setTitle("Duel Invaders");
        settings.setAppIcon("duelinvaders_icon2.png");
        settings.setVersion("0.1.0");
        settings.setMainMenuEnabled(true);
        settings.setGameMenuEnabled(true);
        settings.setFullScreenAllowed(true);
        settings.setFullScreenFromStart(true);
        settings.setCredits(Arrays.asList(
                "Duel Invaders project by:",
                "@MathieuDFS",
                "@jufch",
                "@LBF38"));
        settings.setEnabledMenuItems(EnumSet.of(MenuItem.EXTRA));

        settings.setApplicationMode(ApplicationMode.RELEASE);
    }

    /**
     * Initialisation des commandes du jeu avec les touches du clavier
     */
    @Override
    protected void initInput() {

        onKey(KeyCode.ENTER, () -> {
            playerComponent1.shoot();
        });

        onKey(KeyCode.RIGHT, () -> {
            playerComponent1.moveRight();
        });

        onKey(KeyCode.LEFT, () -> {
            playerComponent1.moveLeft();
        });

        onKey(KeyCode.SPACE, () -> {
            playerComponent2.shoot();
        });

        onKey(KeyCode.D, () -> {
            playerComponent2.moveRight();
        });

        onKey(KeyCode.Q, () -> {
            playerComponent2.moveLeft();
        });


    }

    /**
     * Initialisation des variables du jeu
     * @param vars
     */
    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put(GameVariableNames.PLAYERS_SCORE, 0);
        vars.put(GameVariableNames.PLAYERS_LIVES, Constant.START_LIVES.intValue());
        vars.put(GameVariableNames.isGameOver, false);
        vars.put(GameVariableNames.isGameWon, false);
    }

    /**
     * Initialisation du jeu
     * Coordonnées des entités et début du fond sonore
     */
    @Override
    protected void initGame() {
        play(assetNames.sounds.START_CLAIRON);
        try {
            TimeUnit.SECONDS.sleep(Constant.WAITING_TIME_BEFORE_START);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        getGameWorld().addEntityFactory(new SpaceInvadersFactory());

        //spawn Player1
        player1 = spawn(entityNames.PLAYER);
        player1.setX(Constant.GAME_WIDTH / 2);
        player1.setY(Constant.GAME_HEIGHT - player1.getHeight());
        playerComponent1 = player1.getComponent(PlayerComponent.class);
        playerComponent1.setDirection(Constant.Direction.UP);

        //spawn Player2
        player2 = spawn(entityNames.PLAYER);
        player2.setX(Constant.GAME_WIDTH / 2);
        player2.setY(0);
        playerComponent2 = player2.getComponent(PlayerComponent.class);
        playerComponent2.setDirection(Constant.Direction.DOWN);

        if(InfinityMode) {
            //spawn Aliens pour infinity mode
            Entity alien1 = spawn(entityNames.ALIEN, 0, Constant.GAME_HEIGHT / 2);
            alien1.getComponent(AlienComponent.class).initialize(Constant.Direction.UP);
            Entity alien2 = spawn(entityNames.ALIEN, 0, Constant.GAME_HEIGHT / 2);
            alien2.getComponent(AlienComponent.class).initialize(Constant.Direction.DOWN);
            run(() -> {
                Entity alien = spawn(entityNames.ALIEN, 0, Constant.GAME_HEIGHT / 2);
                alien.getComponent(AlienComponent.class).initialize(Constant.Direction.UP);
            }, Duration.seconds(1.9));
            run(() -> {
                Entity alien = spawn(entityNames.ALIEN, 0, Constant.GAME_HEIGHT / 2);
                alien.getComponent(AlienComponent.class).initialize(Constant.Direction.DOWN);
            }, Duration.seconds(2.1));
        }

        spawn(entityNames.BACKGROUND);
        loopBGM(assetNames.music.BACKGROUND_MUSIC); // TODO: sélectionner la musique via les paramètres
    }

    /**
     * Initialisation des propriétés physiques du jeu liées aux collisions
     */
    @Override
    protected void initPhysics() {
        getPhysicsWorld().addCollisionHandler(new AlienPlayerCollision(EntityType.PLAYER, EntityType.ALIEN));
        getPhysicsWorld().addCollisionHandler(new AlienBulletCollision(EntityType.BULLET, EntityType.ALIEN));
        getPhysicsWorld()
                .addCollisionHandler(new EnemyShootPlayerCollision(EntityType.ENEMY_SHOOT, EntityType.PLAYER));
        getPhysicsWorld().addCollisionHandler(new BulletPlayerCollision(EntityType.BULLET, EntityType.PLAYER));
        getPhysicsWorld().addCollisionHandler(new BulletBulletCollision(EntityType.BULLET, EntityType.BULLET));
    }

    /**
     * Initialisation de l'interface graphique du jeu avec le score du joueur
     */
    @Override
    protected void initUI() {
        Text textScore = new Text();
        textScore.setX(getAppWidth() - 100);
        textScore.setY(100);
        textScore.textProperty().bind(getWorldProperties().intProperty(GameVariableNames.PLAYERS_SCORE).asString());
        getGameScene().addUINode(textScore);

        Text textLives = new Text();
        textLives.setX(getAppWidth() - 100);
        textLives.setY(200);
        textLives.textProperty().bind(getWorldProperties().intProperty(GameVariableNames.PLAYERS_LIVES).asString());
        getGameScene().addUINode(textLives);
    }

    /**
     * Vérification de la fin du jeu et déroulé de la partie en cours
     * @param tpf
     */
    @Override
    protected void onUpdate(double tpf) {
        if (getb(GameVariableNames.isGameOver))
            gameOverScreen();
        if (getb(GameVariableNames.isGameWon))
            winScreen();

        // teste le temps écoulé depuis la dernière fois que le son d'ambiance a été
        // joué
        if ((System.currentTimeMillis() - last_ambient_sound) > delay_ambient_sound) {
            ambientSound();
            last_ambient_sound = System.currentTimeMillis();
            delay_ambient_sound = FXGLMath.random(Constant.AMBIENT_SOUND_DELAY_MIN, Constant.AMBIENT_SOUND_DELAY_MAX);
        }

        run(() -> {
            getGameWorld().getEntitiesByType(EntityType.ALIEN).forEach((alien) -> {
                if (FXGLMath.randomBoolean(0.01))
                    alien.getComponent(AlienComponent.class).randomShoot(Constant.ALIEN_SHOOT_CHANCE);
            });
        }, Duration.seconds(Constant.random.nextDouble() * 10));
    }

    /**
     * Affichage de l'écran de fin de partie
     */
    private void gameOverScreen() {
        play(assetNames.sounds.DEFEAT_CLAIRON);
        String message = "Game Over! Your score is " + geti(GameVariableNames.PLAYERS_SCORE);
        getDialogService().showMessageBox(message, () -> {
            getDialogService().showConfirmationBox("Do you want to play again?", (yes) -> playAgain(yes));
        });
    }

    /**
     * Affichage de l'écran pour jouer une nouvelle partie
     */
    private void playAgain(Boolean yes) {
        if (yes)
            getGameController().startNewGame();
        else
            getGameController().gotoMainMenu();
    }

    /**
     * Affichage de l'écran de victoire
     */
    private void winScreen() {
        play(assetNames.sounds.VICTORY_CLAIRON);
        String message = "You won! Your score is " + geti(GameVariableNames.PLAYERS_SCORE);
        getDialogService().showMessageBox(message, () -> {
            getDialogService().showConfirmationBox("Do you want to play again?", (yes) -> playAgain(yes));
        });
    }

    /**
     * Joue un son d'ambiance aléatoire parmi ceux disponibles
     */
    private void ambientSound() {
        String ambientMusic = assetNames.sounds.AMBIENT_SOUNDS
                .get(FXGLMath.random(0, Constant.NUMBER_OF_AMBIENT_SOUND-1));
        play(ambientMusic);
    }

    public static void main(String[] args) {
        launch(args);
    }
}