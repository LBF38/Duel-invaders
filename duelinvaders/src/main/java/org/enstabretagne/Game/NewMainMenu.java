package org.enstabretagne.Game;

import static com.almasb.fxgl.dsl.FXGL.getSettings;
import static com.almasb.fxgl.dsl.FXGL.getUIFactoryService;
import static com.almasb.fxgl.dsl.FXGL.getWindowService;
import static com.almasb.fxgl.dsl.FXGL.texture;

import org.enstabretagne.Game.GameModes.ClassicGameMode;
import org.enstabretagne.Game.GameModes.InfinityGameMode;
import org.enstabretagne.Game.GameModes.MusicDemoGameMode;
import org.enstabretagne.Game.GameModes.SoloGameMode;
import org.enstabretagne.Utils.Settings;
import org.enstabretagne.Utils.assetNames;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.input.view.KeyView;
import com.almasb.fxgl.ui.FXGLTextFlow;

import javafx.beans.binding.Bindings;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * Menu principal du jeu - Customisation du menu principal
 * 
 * @author LBF38, jufch, MathieuDFS
 * @since 0.3.0
 */
public class NewMainMenu extends FXGLMenu {
    public NewMainMenu() {
        super(MenuType.MAIN_MENU);

        var menu = createMenu();

        var background = texture(assetNames.textures.MAIN_MENU_BACKGROUND, getAppWidth(), getAppHeight()).darker();
        var title = getUIFactoryService().newText("DUEL INVADERS", Color.GRAY, 48);
        title.strokeProperty().set(Color.WHITE);
        var titleBox = new HBox(title);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setTranslateY(100);
        titleBox.setTranslateX(Settings.GAME_WIDTH / 2 - title.getLayoutBounds().getWidth() / 2);

        var pauseText = getUIFactoryService().newText("PAUSE", Color.WHITE, 20);
        var pauseKey = new KeyView(KeyCode.ESCAPE, Color.BLUE, 20.0);
        var pauseBox = new HBox(25, pauseText, pauseKey);
        pauseBox.setAlignment(Pos.CENTER);
        pauseBox.setTranslateX(getAppWidth() - pauseBox.getMaxWidth() - 200);
        pauseBox.setTranslateY(getAppHeight() - 150);

        getContentRoot().getChildren().addAll(background, menu, titleBox, pauseBox);
    }

    /**
     * Create the menu
     * 
     * @return VBox
     */
    private VBox createMenu() {
        VBox options = createOptions();
        FXGLTextFlow creditsText = createCredits();

        SpaceButton buttonClassicMode = new SpaceButton("Play Classique", () -> {
            GameLauncher.setGameMode(new ClassicGameMode());
            fireNewGame();
        });

        SpaceButton buttonInfinityMode = new SpaceButton("Play Infinity", () -> {
            GameLauncher.setGameMode(new InfinityGameMode());
            fireNewGame();
        });

        SpaceButton buttonSoloMode = new SpaceButton("Play Solo", () -> {
            GameLauncher.setGameMode(new SoloGameMode());
            fireNewGame();
        });

        SpaceButton buttonMusicDemo = new SpaceButton("Play Music Demo", () -> {
            GameLauncher.setGameMode(new MusicDemoGameMode());
            fireNewGame();
        });

        SpaceButton buttonOption = new SpaceButton("Option", () -> {
            if (!getContentRoot().getChildren().contains(options)) {
                getWindowService().getCurrentScene().removeChild(creditsText);
                getContentRoot().getChildren().add(options);
            }
        });

        SpaceButton buttonCredit = new SpaceButton("Credit", () -> {
            if (!getContentRoot().getChildren().contains(creditsText)) {
                getWindowService().getCurrentScene().removeChild(options);
                getContentRoot().getChildren().add(creditsText);
            }
        });

        SpaceButton buttonExit = new SpaceButton("Quit Game", () -> fireExit());

        var box = new VBox(10,
                buttonClassicMode,
                buttonInfinityMode,
                buttonSoloMode,
                buttonMusicDemo,
                buttonOption,
                buttonCredit,
                buttonExit,
                new Text(""),
                new Separator(Orientation.HORIZONTAL),
                getUIFactoryService().newText("Select Game Mode", Color.WHITE, 22));
        box.setTranslateX(100);
        box.setTranslateY(Settings.GAME_HEIGHT / 2);
        box.setAlignment(Pos.CENTER);
        return box;
    }

