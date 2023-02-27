package org.enstabretagne.UI;

import static com.almasb.fxgl.dsl.FXGL.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.enstabretagne.Component.PlayerComponent;
import org.enstabretagne.Utils.EntityType;
import org.enstabretagne.Utils.Settings;
import org.enstabretagne.Utils.assetNames;

import com.almasb.fxgl.core.math.FXGLMath;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class UI_Factory {

    public static void showPlayersLivesAndScores(VBox playersUI) {
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

    public static HBox createScoreUI(int score, int player_id) {
        Text scoreText = getUIFactoryService().newText(Integer.toString(score), Color.WHITE, 24.0);
        Text playerText = getUIFactoryService().newText("Player " + Integer.toString(player_id % 2 + 1), Color.WHITE,
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
     * Affichage de l'écran de fin de partie
     */
    public static void gameOverScreen(String score_player1, String score_player2) {
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
     * Joue un son d'ambiance aléatoire parmi ceux disponibles
     */
    public static void ambientSound() {
        String ambientMusic = assetNames.sounds.AMBIENT_SOUNDS
                .get(FXGLMath.random(0, Settings.NUMBER_OF_AMBIENT_SOUND - 1));
        play(ambientMusic);
    }
}
