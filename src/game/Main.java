package game;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

//hi
//asdasdp
//I think I know what's wrong
//nvm
//hi lol
//ah
//u could just
//change the address
//wait imma search it up
public class Main extends Application{
    Stage window;
    StackPane layout1, layout2; //layout1 = menu, layout2 = game
    Button btn;
    Scene menu,game;
    private int width = 1280, height = 720;
    @Override
    public void start(Stage primaryStage) throws Exception{
        Image player = new Image("file:player.png");
        Image playerDestroyed = new Image("file:player destroyed.png");
        Image enemy1 = new Image("file:enemy.png");
        window = primaryStage;
        window.setTitle("Space Invaders");
        layout1 = new StackPane();
        layout2 = new StackPane();
        btn = new Button();
        btn.setText("Start Game!");
        btn.setOnAction(e -> window.setScene(game));
        layout1.getChildren().add(btn);
        layout2.setBackground(new Background(new BackgroundFill(Color.rgb(34,47,62), CornerRadii.EMPTY, Insets.EMPTY)));
        menu = new Scene(layout1,width,height);
        game = new Scene(layout2, width, height);
        window.setScene(menu);
        window.show();
        new AnimationTimer() {
            @Override
            public void handle(long now) {

            }
        }.start();


    }

    public static void main(String[] args) {
        launch(args);
    }
}
