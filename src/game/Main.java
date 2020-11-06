package game;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;

public class Main extends Application{
    StackPane layout1, layout2; //layout1 = menu, layout2 = game
    Button btn;
    Scene menu,game;
    public static GraphicsContext gc;
    public static boolean startGame = false;
    private int width = 1280, height = 720;
    public static Image player;
    public static Image playerDestroyed;
    static boolean temp = false;

    public void start(Stage primaryStage) throws Exception{
        //setup/initialization
        primaryStage.setTitle("Space Invaders");



        Group root = new Group();
        game = new Scene(root);

        Canvas canvas = new Canvas(1280, 720);
        root.getChildren().add(canvas);

        gc = canvas.getGraphicsContext2D();

        Font TimesNewRoman = Font.font( "Times New Roman", FontWeight.BOLD, 48 );
        gc.setFont( TimesNewRoman );

        layout1 = new StackPane();
        btn = new Button();
        btn.setText("Start Game!");
        btn.setOnAction(e -> setStartGame());
        layout1.getChildren().add(btn);
        menu = new Scene(layout1,width,height);
        primaryStage.setScene(menu);

        primaryStage.show();
        gc.setLineWidth(2);
        Font font = Font.font( "Times New Roman", FontWeight.BOLD, 48 );
        gc.setFont(font);

        //setup:
        player = new Image(new File("player.png").toURI().toString(), 80, 36, true, false);
        playerDestroyed = new Image(new File("playerDestroyed.png").toURI().toString(), 80, 36, true, false);
        Player me = new Player();
        ArrayList<String> input = new ArrayList<>();
        game.setOnKeyPressed(
                event -> {
                    String code = event.getCharacter();
                    if (!input.contains(code)) input.add(code);
                }
        );
        game.setOnKeyReleased(
                event -> {
                    String code = event.getCharacter();
                    if (!input.contains(code)) input.add(code);
                }
        );

        //setup ends
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (startGame) {
                    if (!temp) {
                        primaryStage.setScene(game);
                        temp = true;
                    }
                    gc.setFill( Color.BLACK );
                    gc.fillRect(0, 0, 1281, 721);
                    me.update();
                }


            }
        }.start();

        primaryStage.show();

    }
    private static void setStartGame() {
        startGame = true;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
