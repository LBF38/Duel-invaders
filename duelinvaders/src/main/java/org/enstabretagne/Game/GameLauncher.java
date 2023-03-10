package org.enstabretagne.Game;

import static com.almasb.fxgl.dsl.FXGL.getGameScene;
import static com.almasb.fxgl.dsl.FXGL.getGameWorld;
import static com.almasb.fxgl.dsl.FXGL.getPhysicsWorld;
import static com.almasb.fxgl.dsl.FXGL.getb;
import static com.almasb.fxgl.dsl.FXGL.loopBGM;
import static com.almasb.fxgl.dsl.FXGL.play;
import static com.almasb.fxgl.dsl.FXGL.spawn;
import static org.enstabretagne.UI.UI_Factory.ambientSound;
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
import org.enstabretagne.Game.GameModes.AlienFactory;
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
import com.almasb.fxgl.core.math.FXGLMath;

/**
 * Classe principale du jeu
 * C'est la classe qui lance le jeu et gère les fonctions principales
 * 
 * @author jufch, LBF38, MathieuDFS
 * @since 0.1.0
 */
public class GameLauncher extends GameApplication {
    private static GameMode game_mode = new ClassicGameMode();
    private long last_ambient_sound = System.currentTimeMillis();
    private int delay_ambient_sound = FXGLMath.random(Settings.AMBIENT_SOUND_DELAY_MIN,
            Settings.AMBIENT_SOUND_DELAY_MAX);

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
        settings.setVersion("0.2.0");
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
        super.initInput();
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
        try {
            game_mode.initInput(getGameScene().getInput());
        } catch (Exception exception) {
            System.out.println("Erreur lors de l'initialisation des inputs : " +
                    exception.getMessage());
        }

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
        if (getb(GameVariableNames.isGameOver) || getb(GameVariableNames.isGameWon)) {
            game_mode.gameFinished();
        }

        if ((System.currentTimeMillis() - last_ambient_sound) > delay_ambient_sound) {
            ambientSound();
            last_ambient_sound = System.currentTimeMillis();
            delay_ambient_sound = FXGLMath.random(Settings.AMBIENT_SOUND_DELAY_MIN, Settings.AMBIENT_SOUND_DELAY_MAX);
        }

        showPlayersLivesAndScores(getGameWorld(), getGameScene());

        AlienFactory.aliensRandomlyShoot();
    }

    public static void main(String[] args) {
        launch(args);
    }
}