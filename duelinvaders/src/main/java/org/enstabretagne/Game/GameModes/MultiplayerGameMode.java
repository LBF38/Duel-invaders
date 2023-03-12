package org.enstabretagne.Game.GameModes;

import static com.almasb.fxgl.dsl.FXGL.getDialogService;
import static com.almasb.fxgl.dsl.FXGL.getGameController;
import static com.almasb.fxgl.dsl.FXGL.getNetService;
import static com.almasb.fxgl.dsl.FXGL.runOnce;
import static com.almasb.fxgl.dsl.FXGL.*;
import static org.enstabretagne.UI.UI_Factory.gameOverScreen;
import static org.enstabretagne.UI.UI_Factory.winScreen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Pattern;

import javax.naming.LimitExceededException;

import org.enstabretagne.Component.AlienComponent;
import org.enstabretagne.Component.PlayerComponent;
import org.enstabretagne.Utils.GameVariableNames;
import org.enstabretagne.Utils.Settings;
import org.enstabretagne.Utils.entityNames;

import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.logging.Logger;
import com.almasb.fxgl.net.Client;
import com.almasb.fxgl.net.Server;
import com.almasb.fxgl.time.TimerAction;

import javafx.animation.PauseTransition;
import javafx.util.Duration;

public class MultiplayerGameMode extends TwoPlayerGameMode {
    private boolean isServer = false;
    private boolean hasChosen = false;
    private Server<Bundle> server;
    private Client<Bundle> client;

    private boolean alienSpawnStart = false; // indique si les aliens ont commencé à spawn

    private int serverPort = 55555;
    private String serverIPaddress = "localhost";

    @Override
    public void initGameMode() {
        super.initTwoPlayerGameMode();

        List<PauseTransition> dialogQueue = dialogQueue();
        PauseTransition dialogPauseTransition = new PauseTransition(Duration.seconds(1));
        dialogPauseTransition.setOnFinished(event -> {
            if (dialogQueue.size() > 0) {
                dialogQueue.get(0).play();
                dialogQueue.remove(0);
                dialogPauseTransition.playFromStart();
            }
        });
        dialogPauseTransition.play();
    }

    private enum DialogType {
        SERVER_CHOICE, PORT_CHOICE, IP_CHOICE
    }

    private List<PauseTransition> dialogQueue() {
        Map<DialogType, String> dialogQueueMessages = new HashMap<>();
        dialogQueueMessages.put(DialogType.SERVER_CHOICE, "Voulez-vous être le serveur ?");
        dialogQueueMessages.put(DialogType.PORT_CHOICE, "Entrez le port (ex:55555)");
        dialogQueueMessages.put(DialogType.IP_CHOICE, "Entrez l'adresse IP du serveur (format:255.255.255.255)");
        Map<DialogType, Consumer<String>> dialogQueueStringActions = new HashMap<>();
        Map<DialogType, Consumer<Boolean>> dialogQueueBooleanActions = new HashMap<>();
        dialogQueueStringActions.put(DialogType.PORT_CHOICE, port -> {
            try {
                serverPort = Integer.parseInt(port);
            } catch (Exception e) {
                serverPort = 55555;
            }
            Logger.get(getClass()).info("Port : " + serverPort);
        });
        dialogQueueStringActions.put(DialogType.IP_CHOICE, ip -> {
            serverIPaddress = ip;
            Logger.get(getClass()).info("IP address : " + serverIPaddress);
        });
        dialogQueueBooleanActions.put(DialogType.SERVER_CHOICE, yes -> {
            isServer = yes;
            Logger.get(getClass()).info("Are you the server ? : " + isServer);
        });
        return pauseDialogQueue(dialogQueueMessages, dialogQueueStringActions, dialogQueueBooleanActions);
    }

    private List<PauseTransition> pauseDialogQueue(Map<DialogType, String> dialogQueueMessages,
            Map<DialogType, Consumer<String>> dialogQueueStringActions,
            Map<DialogType, Consumer<Boolean>> dialogQueueBooleanActions) {
        List<PauseTransition> pauses = new ArrayList<>();
        for (DialogType dialogType : dialogQueueMessages.keySet()) {
            PauseTransition pause = new PauseTransition(Duration.seconds(1));
            pause.setOnFinished(event -> {
                if (dialogQueueBooleanActions.containsKey(dialogType)) {
                    getDialogService().showConfirmationBox(dialogQueueMessages.get(dialogType),
                            dialogQueueBooleanActions.get(dialogType));
                } else if (dialogQueueStringActions.containsKey(dialogType)) {
                    getDialogService().showInputBox(dialogQueueMessages.get(dialogType),
                            dialogQueueStringActions.get(dialogType));
                }
            });
            pauses.add(pause);
        }
        return pauses;
    }

