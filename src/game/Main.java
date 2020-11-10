package game;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.File;
import java.util.HashSet;

public class Main extends Application{
    private Scene game;
    private Scene gameOver;
    private int high_score=0,current_score=0;
    private Label lbl1;
    private Label lbl2;
    private Label lbl3;
    private String message;
    private final int speed = 15;
    public static GraphicsContext gc;
    public static boolean startGame = false;
    public static Image player;
    public static Image playerDestroyed;
    public static Image enemy1;
    public static Image enemyFire;
    public static Image bossEnemy;
    static Player me;
    static boolean temp = false;
    static boolean over = false;
    static Alien[][]grid;
    static Alien boss;
    static char prevDirection='R';
    static long cntFrames = 0;
    public void start(Stage primaryStage) {
        //setup/initialization
        primaryStage.setTitle("Space Invaders");

        Group root = new Group();
        game = new Scene(root);

        Canvas canvas = new Canvas(1280, 720);
        root.getChildren().add(canvas);

        gc = canvas.getGraphicsContext2D();

        Font TimesNewRoman = Font.font("Times New Roman", FontWeight.NORMAL, 36);
        gc.setFont( TimesNewRoman );
        Label title = new Label("     Space Invaders!\nBy Yubo and Jeremy");
        title.setFont(TimesNewRoman);
        title.setLayoutX(485);
        title.setLayoutY(100);
        lbl1 = new Label("\t\t\t\t\t\tHigh Score: " + high_score);
        lbl2 = new Label("\t\t\t\t\t\tScore: " + current_score);
        lbl3 = new Label("\t\t\t\t\t\t" + message);
        lbl1.setPrefSize(400,75);
        lbl2.setPrefSize(400,75);
        lbl1.setLayoutX(410);
        lbl1.setLayoutY(100);
        lbl2.setLayoutX(420);
        lbl2.setLayoutY(175);
        lbl3.setLayoutX(410);
        lbl3.setLayoutY(275);

        Group layout1 = new Group();
        Button btn = new Button();
        btn.setText("Start Game!");
        btn.setOnAction(e -> setStartGame());
        btn.setPrefSize(400,100);
        btn.setLayoutX(440);
        btn.setLayoutY(300);
        layout1.getChildren().add(btn);
        layout1.getChildren().add(title);
        Scene menu = new Scene(layout1, 1280, 720);
        primaryStage.setScene(menu);
        primaryStage.setResizable(false);
        //layout1 = menu, layout2 = gameOver
        Group layout2 = new Group();
        Button restart = new Button();
        restart.setText("Restart");
        restart.setOnAction(e -> setStartGame());
        restart.setPrefSize(400,75);
        restart.setLayoutX(440);
        restart.setLayoutY(400);
        Button quit = new Button();
        quit.setText("Quit Game");
        quit.setOnAction(e -> exit());
        quit.setPrefSize(400,75);
        quit.setLayoutX(440);
        quit.setLayoutY(525);
        layout2.getChildren().add(restart);
        layout2.getChildren().add(quit);
        layout2.getChildren().add(lbl1);
        layout2.getChildren().add(lbl2);
        layout2.getChildren().add(lbl3);
        gameOver = new Scene(layout2,1280,720);
        primaryStage.show();
        gc.setLineWidth(1);
        //setup:
        player = new Image(new File("player.png").toURI().toString(), 80, 36, true, false);
        playerDestroyed = new Image(new File("player destroyed.png").toURI().toString(), 80, 36, true, false);
        enemy1 = new Image(new File("enemy.png").toURI().toString(), 33, 24, true, false);
        enemyFire = new Image(new File("enemyfire.png").toURI().toString(),33,24,false,false);
        bossEnemy = new Image(new File("boss.png").toURI().toString(),45,30,true,false);
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
                    if(boss.enabled) {
                        boss.update();
                        if(boss.isHit(me.bullet)){
                            boss.enabled = false;
                            cntFrames = cntFrames / 600;
                            me.points+=500;
                        }
                    }
                    //debug System.out.println(me.lives);
                    if(checkWin()){
                        message = "You Won!";
                        startGame = false;
                        over = true;
                    }
                    if(checkLose()){
                        message = "You Lose!";
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
                        char direction;
                        if(grid[10][0].x >= 1180 && prevDirection != 'L') {
                            direction = 'D';
                            moveAll(direction,speed);
                            prevDirection='L';
                        }
                        else if(grid[0][0].x <= 40 && prevDirection != 'R') {
                            direction = 'D';
                            moveAll(direction,speed);

                            prevDirection='R';
                        }
                        else{
                            direction = prevDirection;
                            moveAll(direction,speed);
                        }

                    }
                    if (cntFrames % 60 == 31) {
                        shoot();
                    }
                    if(cntFrames % 600 == 0){
                        boss.enabled = true;
                        boss.x = 1300;
                    }

                    //boss move
                    if(boss.enabled){
                        if(boss.x <= 0) {
                            cntFrames = cntFrames / 600;
                            boss.enabled = false;
                        }
                        boss.move('L',3);
                    }
                    //draw lives, and points
                    gc.setStroke(Color.GREEN);
                    gc.setLineWidth(5);
                    gc.strokeLine(0, 650, 1281, 650);
                    gc.setLineWidth(1);
                    gc.setFill(Color.WHITE);
                    gc.fillText("Score:" + me.points, 30, 30);
                    //end
                    if (input.contains("D") || input.contains("RIGHT")) me.moveRight();
                    if (input.contains("A") || input.contains("LEFT")) me.moveLeft();
                    if (input.contains("SPACE")) me.fire();
                    for (int i = 0; i < me.lives; i++) {
                        gc.drawImage(player, 50 + i * 120, 665);
                    }
                }
                if(over){
                    high_score = Math.max(high_score,me.points);
                    current_score = me.points;
                    lbl1.setText("\t\t\t\t\t\tHigh Score: " + high_score);
                    lbl2.setText("\t\t\t\t\t\tScore: " + current_score);
                    lbl3.setText("\t\t\t\t\t\t" + message);
                    primaryStage.setScene(gameOver);
                    input.clear();
                }
                cntFrames++;
                //just in case of overflow lol
                //If you run the program for just over a year, it might overflow...
                if(cntFrames >= 2000000000)
                    cntFrames = 1;
            }
        }.start();

        primaryStage.show();

    }
    public boolean checkLose() {
        if(me.lives<=0)
            return true;
        for(int i = 0; i < 11; i++){
            for(int j = 0; j < 5; j++){
                if(grid[i][j].enabled&&grid[i][j].y>=600)
                    return true;
            }
        }
        return false;
    }
    public boolean checkWin(){
        for(int i = 0; i < 11; i++)
            for(int j = 0; j < 5; j++)
                if(grid[i][j].enabled)
                    return false;
        return true;
    }
    public void shoot() {
        int x = (int)(Math.random()*11), y = (int)(Math.random()*5);
        //debug System.out.println(x + " " + y);
        while(!grid[x][y].enabled){
            x = (int)(Math.random()*11);
            y = (int)(Math.random()*5);
        }
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
        boss = new Alien(2,100,75);
        boss.enabled = false;
        cntFrames = 0;
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
