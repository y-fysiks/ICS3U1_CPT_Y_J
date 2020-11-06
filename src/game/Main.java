package game;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
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
    private Group layout1, layout2; //layout1 = menu, layout2 = gameOver
    private Button btn,restart,quit;
    private Scene menu,game,gameOver;
    private int high_score=0,current_score=0;
    private Label lbl1,lbl2,title;
    public static GraphicsContext gc;
    public static boolean startGame = false;
    private int width = 1280, height = 720;
    public static Image player;
    public static Image playerDestroyed;
    public static Image enemy1;
    public static Image enemyFire;
    static Player me;
    static boolean temp = false;
    static boolean over = false;
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
        title = new Label("     Space Invaders!\nBy Yubo and Jeremy");
        title.setFont(TimesNewRoman);
        title.setLayoutX(485);
        title.setLayoutY(100);
        lbl1 = new Label("\t\t\t\t\t\tHigh Score: " + Integer.toString(high_score));
        lbl2 = new Label("\t\t\t\t\t\tScore: " + Integer.toString(current_score));
        lbl1.setPrefSize(400,75);
        lbl2.setPrefSize(400,75);
        lbl1.setLayoutX(440);
        lbl1.setLayoutY(75);
        lbl2.setLayoutX(440);
        lbl2.setLayoutY(175);
        layout1 = new Group();
        btn = new Button();
        btn.setText("Start Game!");
        btn.setOnAction(e -> setStartGame());
        btn.setPrefSize(400,100);
        btn.setLayoutX(440);
        btn.setLayoutY(300);
        layout1.getChildren().add(btn);
        layout1.getChildren().add(title);
        menu = new Scene(layout1,width,height);
        primaryStage.setScene(menu);
        primaryStage.setResizable(false);
        layout2 = new Group();
        restart = new Button();
        restart.setText("Restart");
        restart.setOnAction(e -> setStartGame());
        restart.setPrefSize(400,75);
        restart.setLayoutX(440);
        restart.setLayoutY(300);
        quit = new Button();
        quit.setText("Quit Game");
        quit.setOnAction(e -> exit());
        quit.setPrefSize(400,75);
        quit.setLayoutX(440);
        quit.setLayoutY(425);
        layout2.getChildren().add(restart);
        layout2.getChildren().add(quit);
        layout2.getChildren().add(lbl1);
        layout2.getChildren().add(lbl2);
        gameOver = new Scene(layout2,width,height);
        primaryStage.show();
        gc.setLineWidth(1);
        //setup:
        player = new Image(new File("player.png").toURI().toString(), 80, 36, true, false);
        playerDestroyed = new Image(new File("player destroyed.png").toURI().toString(), 80, 36, true, false);
        enemy1 = new Image(new File("enemy.png").toURI().toString(), 33, 24, true, false);
        enemyFire = new Image(new File("enemyfire.png").toURI().toString(),33,24,false,false);

        initGame();
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
                    //debug System.out.println(me.lives);
                    if(me.lives<=0) {
                        startGame = false;
                        over = true;
                    }
                    for(int i = 0; i < 11; i++) {
                        for(int j = 0; j < 5; j++) {
                            grid[i][j].update();
                            if(me.isHit(grid[i][j].bullet)) {
                                grid[i][j].bullet.disable();
                                me.destroy();
                            }
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
                }
                if(over){
                    high_score = Math.max(high_score,me.points);
                    current_score = me.points;
                    lbl1.setText("\t\t\t\t\t\tHigh Score: " + Integer.toString(high_score));
                    lbl2.setText("\t\t\t\t\t\tScore: " + Integer.toString(current_score));
                    primaryStage.setScene(gameOver);

                }
                cntFrames++;

            }
        }.start();

        primaryStage.show();

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
        over = false;
        temp = false;
        initGame();
    }
    public static void initGame(){
        me = new Player();
        grid = new Alien[11][5];
        for(int i = 0; i < 11; i++) {
            for (int j = 0; j < 5; j++) {
                grid[i][j] = new Alien(1, i * 45 + 100,100 + j * 37);
            }
        }
    }
    public static void exit(){
        System.exit(0);
    }
    public static void main(String[] args) {
        launch(args);
    }
}
