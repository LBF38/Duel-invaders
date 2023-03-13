package org.enstabretagne.Game;

import static com.almasb.fxgl.dsl.FXGL.getGameScene;
import static com.almasb.fxgl.dsl.FXGL.getGameWorld;
import static com.almasb.fxgl.dsl.FXGL.getPhysicsWorld;
import static com.almasb.fxgl.dsl.FXGL.loopBGM;
import static com.almasb.fxgl.dsl.FXGL.onKey;
import static com.almasb.fxgl.dsl.FXGL.play;
import static com.almasb.fxgl.dsl.FXGL.spawn;
import static org.enstabretagne.UI.UI_Factory.showPlayersLivesAndScores;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Map;

import org.enstabretagne.Collision.AlienBulletCollision;
import org.enstabretagne.Collision.AlienPlayerCollision;
import org.enstabretagne.Collision.BulletBulletCollision;
import org.enstabretagne.Collision.BulletPlayerCollision;
import org.enstabretagne.Collision.EnemyShootBulletCollision;
import org.enstabretagne.Collision.EnemyShootPlayerCollision;
import org.enstabretagne.Component.SpaceInvadersFactory;
import org.enstabretagne.Game.GameModes.ClassicGameMode;
import org.enstabretagne.Game.GameModes.GameMode;
import org.enstabretagne.Game.GameModes.GameModeTypes;
import org.enstabretagne.Utils.GameVariableNames;
import org.enstabretagne.Utils.Settings;
import org.enstabretagne.Utils.assetNames;
import org.enstabretagne.Utils.entityNames;

import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.MenuItem;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;

import javafx.scene.input.KeyCode;

/**
 * Classe principale du jeu
 * C'est la classe qui lance le jeu et gère les fonctions principales
 * 
 * @author jufch, LBF38, MathieuDFS
 * @since 0.1.0
 */
public class GameLauncher extends GameApplication {
    private static GameMode game_mode = new ClassicGameMode();

    public static void setGameMode(GameMode gameMode) {
        game_mode = gameMode;
    }

    public static GameModeTypes getGameModeType() {
        return game_mode.getGameModeType();
    }

    /**
     * Initialisation des paramètres du jeu
     * 
     * @param settings
     */
    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(Settings.GAME_WIDTH.intValue());
        settings.setHeight(Settings.GAME_HEIGHT.intValue());
        settings.setTitle("Duel Invaders");
        settings.setAppIcon(assetNames.textures.APP_ICON);
        settings.setVersion("1.0.1");
        settings.setMainMenuEnabled(true);
        settings.setGameMenuEnabled(true);
        settings.setFullScreenAllowed(true);
        // settings.setFullScreenFromStart(true);
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
        settings.setSceneFactory(new SceneFactory() {
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
        onKey(KeyCode.Q, () -> {
            game_mode.getPlayerComponent1().moveLeft();
        });
        onKey(KeyCode.D, () -> {
            game_mode.getPlayerComponent1().moveRight();
        });
        onKey(KeyCode.SPACE, () -> {
            if (game_mode.getGameModeType().equals(GameModeTypes.MULTIPLAYER)) {
                GameVariableNames.isShooting = true;
            }
            game_mode.getPlayerComponent1().shoot();
        });
        onKey(KeyCode.ENTER, () -> {
            if (game_mode.getGameModeType().equals(GameModeTypes.MULTIPLAYER)) {
                GameVariableNames.isShooting = true;
            }
            game_mode.getPlayerComponent2().shoot();
        });
        onKey(KeyCode.LEFT, () -> {
            game_mode.getPlayerComponent2().moveLeft();
        });
        onKey(KeyCode.RIGHT, () -> {
            game_mode.getPlayerComponent2().moveRight();
        });
    }

    /**
     * Initialisation des variables du jeu
     * 
     * @param vars
     */
    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put(GameVariableNames.isGameOver, false);
        vars.put(GameVariableNames.isGameWon, false);
        GameVariableNames.multiplayerGameInProgress = false;
        GameVariableNames.multiplayerGameWaiting = false;
    }

    /**
     * Initialisation du jeu
     * Coordonnées des entités et début du fond sonore
     */
    @Override
    protected void initGame() {
        play(assetNames.sounds.START_CLAIRON);
        getGameWorld().addEntityFactory(new SpaceInvadersFactory());

        game_mode.initGameMode();

        spawn(entityNames.BACKGROUND);
        loopBGM(assetNames.music.MUSIC_ACROSS_THE_UNIVERSE);
    }

    /**
     * Initialisation des propriétés physiques du jeu liées aux collisions
     */
    @Override
    protected void initPhysics() {
        getPhysicsWorld().addCollisionHandler(new AlienPlayerCollision());
        getPhysicsWorld().addCollisionHandler(new AlienBulletCollision());
        getPhysicsWorld().addCollisionHandler(new EnemyShootPlayerCollision());
        getPhysicsWorld().addCollisionHandler(new BulletPlayerCollision());
        getPhysicsWorld().addCollisionHandler(new BulletBulletCollision());
        getPhysicsWorld().addCollisionHandler(new EnemyShootBulletCollision());
    }

    /**
     * Initialisation de l'interface graphique du jeu avec le score du joueur
     */
    @Override
    protected void initUI() {
        showPlayersLivesAndScores(getGameWorld(), getGameScene());
    }

    /**
     * Vérification de la fin du jeu et déroulé de la partie en cours
     * 
     * @param tpf
     */
    @Override
    protected void onUpdate(double tpf) {
        game_mode.onUpdate(tpf);
    }

    public static void main(String[] args) {
        launch(args);
    }
}