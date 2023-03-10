package org.enstabretagne.UI;

import static com.almasb.fxgl.dsl.FXGL.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.enstabretagne.Component.PlayerComponent;
import org.enstabretagne.Utils.EntityType;
import org.enstabretagne.Utils.Settings;
import org.enstabretagne.Utils.assetNames;

import com.almasb.fxgl.app.scene.GameScene;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.GameWorld;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class UI_Factory {
    private static VBox playersUI = new VBox();

    public static VBox showPlayersLivesAndScores(GameWorld gameWorld,GameScene gameScene) {
        if (getGameScene().getContentRoot().getChildren().contains(playersUI))
            getGameScene().removeChild(playersUI);
        // System.out.println("Players Lives and Scores");

        List<HBox> playersViews = new ArrayList<>();
        List<PlayerComponent> players = gameWorld.getEntitiesByType(EntityType.PLAYER).stream()
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
        return playersUI;
    }

    public static HBox createScoreUI(int score, int player_id) {
        Text scoreText = getUIFactoryService().newText(Integer.toString(score), Color.WHITE, 24.0);
        Text playerText = getUIFactoryService().newText("Player " + Integer.toString(player_id), Color.WHITE,
                24.0);
        var scoreView = new HBox(10, playerText, scoreText);
        scoreView.setAlignment(Pos.CENTER);
        return scoreView;
    }

    public static HBox createLifeUI(int life) {
        var lifeTexture = texture(assetNames.textures.LIFE, 30, 30);
        var lifeView = new HBox(10);
        for (int i = 0; i < life; i++) {
            lifeView.getChildren().add(lifeTexture.copy());
        }
        lifeView.setAlignment(Pos.CENTER);
        return lifeView;
    }

    /**
     * Affichage de l'écran pour jouer une nouvelle partie
     */
    public static void playAgain(Boolean yes) {
        if (yes)
            getGameController().startNewGame();
        else
            getGameController().gotoMainMenu();
    }

    /**
     * Affichage de l'écran de victoire
     */
    public static void winScreen(String score_player1, String score_player2) {
        play(assetNames.sounds.VICTORY_CLAIRON);
        String message = "You won ! \nScores are as follows : \n" +
                "Player 1 : " + score_player1 + "\n";
        if (score_player2 != null) {
            String player2 = "Player 2 : " + score_player2;
            message += player2;
        }
        askConfirmationToUser(message, (yes) -> playAgain(yes));
    }

    private static void askConfirmationToUser(String message, Consumer<Boolean> responseCallback) {
        getDialogService().showMessageBox(message, () -> {
            getDialogService().showConfirmationBox("Do you want to play again?", responseCallback);
        });
    }

    public static void winScreen(String score_player1) {
        winScreen(score_player1, null);
    }

    public static void winScreen(int score_player1, int score_player2) {
        winScreen(Integer.toString(score_player1), Integer.toString(score_player2));
    }

    /**
     * Affichage de l'écran de fin de partie
     */
    public static void gameOverScreen(String score_player1, String score_player2) {
        play(assetNames.sounds.DEFEAT_CLAIRON);
        String message = "Game Over ! \nScores are as follows : \n" +
                "Player 1 : " + score_player1 + "\n";
        if (score_player2 != null) {
            String player2 = "Player 2 : " + score_player2;
            message += player2;
        }
        askConfirmationToUser(message, (yes) -> playAgain(yes));
    }

    public static void gameOverScreen(String score_player1) {
        gameOverScreen(score_player1, null);
    }

    public static void gameOverScreen(int score_player1, int score_player2) {
        gameOverScreen(Integer.toString(score_player1), Integer.toString(score_player2));
    }

    /**
     * Joue un son d'ambiance aléatoire parmi ceux disponibles
     */
    public static void ambientSound() {
        String ambientMusic = assetNames.sounds.AMBIENT_SOUNDS
                .get(FXGLMath.random(0, Settings.NUMBER_OF_AMBIENT_SOUND - 1));
        play(ambientMusic);
    }
}
