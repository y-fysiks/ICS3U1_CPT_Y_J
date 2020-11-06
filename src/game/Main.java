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
import java.util.HashSet;

public class Main extends Application{
    StackPane layout1, layout2; //layout1 = menu, layout2 = game
    Button btn;
    Scene menu,game;
    public static GraphicsContext gc;
    public static boolean startGame = false;
    private int width = 1280, height = 720;
    public static Image player;
    public static Image playerDestroyed;
    public static Image enemy1;
    static boolean temp = false;
    static Alien[][]grid;
    static char prevDirection='R';
    static long cntFrames = 0;
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
        primaryStage.setResizable(false);

        primaryStage.show();
        gc.setLineWidth(2);
        Font font = Font.font( "Times New Roman", FontWeight.BOLD, 48 );
        gc.setFont(font);

        //setup:
        player = new Image(new File("player.png").toURI().toString(), 80, 36, true, false);
        playerDestroyed = new Image(new File("playerDestroyed.png").toURI().toString(), 80, 36, true, false);
        enemy1 = new Image(new File("enemy.png").toURI().toString(), 33, 24, true, false);


        Player me = new Player();
        grid = new Alien[11][5];
        for(int i = 0; i < 11; i++) {
            for (int j = 0; j < 5; j++) {
                grid[i][j] = new Alien(1, i * 55 + 50,50 + j * 45);
            }
        }
        HashSet<String> input = new HashSet<>();
        game.setOnKeyPressed(
                event -> {
                    String code = event.getCode().toString();
                    input.add(code);
                }
        );
        game.setOnKeyReleased(
                event -> {
                    String code = event.getCode().toString();
                    input.remove(code);
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
                    for(int i = 0; i < 11; i++) {
                        for(int j = 0; j < 5; j++) {
                            grid[i][j].update();
                            if(me.isHit(grid[i][j].bullet)) me.destroy();
                        }
                    }
                    if (cntFrames % 60 == 0) {
                        char direction = 'R';
                        if(grid[10][0].x >= 1200 && prevDirection != 'L') {
                            direction = 'D';
                            moveAll(direction,10);
                            direction = 'L';
                            prevDirection='L';
                        }
                        else if(grid[0][0].x <= 80 && prevDirection != 'R') {
                            direction = 'D';
                            moveAll(direction,10);
                            direction='R';
                            prevDirection='R';
                        }
                        else{
                            direction = prevDirection;
                            moveAll(direction,40);
                        }

                    }

                    if (input.contains("D") || input.contains("RIGHT")) me.moveRight();
                    if (input.contains("A") || input.contains("LEFT")) me.moveLeft();
                    if (input.contains("SPACE")) me.fire();



                }
                cntFrames++;

            }
        }.start();

        primaryStage.show();

    }
    public void moveAll(char direction, int speed){
        if(direction=='R'){
            for(int i = 0; i < 11; i++)
                for(int j = 0; j < 5; j++)
                    grid[i][j].move(direction,speed);
        }
        else if(direction=='L'){
            for(int i = 0; i < 11; i++)
                for(int j = 0; j < 5; j++)
                    grid[i][j].move(direction,speed);
        }
        else if(direction=='D'){
            for(int i = 0; i < 11; i++)
                for(int j = 0; j < 5; j++)
                    grid[i][j].move(direction,speed);
        }
    }
    private static void setStartGame() {
        startGame = true;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