    @Override
    public GameModeTypes getGameModeType() {
        return GameModeTypes.MULTIPLAYER;
    }

    @Override
    public PlayerComponent getPlayerComponent2() {
        if (isServer) {
            return playerComponent1;
        } else {
            return playerComponent2;
        }
    }

    @Override
    public PlayerComponent getPlayerComponent1() {
        if (isServer) {
            return playerComponent1;
        } else {
            return playerComponent2;
        }
    }

    @Override
    public void onUpdate(double tpf) {
        // TODO: make the game logic too.
    }

    @Override
    public void gameFinished() {
        // TODO: clean server/client connection.
        super.gameFinished();
    }

    private void initializePlayers() {
        if (isServer) {
            initializeServer();
            return;
        }
        selectIPaddress();
        initializeClient();
    }

    /**
     * Input box pour le choix héberger/rejoindre une partie multijoueur
     */
    private TimerAction areYouTheServer() {
        // On attend 1 seconde pour que le joueur puisse choisir si il est le serveur ou
        // le client
        // Cela permet au jeu de s'initialiser avant de demander à l'utilisateur
        return runOnce(() -> {
            getDialogService().showConfirmationBox("Voulez-vous être le serveur ?", yes -> {
                isServer = yes;
                Logger.get(getClass()).info("Are you the server ? : " + isServer);
            });
        }, Duration.seconds(1));
    }

    /**
     * Input box pour le choix du port
     */
    private TimerAction selectPort() {
        return runOnce(() -> {
            getDialogService().showInputBox("Entrez le port (ex:55555)", port -> {
                try {
                    serverPort = Integer.parseInt(port);
                } catch (Exception e) {
                    serverPort = 55555;
                }
                Logger.get(getClass()).info("Port : " + serverPort);
            });
            hasChosen = true;
        }, Duration.seconds(2));
    }

    // /**
    // * Logique d'envoi des données à chaque frame
    // */
    // private void onUpdateBroadcastLogic() {
    // if (isServer) {
    // Player1Broadcast();
    // } else {
    // Player2Broadcast();
    // } // Ne sert à rien pour le moment mais utile si plus de choses à transférer
    // à
    // // chaque frame
    // }

    /**
     * Initialisation du serveur
     */
    private void initializeServer() {
        Logger.get(getClass()).info("Server starting...");
        runOnce(() -> {
            getDialogService()
                    .showMessageBox("You are the server on the following address : " + serverIPaddress
                            + " on port : " + serverPort);
        }, Duration.seconds(1));
        server = getNetService().newTCPServer(serverPort);
        onReceiveMessageServer();
        server.startAsync();
        GameVariableNames.multiplayerGameWaiting = true;
        Logger.get(getClass()).info("Server started on port : " + serverPort);
    }

    /**
     * Input box pour la sélection de l'adresse IP du serveur
     */
    private void selectIPaddress() {
        runOnce(() -> {
            getDialogService().showInputBox("Entrez l'adresse IP du serveur (ex: localhost ou 111.222.333.444)",
                    IPaddress -> {
                        // Regex expression for validating IPv4
                        String regex_ipv4 = "(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])";

                        // Regex expression for validating IPv6
                        String regex_ipv6 = "((([0-9a-fA-F]){1,4})\\:){7}([0-9a-fA-F]){1,4}";
                        Pattern pattern_ipv4 = Pattern.compile(regex_ipv4);
                        Pattern pattern_ipv6 = Pattern.compile(regex_ipv6);
                        if (!pattern_ipv4.matcher(IPaddress).matches() && !pattern_ipv6.matcher(IPaddress).matches()) {
                            serverIPaddress = "localhost";
                            return;
                        }
                        serverIPaddress = IPaddress;
                        Logger.get(getClass()).info("IP : " + serverIPaddress);
                    });

        }, Duration.seconds(3));
    }

    /**
     * Initialisation du client
     */
    private void initializeClient() {
        System.out.println("client");
        runOnce(() -> {
            getDialogService()
                    .showMessageBox("You are connecting to the server on the following address : " + serverIPaddress
                            + " on port : " + serverPort);
        }, Duration.seconds(3));
        client = getNetService().newTCPClient(serverIPaddress, serverPort);
        onReceiveMessageClient();
        client.connectAsync();
        GameVariableNames.multiplayerGameWaiting = true;
    }

    private void startMultiGame() {
        long startGameTime = System.currentTimeMillis();
        System.out.println("startGameTime : " + startGameTime);
        AlienFactory.makeAlienBlock();
    }

    /**
     * Envoie des données du joueur 1 du serveur vers le client
     */
    private void Player1Broadcast() {
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
    private void Player2Broadcast() {
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
    private void onReceiveMessageServer() {
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
     * Logique lors de la réception des données du serveur par le client
     */
    private void onReceiveMessageClient() {
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
