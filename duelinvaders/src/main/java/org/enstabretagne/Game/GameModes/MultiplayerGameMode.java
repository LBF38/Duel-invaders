package org.enstabretagne.Game.GameModes;

import static com.almasb.fxgl.dsl.FXGL.getDialogService;
import static com.almasb.fxgl.dsl.FXGL.getNetService;
import static com.almasb.fxgl.dsl.FXGL.runOnce;
import static com.almasb.fxgl.dsl.FXGL.spawn;
import static org.enstabretagne.UI.UI_Factory.gameOverScreen;
import static org.enstabretagne.UI.UI_Factory.winScreen;

import org.enstabretagne.Component.AlienComponent;
import org.enstabretagne.Utils.GameVariableNames;
import org.enstabretagne.Utils.Settings;
import org.enstabretagne.Utils.entityNames;

import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.net.Client;
import com.almasb.fxgl.net.Server;

import javafx.util.Duration;

public class MultiplayerGameMode extends TwoPlayerGameMode {
    private boolean isServer;
    private Server<Bundle> server;
    private Client<Bundle> client;

    private boolean alienSpawnStart = false; // indique si les aliens ont commencé à spawn

    private int Port;
    private String IP;

    @Override
    public void initGameMode() {
        super.initTwoPlayerGameMode();
    }

    @Override
    public GameModeTypes getGameModeType() {
        return GameModeTypes.MULTIPLAYER;
    }

    /**
     * Input box pour le choix héberger/rejoindre une partie multijoueur
     */
    private void isServer() {
        getDialogService().showConfirmationBox("Voulez-vous être le serveur ?", yes -> {
            if (yes) {
                isServer = true;
                PortSelection();
            } else {
                isServer = false;
                PortSelection();
            }
        });
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
    private void onShootBroadcastLogic() { // LBF : dans le mode multijoueur
        if (isServer) {
            server.broadcast(new Bundle("Player1Shoot"));
        } else {
            client.broadcast(new Bundle("Player2Shoot"));
        }
    }

}
