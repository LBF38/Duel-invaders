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
import com.almasb.fxgl.entity.component.Component;
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


    // LBF : dans le mode multi ??
    //utile pour la synchro du lancement du multijoueur
    private boolean multiplayerGameInProgress = false; // indique si la partie multijoueur est lancée ou non
    private boolean multiplayerGameWaiting = false; // indique si une partie multijoueur est en attente
    private boolean alienSpawnStart = false; // indique si les aliens ont commencé à spawn

    private double alienTag= 0; //permet de numéroter les aliens
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
            if (multiplayerGameInProgress){
                onShootBroadcastLogic();
                if(isServer){
                    playerComponent1.shoot();
                }else {
                    playerComponent2.shoot();
                }
            } else {
                playerComponent1.shoot();
            }
        });

        onKey(KeyCode.RIGHT, () -> {
            if (multiplayerGameInProgress) {
                if(isServer) {
                    playerComponent1.moveRight();
                } else {
                    playerComponent2.moveRight();
                }
            }else {
                playerComponent1.moveRight();
            }
        });

        onKey(KeyCode.LEFT, () -> {
            if (multiplayerGameInProgress) {
                if(isServer) {
                    playerComponent1.moveLeft();
                } else {
                    playerComponent2.moveLeft();
                }
            }else {
                playerComponent1.moveLeft();
            }
        });

        onKey(KeyCode.SPACE, () -> {
            if (GameMode == SOLO) {
                playerComponent1.shoot();
            } else if (multiplayerGameInProgress){
                onShootBroadcastLogic();
                if(isServer){
                    playerComponent1.shoot();
                }else {
                    playerComponent2.shoot();
                }
            } else {
                playerComponent2.shoot();
            }
        });

        onKey(KeyCode.D, () -> {
            if (GameMode == SOLO) {
                playerComponent1.moveRight();
            } else if (multiplayerGameInProgress) {
                if(isServer) {
                    playerComponent1.moveRight();
                } else {
                    playerComponent2.moveRight();
                }
            }else {
                playerComponent2.moveRight();
            }
        });

        onKey(KeyCode.Q, () -> {
            if (GameMode == SOLO) {
                playerComponent1.moveLeft();
            } else if (multiplayerGameInProgress) {
                if(isServer) {
                    playerComponent1.moveLeft();
                } else {
                    playerComponent2.moveLeft();
                }
            } else {
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

    private void isServer(){ // LBF : dans le mode multijoueur
        runOnce(() -> {
            getDialogService().showConfirmationBox("Voulez-vous être le serveur ?", yes -> {
                if (yes) {
                    isServer = true;
                    System.out.println("server");
                    server = getNetService().newTCPServer(55555); // todo -> selection du port
                    onReceiveMessageServer();
                    server.startAsync();
                    multiplayerGameWaiting = true;

                } else {
                    isServer = false;
                    System.out.println("client");
                    client = getNetService().newTCPClient("localhost", 55555); // todo -> selection du port
                    onReceiveMessageClient();
                    client.connectAsync();
                    multiplayerGameWaiting = true;
                }
            });
            return null;
        },Duration.seconds(0));

    }


    private void onUpdateLogic(){// LBF : dans le mode multijoueur
        if (isServer) {
            Player1Broadcast();
        } else {
            Player2Broadcast();
        }
    }

    /**
     * Envoie des données du serveur vers le client
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
                    server.broadcast(new Bundle("Server Start"));
                    multiplayerGameInProgress = true;

                }
            });
        });
    }

    /**
     * Envoie des données du client vers le serveur
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
                        alien.getComponent(AlienComponent.class).alien_tag = message.get("alien_tag");
                    } else{
                        Entity alien = spawn(entityNames.ALIEN, 0, Constant.GAME_HEIGHT / 2 - Constant.ALIEN_HEIGHT);
                        alien.getComponent(AlienComponent.class).initialize(Constant.Direction.UP);
                        alien.getComponent(AlienComponent.class).alien_tag = message.get("alien_tag");
                    }
                } else if (message.getName().equals("Server Start")) {
                    multiplayerGameInProgress = true;
                }
            });
        });
    }

    private void onShootBroadcastLogic(){ // LBF : dans le mode multijoueur
        if (isServer) {
            server.broadcast(new Bundle("Player1Shoot"));
        } else {
            client.broadcast(new Bundle("Player2Shoot"));
        }
    }

    private void AlienSpawnAndBroadcast(Constant.Direction direction) { // LBF : dans le mode multijoueur
        if (direction == Constant.Direction.DOWN) {
            Entity alien = spawn(entityNames.ALIEN, 0, Constant.GAME_HEIGHT / 2 - Constant.ALIEN_HEIGHT);
            alien.getComponent(AlienComponent.class).initialize(Constant.Direction.DOWN);
            alien.getComponent(AlienComponent.class).alien_tag = alienTag;
            alienTag++;
        } else{
            Entity alien = spawn(entityNames.ALIEN, 0, Constant.GAME_HEIGHT / 2 - Constant.ALIEN_HEIGHT);
            alien.getComponent(AlienComponent.class).initialize(Constant.Direction.UP);
            alien.getComponent(AlienComponent.class).alien_tag = alienTag;
            alienTag++;
        }
        Bundle bundle = new Bundle("AlienSpawn");
        bundle.put("direction", direction);
        bundle.put("alien_tag", alienTag);
        server.broadcast(bundle);
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
    protected void onUpdate(double tpf) {
        if (multiplayerGameInProgress) { // LBF : dans le mode multijoueur ou réecrire autrement??
            onUpdateLogic();
            if(!alienSpawnStart && isServer){
                run(() -> {
                    AlienSpawnAndBroadcast(Constant.Direction.UP);
                }, Duration.seconds(1.4));
                run(() -> {
                    AlienSpawnAndBroadcast(Constant.Direction.DOWN);
                }, Duration.seconds(1.5));
                alienSpawnStart = true;
            }
        } else if (multiplayerGameInProgress || GameMode !=MULTI) {
            if (getb(GameVariableNames.isGameOver))
                gameOverScreen();
            if (getb(GameVariableNames.isGameWon))
                winScreen();

            if ((System.currentTimeMillis() - last_ambient_sound) > delay_ambient_sound) {
                ambientSound();
                last_ambient_sound = System.currentTimeMillis();
                delay_ambient_sound = FXGLMath.random(Constant.AMBIENT_SOUND_DELAY_MIN, Constant.AMBIENT_SOUND_DELAY_MAX);
            }
            if (getGameScene().getContentRoot().getChildren().contains(playersUI))
                showPlayersLivesAndScores();

            run(() -> {
                getGameWorld().getEntitiesByType(EntityType.ALIEN).forEach((alien) -> {
                    if (FXGLMath.randomBoolean(0.005)) {
                        if (isServer && alien.getComponent(AlienComponent.class).getDirection() == Constant.Direction.DOWN) {
                            alien.getComponent(AlienComponent.class).randomShoot(Constant.ALIEN_SHOOT_CHANCE);
                        } else if (!isServer && alien.getComponent(AlienComponent.class).getDirection() == Constant.Direction.UP) {
                            alien.getComponent(AlienComponent.class).randomShoot(Constant.ALIEN_SHOOT_CHANCE);
                        }
                    }
                });
            }, Duration.seconds(Constant.random.nextDouble() * 10));
        } else {
            //Synchronise le début de la partie entre les deux joueurs
            if(!isServer && multiplayerGameWaiting){
                client.broadcast(new Bundle("Client Connected"));
            }
        }

    }

    /**
     * Affichage de l'écran de fin de partie
     */
    private void gameOverScreen() {
        play(assetNames.sounds.DEFEAT_CLAIRON);
        String message = "Game Over ! \n Scores are as follows : \n" +
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