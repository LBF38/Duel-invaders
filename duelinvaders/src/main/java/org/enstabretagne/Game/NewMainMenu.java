package org.enstabretagne.Game;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.Texture;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import org.enstabretagne.Core.Constant;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getUIFactoryService;


public class NewMainMenu extends FXGLMenu {
    public NewMainMenu() {
        super(MenuType.MAIN_MENU);

        SpaceButton buttonPlayGameClassique = new SpaceButton("Play Classique",()->{
            GameLauncher.setGameMode(0);
            fireNewGame();});
        SpaceButton buttonPlayGameInfini = new SpaceButton("Play Infinity",()->{
            GameLauncher.setGameMode(1);
            fireNewGame();});
        SpaceButton buttonPlayGameSolo = new SpaceButton("Play Solo",()-> {
            GameLauncher.setGameMode(2);
            fireNewGame();});
        /*SpaceButton buttonOption = new SpaceButton("Option",()-> {
        });
        SpaceButton buttonCredit = new SpaceButton("Credit",()-> {});*/
        SpaceButton buttonExit = new SpaceButton("Quit Game",()-> fireExit());


        var box = new VBox(10,
                buttonPlayGameClassique,
                buttonPlayGameInfini,
                buttonPlayGameSolo,
                //buttonOption,
                //buttonCredit,
                buttonExit,
                new Text(""),
                new Separator(Orientation.HORIZONTAL),
                getUIFactoryService().newText("Select Game Mode", Color.BLACK, 22)
        );
        box.setTranslateX(100);
        box.setTranslateY(Constant.GAME_HEIGHT/2);
        box.setAlignment(Pos.CENTER);

        getContentRoot().getChildren().addAll(box);
    }

    //@Override
    protected Button createActionButton(StringBinding stringBinding, Runnable runnable) {
        Button button = new Button();
        return button;
    }

    //@Override
    protected Button createActionButton(String string, Runnable runnable) {
        Button button = new Button();
        return button;
    }

    //@Override ->Normalement ça devrait overide la méthode mais ça ne marche pas
    protected Node createBackground(double width, double height) {
        return FXGL.texture("Background.png", width, height);
    }

    //@Override
    protected Node createProfileView(String title) {
        return new Rectangle();
    }
    //@Override
    protected Node createTitleView(String title) {
        return new Rectangle();
    }

    private static class SpaceButton extends StackPane {
        private static final Color SELECTED_COLOR = Color.BLACK; //Color.WHITE;
        private static final Color NOT_SELECTED8COLOR = Color.GRAY;
        private String name;
        private Runnable action;
        private Text text;
        private Rectangle selector;

        public SpaceButton(String name, Runnable action) {
            this.name = name;
            this.action = action;

            text=FXGL.getUIFactoryService().newText(name, Color.BLACK,16.0);
            text.fillProperty().bind(
                    Bindings.when(focusedProperty())
                            .then(SELECTED_COLOR)
                            .otherwise(NOT_SELECTED8COLOR)
            );

            text.strokeProperty().bind(
                    Bindings.when(focusedProperty())
                            .then(SELECTED_COLOR)
                            .otherwise(NOT_SELECTED8COLOR)
            );
            text.setStrokeWidth(0.5);

            selector = new Rectangle(6, 17, Color.BLACK);
            selector.setTranslateX(-20);
            selector.setTranslateY(-2);
            selector.visibleProperty().bind(focusedProperty());



            setAlignment(Pos.CENTER_LEFT);

            setFocusTraversable(true);

            setOnKeyPressed(e->{
                if (e.getCode() == KeyCode.ENTER) {
                    action.run();
                }
            });

            getChildren().addAll(selector,text);
        }
    }
}
