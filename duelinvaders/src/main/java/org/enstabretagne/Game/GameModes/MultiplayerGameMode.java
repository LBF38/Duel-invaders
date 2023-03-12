package org.enstabretagne.Game.GameModes;

import static com.almasb.fxgl.dsl.FXGL.*;
import static org.enstabretagne.UI.UI_Factory.*;
import static org.enstabretagne.UI.UI_Factory.winScreen;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.enstabretagne.Component.AlienComponent;
import org.enstabretagne.Component.PlayerComponent;
import org.enstabretagne.Utils.EntityType;
import org.enstabretagne.Utils.GameVariableNames;
import org.enstabretagne.Utils.Settings;
import org.enstabretagne.Utils.entityNames;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.logging.Logger;
import com.almasb.fxgl.net.Client;
import com.almasb.fxgl.net.Server;

import javafx.animation.PauseTransition;
import javafx.util.Duration;

public class MultiplayerGameMode extends TwoPlayerGameMode {
    private enum DialogType {
        SERVER_CHOICE, PORT_CHOICE, IP_CHOICE
    }

    private class BundleKey {
        public static final String TYPE = "type";
        public static final String X = "x";
        public static final String Y = "y";
        public static final String SCORE = "score";
        public static final String LIFE = "life";
    }

    private boolean isServer = false;
    private Server<Bundle> server;
    private Client<Bundle> client;

    private int serverPort = 55555;
    private String serverIPaddress = "localhost";

    @Override
    public void initGameMode() {
        super.initTwoPlayerGameMode();
        dialogQueue().play();
    }

    @Override
    public GameModeTypes getGameModeType() {
        return GameModeTypes.MULTIPLAYER;
    }

    @Override
    public PlayerComponent getPlayerComponent2() {
        // Même logique car mode multijoueur
        return getPlayerComponent1();
    }

    @Override
    public PlayerComponent getPlayerComponent1() {
        if (!GameVariableNames.multiplayerGameInProgress) {
            // Pour éviter d'autoriser le joueur 2 à jouer avant le début de la partie
            // TODO: à revoir
        }
        if (GameVariableNames.isShooting) {
            onShootBroadcastLogic();
        }
        if (isServer) {
            return playerComponent1;
        } else {
            return playerComponent2;
        }
    }

    @Override
    public void onUpdate(double tpf) {
        if (GameVariableNames.multiplayerGameInProgress) {
            onUpdateBroadcastLogic();
            super.onUpdate(tpf);
            aliensShootInPlayersDirection();
        } else {
            // Synchronise le début de la partie entre les deux joueurs
            if (!isServer && GameVariableNames.multiplayerGameWaiting) {
                client.broadcast(new Bundle("Client Connected"));
            }
        }
    }

    @Override
    public void gameFinished() {
        if (getb(GameVariableNames.isGameOver)) {
            GameEndBroadcastLogic("Game Over");
            gameOverScreen("");
        }
        if (getb(GameVariableNames.isGameWon)) {
            GameEndBroadcastLogic("Game Win");
            winScreen("");
        }
    }

    private void aliensShootInPlayersDirection() {
        run(() -> {
            getGameWorld().getEntitiesByType(EntityType.ALIEN).forEach((alien) -> {
                if (FXGLMath.randomBoolean(0.005)) {
                    AlienComponent alienComponent = alien.getComponent(AlienComponent.class);
                    if (isServer
                            && alienComponent.getDirection() == Settings.Direction.DOWN) {
                        alienComponent.randomShoot(Settings.ALIEN_SHOOT_CHANCE);
                    } else if (!isServer
                            && alienComponent.getDirection() == Settings.Direction.UP) {
                        alienComponent.randomShoot(Settings.ALIEN_SHOOT_CHANCE);
                    }
                }
            });
        }, Duration.seconds(Settings.random.nextDouble() * 10));
    }

    private void GameEndBroadcastLogic(String message) {
        Bundle bundle = new Bundle("Game End");
        bundle.put("type", message);
        if (isServer) {
            server.broadcast(bundle);
        } else {
            client.broadcast(bundle);
        }
    }

    private PauseTransition dialogQueue() {
        Map<DialogType, String> dialogQueueMessages = new HashMap<>();
        dialogQueueMessages.put(DialogType.SERVER_CHOICE, "Voulez-vous être le serveur ?");
        dialogQueueMessages.put(DialogType.PORT_CHOICE, "Entrez le port (ex:55555)");
        dialogQueueMessages.put(DialogType.IP_CHOICE,
                "Entrez l'adresse IP du serveur (format:255.255.255.255 ou localhost)");
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(event -> {
            showConfirm(dialogQueueMessages.get(DialogType.SERVER_CHOICE),
                    (yes) -> {
                        areYouTheServer(yes);
                        showPortInput(dialogQueueMessages);
                    });
        });
        return pause;
    }

    /**
     * Choix utilisateur pour héberger/rejoindre une partie multijoueur
     */
    private Boolean areYouTheServer(Boolean yes) {
        isServer = yes;
        Logger.get(getClass()).info("Are you the server ? : " + isServer);
        return isServer;
    }

    private void showPortInput(Map<DialogType, String> dialogQueueMessages) {
        getDialogService().showInputBox(dialogQueueMessages.get(DialogType.PORT_CHOICE), (port) -> {
            selectPort(port);
            if (isServer) {
                initializePlayers();
                return;
            }
            getDialogService().showInputBox(dialogQueueMessages.get(DialogType.IP_CHOICE),
                    (IPaddress) -> {
                        selectIPaddress(IPaddress);
                        Logger.get(getClass()).info("IP : " + serverIPaddress);
                        initializePlayers();
                    });
        });
    }

