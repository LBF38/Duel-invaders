package org.enstabretagne.Game;

import static com.almasb.fxgl.dsl.FXGL.getDialogService;
import static com.almasb.fxgl.dsl.FXGL.getGameController;
import static com.almasb.fxgl.dsl.FXGL.getGameScene;
import static com.almasb.fxgl.dsl.FXGL.getGameWorld;
import static com.almasb.fxgl.dsl.FXGL.getPhysicsWorld;
import static com.almasb.fxgl.dsl.FXGL.getUIFactoryService;
import static com.almasb.fxgl.dsl.FXGL.getb;
import static com.almasb.fxgl.dsl.FXGL.loopBGM;
import static com.almasb.fxgl.dsl.FXGL.play;
import static com.almasb.fxgl.dsl.FXGL.run;
import static com.almasb.fxgl.dsl.FXGL.spawn;
import static com.almasb.fxgl.dsl.FXGL.texture;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.enstabretagne.Collision.AlienBulletCollision;
import org.enstabretagne.Collision.AlienPlayerCollision;
import org.enstabretagne.Collision.BulletBulletCollision;
import org.enstabretagne.Collision.BulletPlayerCollision;
import org.enstabretagne.Collision.EnemyShootBulletCollision;
import org.enstabretagne.Collision.EnemyShootPlayerCollision;
import org.enstabretagne.Component.AlienComponent;
import org.enstabretagne.Component.PlayerComponent;
import org.enstabretagne.Component.SpaceInvadersFactory;
import org.enstabretagne.Game.GameModes.ClassicGameMode;
import org.enstabretagne.Game.GameModes.GameMode;
import org.enstabretagne.Utils.EntityType;
import org.enstabretagne.Utils.GameModeTypes;
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
import com.almasb.fxgl.entity.Entity;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

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
    VBox playersUI = new VBox();

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
        game_mode.initInput(getGameScene().getInput());
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
            game_mode.rebindInput(getGameScene().getInput());
        } catch (Exception exception) {
            System.out.println("Erreur lors de l'initialisation des inputs : " +
                    exception.getMessage());
        }

        // if (GameModeType == INFINITY_MODE) {
        // // spawn Aliens pour infinity mode
        // Entity alien1 = spawn(entityNames.ALIEN, 0, Settings.GAME_HEIGHT / 2 -
        // Settings.ALIEN_HEIGHT);
        // alien1.getComponent(AlienComponent.class).initialize(Settings.Direction.UP);
        // Entity alien2 = spawn(entityNames.ALIEN, 0, Settings.GAME_HEIGHT / 2 -
        // Settings.ALIEN_HEIGHT);
        // alien2.getComponent(AlienComponent.class).initialize(Settings.Direction.DOWN);
        // run(() -> {
        // Entity alien = spawn(entityNames.ALIEN, 0, Settings.GAME_HEIGHT / 2 -
        // Settings.ALIEN_HEIGHT);
        // alien.getComponent(AlienComponent.class).initialize(Settings.Direction.UP);
        // }, Duration.seconds(1.4));
        // run(() -> {
        // Entity alien = spawn(entityNames.ALIEN, 0, Settings.GAME_HEIGHT / 2 -
        // Settings.ALIEN_HEIGHT);
        // alien.getComponent(AlienComponent.class).initialize(Settings.Direction.DOWN);
        // }, Duration.seconds(1.5));

        // } else if (GameModeType == CLASSIQUE) {
        // makeAlienBlock();
        // } else if (GameModeType == SOLO) {
        // makeAlienBlockSolo();
        // }

        spawn(entityNames.BACKGROUND);
        loopBGM(assetNames.music.MUSIC_ACROSS_THE_UNIVERSE);
    }

    private void makeAlienBlock() {
        for (int i = 0; i < 2; i++) {
            makeAlienLine(i, Settings.Direction.DOWN);
            makeAlienLine(i, Settings.Direction.UP);
        }
    }

    private void makeAlienLine(int line, Settings.Direction direction) {
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

    private void makeAlienBlockSolo() {
        for (int line = 0; line < 4; line++) {
            for (int k = 0; k < Settings.ALIENS_NUMBER; k++) {
                Entity alien = spawn(entityNames.ALIEN, k * Settings.ALIEN_WIDTH, (line - 1) * Settings.ALIEN_HEIGHT);
                alien.getComponent(AlienComponent.class).initialize(Settings.Direction.DOWN);
                alien.getComponent(AlienComponent.class).setAlienNumber(k);
            }
        }
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
        showPlayersLivesAndScores();
    }

    private void showPlayersLivesAndScores() {
        getGameScene().removeChild(playersUI);

        List<HBox> playersViews = new ArrayList<>();
        List<PlayerComponent> players = getGameWorld().getEntitiesByType(EntityType.PLAYER).stream()
                .map(player -> player.getComponent(PlayerComponent.class)).collect(Collectors.toList());
        for (PlayerComponent playerComponent : players) {
            HBox scoreUI = createScoreUI(playerComponent.getScore(), playerComponent.getId());
            scoreUI.setTranslateY(scoreUI.getHeight() * playerComponent.getId());
            HBox lifeUI = createLifeUI(playerComponent.getLife());
            var playerUI = new HBox(30, scoreUI, lifeUI);
            playersViews.add(playerUI);
        }
        playersUI = new VBox(20, playersViews.toArray(new HBox[0]));
        getGameScene().addChild(playersUI);
    }

    private HBox createScoreUI(int score, int player_id) {
        Text scoreText = getUIFactoryService().newText(Integer.toString(score), Color.WHITE, 24.0);
        Text playerText = getUIFactoryService().newText("Player " + Integer.toString(player_id % 2 + 1), Color.WHITE,
                24.0);
        var scoreView = new HBox(10, playerText, scoreText);
        scoreView.setAlignment(Pos.CENTER);
        return scoreView;
    }

    private HBox createLifeUI(int life) {
        var lifeTexture = texture(assetNames.textures.LIFE, 30, 30);
        var lifeView = new HBox(10);
        for (int i = 0; i < life; i++) {
            lifeView.getChildren().add(lifeTexture.copy());
        }
        lifeView.setAlignment(Pos.CENTER);
        return lifeView;
    }

    /**
     * Vérification de la fin du jeu et déroulé de la partie en cours
     * 
     * @param tpf
     */
    @Override
    protected void onUpdate(double tpf) {
        if (getb(GameVariableNames.isGameOver))
            gameOverScreen("to refactor", "to refactor"); // TODO : refactor
        if (getb(GameVariableNames.isGameWon))
            winScreen("to refactor", "to refactor"); // TODO : refactor

        if ((System.currentTimeMillis() - last_ambient_sound) > delay_ambient_sound) {
            ambientSound();
            last_ambient_sound = System.currentTimeMillis();
            delay_ambient_sound = FXGLMath.random(Settings.AMBIENT_SOUND_DELAY_MIN, Settings.AMBIENT_SOUND_DELAY_MAX);
        }
        if (getGameScene().getContentRoot().getChildren().contains(playersUI))
            showPlayersLivesAndScores();
        run(() -> {
            getGameWorld().getEntitiesByType(EntityType.ALIEN).forEach((alien) -> {
                if (FXGLMath.randomBoolean(0.01))
                    alien.getComponent(AlienComponent.class).randomShoot(Settings.ALIEN_SHOOT_CHANCE);
            });
        }, Duration.seconds(Settings.random.nextDouble() * 10));
    }

    /**
     * Affichage de l'écran de fin de partie
     */
    private void gameOverScreen(String score_player1, String score_player2) {
        play(assetNames.sounds.DEFEAT_CLAIRON);
        String message = "Game Over ! \n Scores are as follows : \n" +
                "Player 1 : " + score_player1 + "\n";
        if (score_player2 != null) {
            String player2 = "Player 2 : " + score_player2;
            message += player2;
        }
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
    private void winScreen(String score_player1, String score_player2) {
        play(assetNames.sounds.VICTORY_CLAIRON);
        String message = "You won ! \n Scores are as follows : \n" +
                "Player 1 : " + score_player1 + "\n";
        if (score_player2 != null) {
            String player2 = "Player 2 : " + score_player2;
            message += player2;
        }
        getDialogService().showMessageBox(message, () -> {
            getDialogService().showConfirmationBox("Do you want to play again?", (yes) -> playAgain(yes));
        });
    }

    /**
     * Joue un son d'ambiance aléatoire parmi ceux disponibles
     */
    private void ambientSound() {
        String ambientMusic = assetNames.sounds.AMBIENT_SOUNDS
                .get(FXGLMath.random(0, Settings.NUMBER_OF_AMBIENT_SOUND - 1));
        play(ambientMusic);
    }

    public static void main(String[] args) {
        launch(args);
    }
}