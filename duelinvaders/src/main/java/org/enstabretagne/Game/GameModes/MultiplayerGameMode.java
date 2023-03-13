package org.enstabretagne.Game.GameModes;

import static com.almasb.fxgl.dsl.FXGL.getDialogService;
import static com.almasb.fxgl.dsl.FXGL.getGameController;
import static com.almasb.fxgl.dsl.FXGL.getGameWorld;
import static com.almasb.fxgl.dsl.FXGL.getNetService;
import static com.almasb.fxgl.dsl.FXGL.getNotificationService;
import static com.almasb.fxgl.dsl.FXGL.getb;
import static com.almasb.fxgl.dsl.FXGL.run;
import static com.almasb.fxgl.dsl.FXGL.showConfirm;
import static org.enstabretagne.UI.UI_Factory.gameOverScreen;
import static org.enstabretagne.UI.UI_Factory.winScreen;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.enstabretagne.Component.AlienComponent;
import org.enstabretagne.Component.PlayerComponent;
import org.enstabretagne.Utils.EntityType;
import org.enstabretagne.Utils.GameVariableNames;
import org.enstabretagne.Utils.Settings;

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

    private class BundleType {
        public static final String PLAYER1 = "Player1";
        public static final String PLAYER2 = "Player2";
        public static final String PLAYER1_SHOOT = "Player1Shoot";
        public static final String PLAYER2_SHOOT = "Player2Shoot";
        public static final String CLIENT_CONNECTED = "Client Connected";
        public static final String SERVER_START = "Server Start";
        public static final String GAME_OVER = "Game Over";
        public static final String GAME_WIN = "Game Win";
    }

    private boolean isServer = false;
    private boolean newGame = true;
    private long clientConnectionAttempt = System.currentTimeMillis();
    private int clientConnectionWaitingTime = 15000;
    private Server<Bundle> server;
    private Client<Bundle> client;
    private int serverPort = 55555;
    private String serverIPaddress = "localhost";

    @Override
    public void initGameMode() {
        super.initTwoPlayerGameMode();
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
        if (GameVariableNames.isShooting && GameVariableNames.multiplayerGameInProgress) {
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
        if (newGame) {
            dialogQueue().play();
            newGame = false;
        }
        if (GameVariableNames.multiplayerGameInProgress) {
            onUpdateBroadcastLogic();
            super.onUpdate(tpf);
            aliensShootInPlayersDirection();
            return;
        }
        // Synchronise le début de la partie entre les deux joueurs
        if (!isServer && GameVariableNames.multiplayerGameWaiting) {
            client.broadcast(new Bundle(BundleType.CLIENT_CONNECTED));
            boolean isConnectionAttempt = System.currentTimeMillis()
                    - clientConnectionAttempt > clientConnectionWaitingTime;
            if (client.getConnections().size() == 0
                    && isConnectionAttempt) {
                clientConnectionAttempt = System.currentTimeMillis();
                Logger.get(getClass()).warning("Client failed to connect to server ! ");
                getNotificationService().pushNotification("Client failed to connect to server ! ");
                getDialogService().showConfirmationBox("Connection failed !\nDo you want to try again ?",
                        (yes) -> {
                            if (yes) {
                                client.connectAsync();
                                getGameController().gotoPlay();
                            } else
                                getGameController().gotoMainMenu();
                        });
            } else if (isConnectionAttempt) {
                Logger.get(getClass()).info("Client connected to server !");
                getNotificationService().pushNotification("Connected to the server !");
            }
        }
    }

    @Override
    public void gameFinished() {
        if (getb(GameVariableNames.isGameOver)) {
            GameEndBroadcastLogic(BundleType.GAME_OVER);
            gameOverScreen(playerComponent1.getScore(), playerComponent2.getScore());
        }
        if (getb(GameVariableNames.isGameWon)) {
            GameEndBroadcastLogic(BundleType.GAME_WIN);
            winScreen(playerComponent1.getScore(), playerComponent2.getScore());
        }
        newGame = true;
    }

    /**
     * Logique d'envoi des données à chaque frame
     */
    private void onUpdateBroadcastLogic() {
        if (isServer) {
            Bundle bundle = createPlayerInfosBundle(player1, playerComponent1, BundleType.PLAYER1, BundleType.PLAYER1);
            server.broadcast(bundle);
        } else {
            Bundle bundle = createPlayerInfosBundle(player2, playerComponent2, BundleType.PLAYER2, BundleType.PLAYER2);
            client.broadcast(bundle);
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

    private void GameEndBroadcastLogic(String bundleType) {
        Bundle bundle = new Bundle(bundleType);
        if (isServer) {
            server.broadcast(bundle);
            server.stop();
        } else {
            client.broadcast(bundle);
            client.disconnect();
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
        getNotificationService().pushNotification("Server started at " + serverIPaddress
                + ":" + serverPort);
        server = getNetService().newTCPServer(serverPort);
        onReceiveMessageServer();
        server.startAsync();
        GameVariableNames.multiplayerGameWaiting = true;
        Logger.get(getClass()).info("Server started on port : " + serverPort);
    }

    /**
     * Logique lors de la réception des données du client par le serveur
     */
    private void onReceiveMessageServer() {
        server.setOnConnected(connection -> {
            connection.addMessageHandlerFX((conn, message) -> {
                switch (message.getName()) {
                    case BundleType.PLAYER2:
                        updatePlayersInfos(player2, playerComponent2, message);
                        break;
                    case BundleType.PLAYER2_SHOOT:
                        playerComponent2.shoot();
                        break;
                    case BundleType.CLIENT_CONNECTED:
                        if (!GameVariableNames.multiplayerGameInProgress) {
                            server.broadcast(new Bundle(BundleType.SERVER_START));
                            GameVariableNames.multiplayerGameInProgress = true;
                            startMultiGame();
                        }
                        break;
                    case BundleType.GAME_OVER:
                        gameOverScreen(playerComponent1.getScore(), playerComponent2.getScore());
                        server.stop();
                        break;
                    case BundleType.GAME_WIN:
                        winScreen(playerComponent1.getScore(), playerComponent2.getScore());
                        server.stop();
                        break;
                    default:
                        Logger.get(getClass()).info("Server received unknown message: " + message);
                        break;
                }
            });
        });
    }

    /**
     * Update the players infos from the bundle received (client or server)
     * 
     * @param player
     * @param playerComponent
     * @param bundle
     */
    private void updatePlayersInfos(Entity player, PlayerComponent playerComponent, Bundle bundle) {
        player.setX(bundle.get(BundleKey.X));
        player.setY(bundle.get(BundleKey.Y));
        playerComponent.setScore(bundle.get(BundleKey.SCORE));
        playerComponent.setLife(bundle.get(BundleKey.LIFE));
    }

    /**
     * Initialisation du client
     */
    private void initializeClient() {
        Logger.get(getClass()).info("Client starting...");
        getNotificationService()
                .pushNotification("Connecting to the following server : " + serverIPaddress
                        + ":" + serverPort);
        client = getNetService().newTCPClient(serverIPaddress, serverPort);
        onReceiveMessageClient();
        client.connectAsync();
        GameVariableNames.multiplayerGameWaiting = true;
    }

    /**
     * Logique lors de la réception des données du serveur par le client
     */
    private void onReceiveMessageClient() {
        client.setOnConnected(connection -> {
            connection.addMessageHandlerFX((conn, message) -> {
                switch (message.getName()) {
                    case BundleType.PLAYER1:
                        updatePlayersInfos(player1, playerComponent1, message);
                        break;
                    case BundleType.PLAYER1_SHOOT:
                        playerComponent1.shoot();
                        break;
                    case BundleType.SERVER_START:
                        GameVariableNames.multiplayerGameInProgress = true;
                        startMultiGame();
                        break;
                    case BundleType.GAME_OVER:
                        gameOverScreen(playerComponent1.getScore(), playerComponent2.getScore());
                        client.disconnect();
                        break;
                    case BundleType.GAME_WIN:
                        winScreen(playerComponent1.getScore(), playerComponent2.getScore());
                        client.disconnect();
                        break;
                    default:
                        Logger.get(getClass()).info("Client received unknown message: " + message);
                        break;
                }
            });
        });
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
            server.broadcast(new Bundle(BundleType.PLAYER1_SHOOT));
        } else {
            client.broadcast(new Bundle(BundleType.PLAYER2_SHOOT));
        }
    }

}