    /**
     * Create the options menu
     * 
     * @return VBox
     */
    private VBox createOptions() {
        var musicVolume = getSettings().getGlobalMusicVolume();
        var soundVolume = getSettings().getGlobalSoundVolume();
        var musicSlider = new Slider(0, 100, musicVolume * 100);
        var soundSlider = new Slider(0, 100, soundVolume * 100);
        var musicText = getUIFactoryService().newText("Music Volume", Color.GRAY, 22);
        musicText.strokeProperty().set(Color.WHITE);
        musicText.setStrokeWidth(0.5);
        var soundText = getUIFactoryService().newText("Sound Volume", Color.GRAY, 22);
        soundText.strokeProperty().set(Color.WHITE);
        soundText.setStrokeWidth(0.5);
        var musicBox = new HBox(10, musicText, musicSlider);
        var soundBox = new HBox(10, soundText, soundSlider);
        var box = new VBox(10, musicBox, soundBox);
        box.setTranslateX(getAppHeight() / 2 + 100);
        box.setTranslateY(Settings.GAME_HEIGHT / 2);
        musicBox.setAlignment(Pos.CENTER);
        soundBox.setAlignment(Pos.CENTER);
        box.setAlignment(Pos.CENTER);

        musicSlider.onMouseReleasedProperty().set(e -> {
            getSettings().setGlobalMusicVolume(musicSlider.getValue() / 100);
        });
        soundSlider.onMouseReleasedProperty().set(e -> {
            getSettings().setGlobalSoundVolume(soundSlider.getValue() / 100);
        });
        return box;
    }

    /**
     * Create the credits menu
     * 
     * @return FXGLTextFlow
     */
    private FXGLTextFlow createCredits() {
        String creditsString = getSettings().getCredits().toString().replace(",", "\n").replace("[", "")
                .replace("]", "");
        FXGLTextFlow creditsText = getUIFactoryService().newTextFlow();
        creditsText.append(creditsString, Color.GRAY, 16);
        creditsText.setTranslateX(getAppHeight() / 2 + 100);
        creditsText.setTranslateY(Settings.GAME_HEIGHT / 2);
        return creditsText;
    }

    /**
     * Define the button
     * 
     * @param name
     * @param action
     * @return SpaceButton
     * @see SpaceButton
     * @see StackPane
     * 
     * @author MathieuDFS, jufch, LBF38
     * @since 0.3.0
     */
    private static class SpaceButton extends StackPane {
        private static final Color SELECTED_COLOR = Color.WHITE;
        private static final Color NOT_SELECTED_COLOR = Color.GRAY;
        private Text text;
        private Rectangle selector;

        public SpaceButton(String name, Runnable action) {
            text = getUIFactoryService().newText(name, Color.WHITE, 16.0);
            text.fillProperty().bind(
                    Bindings.when(focusedProperty())
                            .then(SELECTED_COLOR)
                            .otherwise(NOT_SELECTED_COLOR));
            text.fillProperty().bind(
                    Bindings.when(hoverProperty())
                            .then(SELECTED_COLOR)
                            .otherwise(NOT_SELECTED_COLOR));
            text.strokeProperty().bind(
                    Bindings.when(focusedProperty())
                            .then(SELECTED_COLOR)
                            .otherwise(NOT_SELECTED_COLOR));
            text.setStrokeWidth(0.5);

            selector = new Rectangle(6, 17, Color.WHITE);
            selector.setTranslateX(-20);
            selector.setTranslateY(-2);
            selector.visibleProperty().bind(focusedProperty());

            setAlignment(Pos.CENTER_LEFT);

            setFocusTraversable(true);

            setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.SPACE) {
                    action.run();
                }
            });

            setOnMouseClicked(event -> action.run());

            getChildren().addAll(selector, text);
        }
    }
}
