package game;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application{
    StackPane layout1, layout2; //layout1 = menu, layout2 = game
    Button btn;
    Scene menu,game;
    public static boolean startGame = false;
    private int width = 1280, height = 720;

    public void start(Stage primaryStage) throws Exception{
        //setup/initialization
        primaryStage.setTitle("Space Invaders");

        Image player = new Image(new File("player.png").toURI().toString(), 80, 36, true, false);
        Image playerDestroyed = new Image(new File("playerDestroyed.png").toURI().toString());
        Image enemy1 = new Image(new File("enemy.png").toURI().toString());

        Group root = new Group();
        game = new Scene(root);
        primaryStage.setScene(game);

        Canvas canvas = new Canvas(1280, 720);
        root.getChildren().add(canvas);

        GraphicsContext gc = canvas.getGraphicsContext2D();

        Font TimesNewRoman = Font.font( "Times New Roman", FontWeight.BOLD, 48 );
        gc.setFont( TimesNewRoman );

        layout1 = new StackPane();
        btn = new Button();
        btn.setText("Start Game!");
        btn.setOnAction(e -> primaryStage.setScene(game));
        layout1.getChildren().add(btn);
        menu = new Scene(layout1,width,height);
        primaryStage.setScene(menu);

        primaryStage.show();


        new AnimationTimer() {
            @Override
            public void handle(long now) {
                gc.setFill( Color.RED );
                gc.setStroke( Color.BLACK );
                gc.setLineWidth(2);


            }
        }.start();

        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
