package org.enstabretagne.Game;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.fxgl.dsl.FXGLForKtKt.runOnce;
import static org.enstabretagne.Game.GameMode.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.net.Client;
import com.almasb.fxgl.net.Server;
import org.enstabretagne.Component.AlienComponent;
import org.enstabretagne.Component.EntityType;
import org.enstabretagne.Component.PlayerComponent;
import org.enstabretagne.Component.SpaceInvadersFactory;
import org.enstabretagne.Core.AlienBulletCollision;
import org.enstabretagne.Core.AlienPlayerCollision;
import org.enstabretagne.Core.BulletBulletCollision;
import org.enstabretagne.Core.BulletPlayerCollision;
import org.enstabretagne.Core.Constant;
import org.enstabretagne.Core.EnemyShootBulletCollision;
import org.enstabretagne.Core.EnemyShootPlayerCollision;
import org.enstabretagne.Core.GameVariableNames;
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
import javafx.scene.input.KeyCode;
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
    private PlayerComponent playerComponent1;
    private PlayerComponent playerComponent2;
    private Entity player1;
    private Entity player2;
    private long last_ambient_sound = System.currentTimeMillis();
    private int delay_ambient_sound = FXGLMath.random(Constant.AMBIENT_SOUND_DELAY_MIN,
            Constant.AMBIENT_SOUND_DELAY_MAX);
    private static GameMode GameMode = CLASSIQUE;
    VBox playersUI = new VBox();

    public static void setGameMode(GameMode gameMode) {
        GameMode = gameMode;
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
        settings.setWidth(Constant.GAME_WIDTH.intValue());
        settings.setHeight(Constant.GAME_HEIGHT.intValue());
        settings.setTitle("Duel Invaders");
        settings.setAppIcon(assetNames.textures.APP_ICON);
        settings.setVersion("0.2.0");
        settings.setMainMenuEnabled(true);
        settings.setGameMenuEnabled(true);
        settings.setFullScreenAllowed(true);
//        settings.setFullScreenFromStart(true);
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
        onKey(KeyCode.ENTER, () -> {
            if (GameVariableNames.multiplayerGameInProgress){
                onShootBroadcastLogic();
                if(isServer){
                    playerComponent1.shoot();
                }else {
                    playerComponent2.shoot();
                }
            } else if (!GameVariableNames.multiplayerGameWaiting){
                playerComponent1.shoot();
            }
        });

        onKey(KeyCode.RIGHT, () -> {
            if (GameVariableNames.multiplayerGameInProgress) {
                if(isServer) {
                    playerComponent1.moveRight();
                } else {
                    playerComponent2.moveRight();
                }
            }else if (!GameVariableNames.multiplayerGameWaiting) {
                playerComponent1.moveRight();
            }
        });

        onKey(KeyCode.LEFT, () -> {
            if (GameVariableNames.multiplayerGameInProgress) {
                if(isServer) {
                    playerComponent1.moveLeft();
                } else {
                    playerComponent2.moveLeft();
                }
            }else if (!GameVariableNames.multiplayerGameWaiting) {
                playerComponent1.moveLeft();
            }
        });

        onKey(KeyCode.SPACE, () -> {
            if (GameMode == SOLO) {
                playerComponent1.shoot();
            } else if (GameVariableNames.multiplayerGameInProgress){
                onShootBroadcastLogic();
                if(isServer){
                    playerComponent1.shoot();
                }else {
                    playerComponent2.shoot();
                }
            } else if (!GameVariableNames.multiplayerGameWaiting) {
                playerComponent2.shoot();
            }
        });

        onKey(KeyCode.D, () -> {
            if (GameMode == SOLO) {
                playerComponent1.moveRight();
            } else if (GameVariableNames.multiplayerGameInProgress) {
                if(isServer) {
                    playerComponent1.moveRight();
                } else {
                    playerComponent2.moveRight();
                }
            }else if (!GameVariableNames.multiplayerGameWaiting) {
                playerComponent2.moveRight();
            }
        });

        onKey(KeyCode.Q, () -> {
            if (GameMode == SOLO) {
                playerComponent1.moveLeft();
            } else if (GameVariableNames.multiplayerGameInProgress) {
                if(isServer) {
                    playerComponent1.moveLeft();
                } else {
                    playerComponent2.moveLeft();
                }
            } else if (!GameVariableNames.multiplayerGameWaiting) {
                playerComponent2.moveLeft();
            }
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
        GameVariableNames.multiplayerGameInProgress= false;
        GameVariableNames.multiplayerGameWaiting= false;
    }

    /**
     * Initialisation du jeu
     * Coordonnées des entités et début du fond sonore
     */
    @Override
    protected void initGame() {
        play(assetNames.sounds.START_CLAIRON);
        getGameWorld().addEntityFactory(new SpaceInvadersFactory());

        if(GameMode == MULTI) { // LBF : dans le mode multijoueur
            isServer();
            }

        player1 = spawn(entityNames.PLAYER); // LBF : dans tous les modes de jeu
        player1.setX(Constant.GAME_WIDTH / 2);
        player1.setY(Constant.GAME_HEIGHT - player1.getHeight());
        playerComponent1 = player1.getComponent(PlayerComponent.class);
        playerComponent1.setDirection(Constant.Direction.UP);
        playerComponent1.initializeScore();
        playerComponent1.initializeLife();

        if (GameMode != SOLO) { // LBF : dans tous les modes de jeu sauf solo
            player2 = spawn(entityNames.PLAYER);
            player2.setX(Constant.GAME_WIDTH / 2);
            player2.setY(0);
            playerComponent2 = player2.getComponent(PlayerComponent.class);
            playerComponent2.setDirection(Constant.Direction.DOWN);
            playerComponent2.initializeScore();
            playerComponent2.initializeLife();
        }

        if (GameMode == INFINITY_MODE) { // LBF : dans le mode infinity
            Entity alien1 = spawn(entityNames.ALIEN, 0, Constant.GAME_HEIGHT / 2 - Constant.ALIEN_HEIGHT);
            alien1.getComponent(AlienComponent.class).initialize(Constant.Direction.UP);
            Entity alien2 = spawn(entityNames.ALIEN, 0, Constant.GAME_HEIGHT / 2 - Constant.ALIEN_HEIGHT);
            alien2.getComponent(AlienComponent.class).initialize(Constant.Direction.DOWN);
            run(() -> {
                Entity alien = spawn(entityNames.ALIEN, 0, Constant.GAME_HEIGHT / 2 - Constant.ALIEN_HEIGHT);
                alien.getComponent(AlienComponent.class).initialize(Constant.Direction.UP);
            }, Duration.seconds(1.4));
            run(() -> {
                Entity alien = spawn(entityNames.ALIEN, 0, Constant.GAME_HEIGHT / 2 - Constant.ALIEN_HEIGHT);
                alien.getComponent(AlienComponent.class).initialize(Constant.Direction.DOWN);
            }, Duration.seconds(1.5));

        } else if (GameMode == CLASSIQUE) { // LBF : dans le mode classique
            makeAlienBlock();
        } else if (GameMode == SOLO) { // LBF : dans le mode solo
            makeAlienBlockSolo();
        }

        spawn(entityNames.BACKGROUND);
        loopBGM(assetNames.music.MUSIC_ACROSS_THE_UNIVERSE);
    }

    /**
     * Input box pour le choix héberger/rejoindre une partie multijoueur
     */
    private void isServer(){ // LBF : dans le mode multijoueur
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
            return null;
        },Duration.seconds(0));
    }

    /**
     * Initialisation du serveur
     */
    private void isServer_ServerInit(){
        System.out.println("server");
        server = getNetService().newTCPServer(Port);
        onReceiveMessageServer();
        server.startAsync();
        GameVariableNames.multiplayerGameWaiting = true;
    }

    /**
     * Initialisation du client
     */
    private void isServer_ClientInit(){
        System.out.println("client");
        client = getNetService().newTCPClient(IP, Port);
        onReceiveMessageClient();
        client.connectAsync();
        GameVariableNames.multiplayerGameWaiting = true;
    }

    private void PortSelection(){
        runOnce(() -> {
            getDialogService().showInputBox("Entrez le port (ex:55555)", port -> {
                if (port != null) {
                    Port = Integer.parseInt(port);
                }
                else {
                    Port = 55555;
                }
                System.out.println("Port : " + Port);
                if(!isServer){
                    IPSelection();
                }
                else{
                    isServer_ServerInit();
                }
            });
            return null;
        },Duration.seconds(0));
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
            return null;
        }, Duration.seconds(0));
    }

    private void startMultiGame(){
        long startGameTime = System.currentTimeMillis();
        System.out.println("startGameTime : " + startGameTime);
        makeAlienBlock();
    }
    /**
     * Logique d'envoi des données à chaque frame
     */
    private void onUpdateBroadcastLogic(){// LBF : dans le mode multijoueur
        if (isServer) {
            Player1Broadcast();
        } else {
            Player2Broadcast();
        } //Ne sert à rien pour le moment mais utile si plus de choses à transférer à chaque frame
    }

    /**
     * Envoie des données du joueur 1 du serveur vers le client
     */
    private void Player1Broadcast(){ // LBF : dans le mode multijoueur
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
    private void Player2Broadcast(){ // LBF : dans le mode multijoueur
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
                    if(message.get("type").equals("Game Over")){
                        gameOverScreen();
                    } else if(message.get("type").equals("Game Win")){
                        winScreen();
                    }
                }else{
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
                    } else{
                        Entity alien = spawn(entityNames.ALIEN, 0, Constant.GAME_HEIGHT / 2 - Constant.ALIEN_HEIGHT);
                        alien.getComponent(AlienComponent.class).initialize(Constant.Direction.UP);
                    }
                } else if (message.getName().equals("Server Start")) {
                    GameVariableNames.multiplayerGameInProgress = true;
                    startMultiGame();
                } else if (message.getName().equals("Game End")) {
                    System.out.println("Game End Received");
                    if(message.get("type").equals("Game Over")){
                        gameOverScreen();
                    } else if(message.get("type").equals("Game Win")){
                        winScreen();
                    }
                } else{
                    System.out.println("Message non reconnu");
                }
            });
        });
    }

    /**
     * Logique d'envoi des données lors du tir d'un joueur
     */
    private void onShootBroadcastLogic(){ // LBF : dans le mode multijoueur
        if (isServer) {
            server.broadcast(new Bundle("Player1Shoot"));
        } else {
            client.broadcast(new Bundle("Player2Shoot"));
        }
    }



    private void makeAlienBlock() {
        for (int i = 0; i < 2; i++) {
            makeAlienLine(i, Constant.Direction.DOWN);
            makeAlienLine(i, Constant.Direction.UP);
        }
    }

    private void makeAlienLine(int line, Constant.Direction direction) {
        for (int i = 0; i < Constant.ALIENS_NUMBER; i++) {
            if (direction == Constant.Direction.DOWN) {
                Entity alien = spawn(entityNames.ALIEN, i * Constant.ALIEN_WIDTH,
                        Constant.GAME_HEIGHT / 2 + (line - 1) * Constant.ALIEN_HEIGHT);
                alien.getComponent(AlienComponent.class).initialize(direction);
                alien.getComponent(AlienComponent.class).setAlienNumber(i);
            } else {
                Entity alien = spawn(entityNames.ALIEN, i * Constant.ALIEN_WIDTH,
                        Constant.GAME_HEIGHT / 2 + (line - 2) * Constant.ALIEN_HEIGHT);
                alien.getComponent(AlienComponent.class).initialize(direction);
                alien.getComponent(AlienComponent.class).setAlienNumber(i);
            }
        }
    }

    private void makeAlienBlockSolo() {
        for (int line = 0; line < 4; line++) {
            for (int k = 0; k < Constant.ALIENS_NUMBER; k++) {
                Entity alien = spawn(entityNames.ALIEN, k * Constant.ALIEN_WIDTH, (line - 1) * Constant.ALIEN_HEIGHT);
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
    protected void onUpdate(double tpf ) {
        if(GameMode == MULTI){
            onUpdateMultiplayer(tpf);
        } else{
            onUpdateCommon(tpf);
        }
    }
    private void onUpdateMultiplayer(double tpf){ // LBF : dans le mode multijoueur
        if (GameVariableNames.multiplayerGameInProgress) { // LBF : dans le mode multijoueur ou réecrire autrement??
            onUpdateBroadcastLogic();
            onUpdateCommon(tpf);
        } else {
            //Synchronise le début de la partie entre les deux joueurs
            if(!isServer && GameVariableNames.multiplayerGameWaiting){
                client.broadcast(new Bundle("Client Connected"));
            }
        }
    }
    private void onUpdateCommon(double tpf) { // LBF : dans tous les modes de jeu
        if (getb(GameVariableNames.isGameOver)){
            GameEndBroadcastLogic("Game Over");
            gameOverScreen();
        }
        if (getb(GameVariableNames.isGameWon)) {
            GameEndBroadcastLogic("Game Win");
            winScreen();
        }
        if ((System.currentTimeMillis() - last_ambient_sound) > delay_ambient_sound) {
            ambientSound();
            last_ambient_sound = System.currentTimeMillis();
            delay_ambient_sound = FXGLMath.random(Constant.AMBIENT_SOUND_DELAY_MIN, Constant.AMBIENT_SOUND_DELAY_MAX);
        }
        if (getGameScene().getContentRoot().getChildren().contains(playersUI))
            showPlayersLivesAndScores();

        run(() -> {
            getGameWorld().getEntitiesByType(EntityType.ALIEN).forEach((alien) -> {
                if (FXGLMath.randomBoolean(0.005)) { // LBF : dans le mode multijoeur (tir unidirectionnel)
                    if (isServer && alien.getComponent(AlienComponent.class).getDirection() == Constant.Direction.DOWN) {
                        alien.getComponent(AlienComponent.class).randomShoot(Constant.ALIEN_SHOOT_CHANCE);
                    } else if (!isServer && alien.getComponent(AlienComponent.class).getDirection() == Constant.Direction.UP) {
                        alien.getComponent(AlienComponent.class).randomShoot(Constant.ALIEN_SHOOT_CHANCE);
                    }
                }
            });
        }, Duration.seconds(Constant.random.nextDouble() * 10));
    }

    private void GameEndBroadcastLogic(String message){
        Bundle bundle = new Bundle("Game End");
        bundle.put("type", message);
        if(isServer){
            server.broadcast(bundle);
        } else {
            client.broadcast(bundle);
        }
    }

    /**
     * Affichage de l'écran de fin de partie
     */
    private void gameOverScreen() {
        play(assetNames.sounds.DEFEAT_CLAIRON);
        String message = "Game Over ! \n Scores are as follows : \n" +
                "Player 1 : " + playerComponent1.getScore() + "\n";
        if (playerComponent2 != null ) {
            String player2 = "Player 2 : " + playerComponent2.getScore();
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
    private void winScreen() {
        play(assetNames.sounds.VICTORY_CLAIRON);
        String message = "You won ! \n Scores are as follows : \n" +
                "Player 1 : " + playerComponent1.getScore() + "\n";
        if (playerComponent2 != null) {
            String player2 = "Player 2 : " + playerComponent2.getScore();
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
                .get(FXGLMath.random(0, Constant.NUMBER_OF_AMBIENT_SOUND - 1));
        play(ambientMusic);
    }

    public static void main(String[] args) {
        launch(args);
    }
}