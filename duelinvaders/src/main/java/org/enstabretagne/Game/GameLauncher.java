package org.enstabretagne.Game;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.fxgl.dsl.FXGLForKtKt.geti;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.dsl.FXGL;
import org.enstabretagne.Component.*;
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
    private Entity life1;
    private Entity life2;
    private Entity life3;
    private long last_ambient_sound = System.currentTimeMillis();
    private int delay_ambient_sound = FXGLMath.random(Constant.AMBIENT_SOUND_DELAY_MIN,
            Constant.AMBIENT_SOUND_DELAY_MAX);

    private static int GameMode = 2; // 0 -> classique, 1 -> InfinityMode, 2->Solo

    public static void setGameMode(int gameMode) {
        GameMode = gameMode;
    }


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
                "@LBF38",
                "",
                "Music from:",
                "https://www.jamendo.com/start",
                "Oleg O.Kachanko - Across the Universes",
                "Raresix - Beyond Consciouness",
                "Scythe of Luna - Dark Matter Sprouts (Off Vocal)",
                "Social Bot - Degrees of Freedom",
                "cyborhjeff - Stellar remember",
                "",
                "Sounds effect from:",
                "https://universal-soundbank.com/"));
        settings.setEnabledMenuItems(EnumSet.of(MenuItem.EXTRA));
        settings.setApplicationMode(ApplicationMode.RELEASE);
        settings.setSceneFactory(new SceneFactory(){
            @Override
            public FXGLMenu newMainMenu() {
                return new NewMainMenu();
            }
        });
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
            if(GameMode == 2) {
                playerComponent1.shoot();
            }else {
                playerComponent2.shoot();
            }
        });

        onKey(KeyCode.D, () -> {
            if(GameMode == 2) {
                playerComponent1.moveRight();
            }else {
                playerComponent2.moveRight();
            }
        });

        onKey(KeyCode.Q, () -> {
            if(GameMode == 2) {
                playerComponent1.moveLeft();
            }else {
                playerComponent2.moveLeft();
            }
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
        getGameWorld().addEntityFactory(new SpaceInvadersFactory());

        //spawn Player1
        player1 = spawn(entityNames.PLAYER);
        player1.setX(Constant.GAME_WIDTH / 2);
        player1.setY(Constant.GAME_HEIGHT - player1.getHeight());
        playerComponent1 = player1.getComponent(PlayerComponent.class);
        playerComponent1.setDirection(Constant.Direction.UP);

        if(GameMode != 2) {
            //spawn Player2
            player2 = spawn(entityNames.PLAYER);
            player2.setX(Constant.GAME_WIDTH / 2);
            player2.setY(0);
            playerComponent2 = player2.getComponent(PlayerComponent.class);
            playerComponent2.setDirection(Constant.Direction.DOWN);
        }

        if(GameMode== 1) {
            //spawn Aliens pour infinity mode

            Entity alien1 = spawn(entityNames.ALIEN, 0, Constant.GAME_HEIGHT/2- Constant.ALIEN_HEIGHT);
            alien1.getComponent(AlienComponent.class).initialize(Constant.Direction.UP);
            Entity alien2 = spawn(entityNames.ALIEN, 0, Constant.GAME_HEIGHT/2- Constant.ALIEN_HEIGHT);
            alien2.getComponent(AlienComponent.class).initialize(Constant.Direction.DOWN);
            run(() -> {
                Entity alien = spawn(entityNames.ALIEN, 0, Constant.GAME_HEIGHT/2- Constant.ALIEN_HEIGHT);
                alien.getComponent(AlienComponent.class).initialize(Constant.Direction.UP);
            }, Duration.seconds(1.4));
            run(() -> {
                Entity alien = spawn(entityNames.ALIEN, 0, Constant.GAME_HEIGHT/2- Constant.ALIEN_HEIGHT);
                alien.getComponent(AlienComponent.class).initialize(Constant.Direction.DOWN);
            }, Duration.seconds(1.5));

        } else if (GameMode == 0) {
            makeAlienBlock();
        } else if (GameMode==2){
            makeAlienBlockSolo();
        }

        spawn(entityNames.BACKGROUND);
        loopBGM(assetNames.music.MUSIC_ACROSS_THE_UNIVERSE);

        //spawn life
        life3 = spawn(entityNames.LIFE,3,0);
        life3.getComponent(LifeComponent.class).initialize(Constant.Direction.UP);
        life2 = spawn(entityNames.LIFE,2,0);
        life2.getComponent(LifeComponent.class).initialize(Constant.Direction.UP);
        life2.getComponent(LifeComponent.class).updateLife(false);
        life1 = spawn(entityNames.LIFE,1,0);
        life1.getComponent(LifeComponent.class).initialize(Constant.Direction.UP);
        life1.getComponent(LifeComponent.class).updateLife(false);
    }

    private void makeAlienBlock() {
        for (int i = 0; i < 2; i++) {
            makeAlienLine(i, Constant.Direction.DOWN);
            makeAlienLine(i, Constant.Direction.UP);
        }
    }

    private void makeAlienLine(int line,Constant.Direction direction) {
        for (int i = 0; i < Constant.ALIENS_NUMBER; i++) {
            if(direction == Constant.Direction.DOWN) {
                Entity alien = spawn(entityNames.ALIEN, i * Constant.ALIEN_WIDTH, Constant.GAME_HEIGHT/2+(line-1) * Constant.ALIEN_HEIGHT);
                alien.getComponent(AlienComponent.class).initialize(direction);
                alien.getComponent(AlienComponent.class).setAlienNumber(i);
            } else {
                Entity alien = spawn(entityNames.ALIEN, i * Constant.ALIEN_WIDTH, Constant.GAME_HEIGHT/2 + (line-2) * Constant.ALIEN_HEIGHT);
                alien.getComponent(AlienComponent.class).initialize(direction);
                alien.getComponent(AlienComponent.class).setAlienNumber(i);
            }

        }
    }

    private void makeAlienBlockSolo() {
        for (int line = 0; line <4; line++) {
            for (int k = 0; k < Constant.ALIENS_NUMBER; k++) {
                Entity alien = spawn(entityNames.ALIEN, k*Constant.ALIEN_WIDTH, (line-1) * Constant.ALIEN_HEIGHT);
                alien.getComponent(AlienComponent.class).initialize(Constant.Direction.DOWN);
                alien.getComponent(AlienComponent.class).setAlienNumber(k);
            }
        }
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
        getPhysicsWorld().addCollisionHandler(new EnemyShootBulletCollision(EntityType.BULLET, EntityType.ENEMY_SHOOT));
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
        Life_Update();
        run(() -> {
            getGameWorld().getEntitiesByType(EntityType.ALIEN).forEach((alien) -> {
                if (FXGLMath.randomBoolean(0.01))
                    alien.getComponent(AlienComponent.class).randomShoot(Constant.ALIEN_SHOOT_CHANCE);
            });
        }, Duration.seconds(Constant.random.nextDouble() * 10));
    }

    private void Life_Update() {
        int life_number = geti(GameVariableNames.PLAYERS_LIVES);
        if(life_number==3){
            life1.getComponent(LifeComponent.class).updateLife(false);
            life2.getComponent(LifeComponent.class).updateLife(false);
            life3.getComponent(LifeComponent.class).updateLife(true);
        }else if(life_number==2) {
            life1.getComponent(LifeComponent.class).updateLife(false);
            life2.getComponent(LifeComponent.class).updateLife(true);
            life3.getComponent(LifeComponent.class).updateLife(false);
        }else if(life_number==1) {
            life1.getComponent(LifeComponent.class).updateLife(true);
            life2.getComponent(LifeComponent.class).updateLife(false);
            life3.getComponent(LifeComponent.class).updateLife(false);
        }
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