    /**
     * Choix du port du serveur
     */
    private void selectPort(String port) {
        try {
            serverPort = Integer.parseInt(port);
        } catch (Exception e) {
            serverPort = 55555;
        }
        Logger.get(getClass()).info("Port : " + serverPort);
    }

    /**
     * Input box pour la sélection de l'adresse IP du serveur
     */
    private void selectIPaddress(String IPaddress) {
        // Regex expression for validating IPv4
        final String regex_ipv4 = "^\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3}$";
        // Regex expression for validating IPv6
        final String regex_ipv6 = "^([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$";
        Pattern pattern_ipv4 = Pattern.compile(regex_ipv4);
        Pattern pattern_ipv6 = Pattern.compile(regex_ipv6);
        if (!pattern_ipv4.matcher(IPaddress).matches() && !pattern_ipv6.matcher(IPaddress).matches()) {
            serverIPaddress = "localhost";
            return;
        }
        serverIPaddress = IPaddress;
    }

    private void initializePlayers() {
        if (isServer) {
            initializeServer();
            return;
        }
        initializeClient();
    }

    /**
     * Initialisation du serveur
     */
    private void initializeServer() {
        Logger.get(getClass()).info("Server starting...");
        getNotificationService().pushNotification("You are the server on the following address : " + serverIPaddress
                + " on port : " + serverPort);
        server = getNetService().newTCPServer(serverPort);
        onReceiveMessageServer();
        server.startAsync();
        GameVariableNames.multiplayerGameWaiting = true;
        Logger.get(getClass()).info("Server started on port : " + serverPort);
    }

    /**
     * Logique lors de la réception des données du client par le serveur
     */
    private void onReceiveMessageServer() { // TODO: refactor this
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
                        gameOverScreen(playerComponent1.getScore(), playerComponent2.getScore());
                    } else if (message.get("type").equals("Game Win")) {
                        winScreen(playerComponent1.getScore(), playerComponent2.getScore());
                    }
                } else {
                    System.out.println("Message non reconnu");
                }
            });
        });
    }

    /**
     * Initialisation du client
     */
    private void initializeClient() {
        Logger.get(getClass()).info("Client starting...");
        getNotificationService()
                .pushNotification("You are connecting to the server on the following address : " + serverIPaddress
                        + " on port : " + serverPort);
        client = getNetService().newTCPClient(serverIPaddress, serverPort);
        onReceiveMessageClient();
        client.connectAsync();
        GameVariableNames.multiplayerGameWaiting = true;
        Logger.get(getClass()).info("Client connected to server !");
    }

    /**
     * Logique lors de la réception des données du serveur par le client
     */
    private void onReceiveMessageClient() {// TODO: refactor this
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
                    if (message.get("direction") == Settings.Direction.DOWN) {
                        Entity alien = spawn(entityNames.ALIEN, 0, Settings.GAME_HEIGHT / 2 - Settings.ALIEN_HEIGHT);
                        alien.getComponent(AlienComponent.class).initialize(Settings.Direction.DOWN);
                    } else {
                        Entity alien = spawn(entityNames.ALIEN, 0, Settings.GAME_HEIGHT / 2 - Settings.ALIEN_HEIGHT);
                        alien.getComponent(AlienComponent.class).initialize(Settings.Direction.UP);
                    }
                } else if (message.getName().equals("Server Start")) {
                    GameVariableNames.multiplayerGameInProgress = true;
                    startMultiGame();
                } else if (message.getName().equals("Game End")) {
                    System.out.println("Game End Received");
                    if (message.get("type").equals("Game Over")) {
                        gameOverScreen(playerComponent1.getScore(), playerComponent2.getScore());
                    } else if (message.get("type").equals("Game Win")) {
                        winScreen(playerComponent1.getScore(), playerComponent2.getScore());
                    }
                } else {
                    System.out.println("Message non reconnu");
                }
            });
        });
    }

    /**
     * Logique d'envoi des données à chaque frame
     */
    private void onUpdateBroadcastLogic() {
        if (isServer) {
            Bundle bundle = createPlayerInfosBundle(player1, playerComponent1, "Player1", "Player1");
            server.broadcast(bundle);
        } else {
            Bundle bundle = createPlayerInfosBundle(player2, playerComponent2, "Player2", "Player2");
            client.broadcast(bundle);
        }
    }

    /**
     * Création d'un bundle contenant les données du joueur
     * 
     * Informations transmises :
     * <ul>
     * <li>type : type de joueur</li>
     * <li>x : position x du joueur</li>
     * <li>y : position y du joueur</li>
     * <li>score : score du joueur</li>
     * <li>life : vie du joueur</li>
     * </ul>
     * 
     * @param player
     * @param playerComponent
     * @param bundleName
     * @param bundleType
     * @return bundle contenant les données du joueur
     * 
     * @author LBF38
     */
    private Bundle createPlayerInfosBundle(Entity player, PlayerComponent playerComponent, String bundleName,
            String bundleType) {
        Bundle bundle = new Bundle(bundleName);
        bundle.put(BundleKey.TYPE, bundleType);
        bundle.put(BundleKey.X, player.getX());
        bundle.put(BundleKey.Y, player.getY());
        bundle.put(BundleKey.SCORE, playerComponent.getScore());
        bundle.put(BundleKey.LIFE, playerComponent.getLife());
        return bundle;
    }

    private void startMultiGame() {
        long startGameTime = System.currentTimeMillis();
        Logger.get(getClass()).info("startGameTime : " + startGameTime);
        AlienFactory.makeAlienBlock();
    }

    /**
     * Logique d'envoi des données lors du tir d'un joueur
     */
    private void onShootBroadcastLogic() {
        if (isServer) {
            server.broadcast(new Bundle("Player1Shoot"));
        } else {
            client.broadcast(new Bundle("Player2Shoot"));
        }
    }

}
