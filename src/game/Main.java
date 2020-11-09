package game;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    Label hiScore;
    Scene menu,game;
    public static GraphicsContext gc;
    public static boolean startGame = false;
    public static Image player;
    public static Image playerDestroyed;
    public static Image enemy1;
    public static Image enemyFire;
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

        Font TimesNewRoman = Font.font("Times New Roman", FontWeight.NORMAL, 36);
        gc.setFont( TimesNewRoman );

        layout1 = new StackPane();
        btn = new Button();
        btn.setText("Start Game!");
        btn.setOnAction(e -> setStartGame());
        hiScore = new Label();
        hiScore.setTranslateY(50);

        layout1.getChildren().add(btn);
        layout1.getChildren().add(hiScore);
        menu = new Scene(layout1,1280,720);
        primaryStage.setScene(menu);
        primaryStage.setResizable(false);
        primaryStage.show();
        gc.setLineWidth(1);

        //setup:
        player = new Image(new File("player.png").toURI().toString(), 80, 36, true, false);
        playerDestroyed = new Image(new File("player destroyed.png").toURI().toString(), 80, 36, true, false);
        enemy1 = new Image(new File("enemy.png").toURI().toString(), 33, 24, true, false);
        enemyFire = new Image(new File("enemyfire.png").toURI().toString(),33,24,false,false);

        Player me = new Player();
        grid = new Alien[11][5];
        for(int i = 0; i < 11; i++) {
            for (int j = 0; j < 5; j++) {
                grid[i][j] = new Alien(1, i * 45 + 100,100 + j * 37);
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
                            if (grid[i][j].isHit(me.bullet)) {
                                grid[i][j].enabled = false;
                                me.bullet.disable();
                                me.points += 100;
                            }
                            if (cntFrames % 30 == 29) {
                                grid[i][j].isFiring = false;
                            }
                        }
                    }
                    if (cntFrames % 30 == 0) {
                        char direction = 'R';
                        if(grid[10][0].x >= 1180 && prevDirection != 'L') {
                            direction = 'D';
                            moveAll(direction,10);
                            prevDirection='L';
                        }
                        else if(grid[0][0].x <= 100 && prevDirection != 'R') {
                            direction = 'D';
                            moveAll(direction,10);
                            prevDirection='R';
                        }
                        else{
                            direction = prevDirection;
                            moveAll(direction,10);
                        }


                    }
                    if (cntFrames % 60 == 30) {
                        shoot();
                    }
                    //draw lives, and points
                    gc.setStroke(Color.GREEN);
                    gc.setLineWidth(5);
                    gc.strokeLine(0, 650, 1281, 650);
                    gc.setLineWidth(1);
                    gc.setFill(Color.WHITE);
                    gc.fillText("" + me.points, 30, 30);
                    //end
                    if (input.contains("D") || input.contains("RIGHT")) me.moveRight();
                    if (input.contains("A") || input.contains("LEFT")) me.moveLeft();
                    if (input.contains("SPACE")) me.fire();
                    if (me.lives == 0) {
                        primaryStage.setScene(menu);
                        gameOver(me);
                        hiScore.setText("High score: " + me.highScore);
                        for (int i = 0; i < grid.length; i++) {
                            for (int j = 0; j < grid[i].length; j++) {
                                grid[i][j].enabled = true;
                            }
                        }
                    }
                    for (int i = 0; i < me.lives; i++) {
                        gc.drawImage(player, i * 120 + 50, 670);

                    }

                }
                cntFrames++;

            }
        }.start();

        primaryStage.show();

    }

    public void gameOver(Player player) {
        player.highScore = Math.max(player.highScore, player.points);
        player.points = 0;
        player.lives = 3;
        startGame = false;
        temp = false;
    }
    public void shoot() {
        int x = (int)(Math.random()*11), y = (int)(Math.random()*5);
        //debug System.out.println(x + " " + y);
        grid[x][y].bullet.fire(grid[x][y].x,grid[x][y].y);
        grid[x][y].isFiring=true;
        grid[x][y].update();
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
