package org.enstabretagne.Game;

import static com.almasb.fxgl.dsl.FXGL.getDialogService;
import static com.almasb.fxgl.dsl.FXGL.getGameScene;
import static com.almasb.fxgl.dsl.FXGL.getGameWorld;
import static com.almasb.fxgl.dsl.FXGL.getNetService;
import static com.almasb.fxgl.dsl.FXGL.getPhysicsWorld;
import static com.almasb.fxgl.dsl.FXGL.getb;
import static com.almasb.fxgl.dsl.FXGL.loopBGM;
import static com.almasb.fxgl.dsl.FXGL.onKey;
import static com.almasb.fxgl.dsl.FXGL.play;
import static com.almasb.fxgl.dsl.FXGL.runOnce;
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
import org.enstabretagne.Component.AlienComponent;
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
import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.net.Client;
import com.almasb.fxgl.net.Server;

import javafx.scene.input.KeyCode;
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

    public static void setGameMode(GameMode gameMode) {
        game_mode = gameMode;
    }

    public static GameModeTypes getGameModeType() {
        return game_mode.getGameModeType();
    }

    private boolean isServer;
    private Server<Bundle> server;
    private Client<Bundle> client;

    private boolean alienSpawnStart = false; // indique si les aliens ont commencé à spawn

    private int Port;
    private String IP;

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
        onKey(KeyCode.Q, () -> {
            game_mode.getPlayerComponent1().moveLeft();
        });
        onKey(KeyCode.D, () -> {
            game_mode.getPlayerComponent1().moveRight();
        });
        onKey(KeyCode.SPACE, () -> {
            game_mode.getPlayerComponent1().shoot();
        });
        onKey(KeyCode.ENTER, () -> {
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
     * Input box pour le choix héberger/rejoindre une partie multijoueur
     */
    private void isServer() {
        runOnce(() -> {
            getDialogService().showConfirmationBox("Voulez-vous être le serveur ?", yes -> {
                if (yes) {
                    isServer = true;
                    PortSelection();
                } else {
                    isServer = false;
                    PortSelection();
                }
            });
            return;
        }, Duration.seconds(0));
    }

    /**
     * Initialisation du serveur
     */
    private void isServer_ServerInit() {
        System.out.println("server");
        server = getNetService().newTCPServer(Port);
        onReceiveMessageServer();
        server.startAsync();
        GameVariableNames.multiplayerGameWaiting = true;
    }

    /**
     * Initialisation du client
     */
    private void isServer_ClientInit() {
        System.out.println("client");
        client = getNetService().newTCPClient(IP, Port);
        onReceiveMessageClient();
        client.connectAsync();
        GameVariableNames.multiplayerGameWaiting = true;
    }

    private void PortSelection() {
        runOnce(() -> {
            getDialogService().showInputBox("Entrez le port (ex:55555)", port -> {
                if (port != null) {
                    Port = Integer.parseInt(port);
                } else {
                    Port = 55555;
                }
                System.out.println("Port : " + Port);
                if (!isServer) {
                    IPSelection();
                } else {
                    isServer_ServerInit();
                }
            });
            return;
        }, Duration.seconds(0));
    }

    private void IPSelection() {
        runOnce(() -> {
            getDialogService().showInputBox("Entrez l'adresse IP du serveur (ex: localhost ou 111.222.333.444)", ip -> {
                if (ip != null) {
                    IP = ip;
                } else {
                    IP = "localhost";
                }
                System.out.println("IP : " + IP);
                isServer_ClientInit();
            });
            return;
        }, Duration.seconds(0));
    }

    private void startMultiGame() {
        long startGameTime = System.currentTimeMillis();
        System.out.println("startGameTime : " + startGameTime);
        AlienFactory.makeAlienBlock();
    }

    /**
     * Logique d'envoi des données à chaque frame
     */
    private void onUpdateBroadcastLogic() {// LBF : dans le mode multijoueur
        if (isServer) {
            Player1Broadcast();
        } else {
            Player2Broadcast();
        } // Ne sert à rien pour le moment mais utile si plus de choses à transférer à
          // chaque frame
    }

    /**
     * Envoie des données du joueur 1 du serveur vers le client
     */
    private void Player1Broadcast() { // LBF : dans le mode multijoueur
        Bundle bundle = new Bundle("Player1");
        bundle.put("type", "Player1");
        bundle.put("x", player1.getX());
        bundle.put("y", player1.getY());
        bundle.put("score", playerComponent1.getScore());
        bundle.put("life", playerComponent1.getLife());
        server.broadcast(bundle);
    }

    /**
     * Envoie des données du joueur 2 du client vers le serveur
     */
    private void Player2Broadcast() { // LBF : dans le mode multijoueur
        Bundle bundle = new Bundle("Player2");
        bundle.put("type", "Player2");
        bundle.put("x", player2.getX());
        bundle.put("y", player2.getY());
        bundle.put("score", playerComponent2.getScore());
        bundle.put("life", playerComponent2.getLife());
        client.broadcast(bundle);
    }

    /**
     * Logique lors de la réception des données du client par le serveur
     */
    private void onReceiveMessageServer() { // LBF : dans le mode multijoueur
        server.setOnConnected(connection -> {
            connection.addMessageHandlerFX((conn, message) -> {
                if (message.getName().equals("Player2")) {
                    player2.setX(message.get("x"));
                    player2.setY(message.get("y"));
                    playerComponent2.setScore(message.get("score"));
                    playerComponent2.setLife(message.get("life"));
                } else if (message.getName().equals("Player2Shoot")) {
                    playerComponent2.shoot();
                } else if (message.getName().equals("Client Connected")) {
                    if (!GameVariableNames.multiplayerGameInProgress) {
                        server.broadcast(new Bundle("Server Start"));
                        GameVariableNames.multiplayerGameInProgress = true;
                        startMultiGame();
                    }
                } else if (message.getName().equals("Game End")) {
                    System.out.println("Game End Received");
                    if (message.get("type").equals("Game Over")) {
                        gameOverScreen();
                    } else if (message.get("type").equals("Game Win")) {
                        winScreen();
                    }
                } else {
                    System.out.println("Message non reconnu");
                }
            });
        });
    }

    /**
     * Logique lors de la réception des données du serveur par le client
     */
    private void onReceiveMessageClient() { // LBF : dans le mode multijoueur
        client.setOnConnected(connection -> {
            connection.addMessageHandlerFX((conn, message) -> {
                if (message.getName().equals("Player1")) {
                    player1.setX(message.get("x"));
                    player1.setY(message.get("y"));
                    playerComponent1.setScore(message.get("score"));
                    playerComponent1.setLife(message.get("life"));
                } else if (message.getName().equals("Player1Shoot")) {
                    playerComponent1.shoot();
                } else if (message.getName().equals("AlienSpawn")) {
                    if (message.get("direction") == Constant.Direction.DOWN) {
                        Entity alien = spawn(entityNames.ALIEN, 0, Constant.GAME_HEIGHT / 2 - Constant.ALIEN_HEIGHT);
                        alien.getComponent(AlienComponent.class).initialize(Constant.Direction.DOWN);
                    } else {
                        Entity alien = spawn(entityNames.ALIEN, 0, Constant.GAME_HEIGHT / 2 - Constant.ALIEN_HEIGHT);
                        alien.getComponent(AlienComponent.class).initialize(Constant.Direction.UP);
                    }
                } else if (message.getName().equals("Server Start")) {
                    GameVariableNames.multiplayerGameInProgress = true;
                    startMultiGame();
                } else if (message.getName().equals("Game End")) {
                    System.out.println("Game End Received");
                    if (message.get("type").equals("Game Over")) {
                        gameOverScreen();
                    } else if (message.get("type").equals("Game Win")) {
                        winScreen();
                    }
                } else {
                    System.out.println("Message non reconnu");
                }
            });
        });
    }

    /**
     * Logique d'envoi des données lors du tir d'un joueur
     */
    private void onShootBroadcastLogic() { // LBF : dans le mode multijoueur
        if (isServer) {
            server.broadcast(new Bundle("Player1Shoot"));
        } else {
            client.broadcast(new Bundle("Player2Shoot"));
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