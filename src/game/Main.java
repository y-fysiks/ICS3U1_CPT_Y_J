package game;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
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
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.File;
import java.util.HashSet;

public class Main extends Application{
    //Declare class variables
    private Scene game;
    private Scene gameOver;
    private int high_score=0,current_score=0;
    private String message;
    private final int speed = 20;
    public static GraphicsContext gc;
    public static boolean startGame = false;
    public static Image player;
    public static Image playerDestroyed;
    public static Image enemy1;
    public static Image enemyFire;
    public static Image bossEnemy;
    static Player me;
    static boolean over = false;
    static Alien[][]grid;
    static Alien boss;
    static char nextDirection='R';
    static long cntFrames = 0;
    public void start(Stage primaryStage) {
        //setup/initialization of all groups, canvas, fonts, images, buttons, labels.
        //JavaFX objects
        primaryStage.setTitle("Space Invaders");
        primaryStage.setWidth(1280);
        primaryStage.setHeight(720);
        Group root = new Group();
        game = new Scene(root);

        Canvas canvas = new Canvas(1280, 720);
        root.getChildren().add(canvas);

        gc = canvas.getGraphicsContext2D();

        Font TimesNewRoman = Font.font("Times New Roman", FontWeight.NORMAL, 36);
        gc.setFont(TimesNewRoman);

        Label title = new Label("     Space Invaders!\nBy Yubo and Jeremy");
        title.setFont(TimesNewRoman);
        title.setLayoutX(485);
        title.setLayoutY(100);
        title.setTextFill(Color.GREEN);

        Label lbl1 = new Label("High Score: " + high_score);
        Label lbl2 = new Label("Score: " + current_score);
        Label lbl3 = new Label("" + message);

        lbl1.setStyle("-fx-font-size: 2em");
        lbl1.setTranslateY(100);
        lbl1.setMinWidth(1280);
        lbl1.setAlignment(Pos.CENTER);
        lbl1.setTextAlignment(TextAlignment.CENTER);
        lbl1.setTextFill(Color.GREEN);
        //debug System.out.println(lbl1.getWidth());

        lbl2.setStyle("-fx-font-size: 2em");
        lbl2.setMinWidth(1280);
        lbl2.setAlignment(Pos.CENTER);
        lbl2.setTextAlignment(TextAlignment.CENTER);
        lbl3.setMinWidth(1280);
        lbl3.setAlignment(Pos.CENTER);
        lbl3.setTextAlignment(TextAlignment.CENTER);
        lbl3.setStyle("-fx-font-size: 3em");
        lbl1.setTranslateY(75);
        lbl2.setTranslateY(175);
        lbl3.setTranslateY(275);
        lbl1.setTextFill(Color.GREEN);
        lbl2.setTextFill(Color.GREEN);
        lbl3.setTextFill(Color.RED);

        Group layout1 = new Group();
        Button btn = new Button();
        Button tutorial = new Button();


        btn.setText("Start Game!");
        btn.setOnAction(e -> setStartGame());
        btn.setPrefSize(300,50);
        btn.setLayoutX(490);
        btn.setLayoutY(300);
        btn.setTextFill(Color.GREEN);
        btn.setStyle("-fx-font-size: 2em; -fx-background-color: black");


        Group howToG = new Group();
        Scene howTo = new Scene(howToG, 1280, 720, Color.BLACK);
        Label instructions = new Label("You are under attack by aliens!\nYou must shoot them down, but be careful!\nThey will shoot back. \n\nIf they reach the ground, you lose. If you die, you lose.\n\nNormal Alien: 100 pts.\nMothership: 500pts.\nUse A and D or left and right arrow keys to move, and space to shoot.\nGood luck.");
        instructions.setStyle("-fx-font-size: 2em");
        instructions.setTranslateY(100);
        instructions.setMinWidth(1280);
        instructions.setAlignment(Pos.CENTER);
        instructions.setTextAlignment(TextAlignment.CENTER);
        instructions.setTextFill(Color.GREEN);

        Button back = new Button("Back");
        back.setTextFill(Color.GREEN);
        back.setStyle("-fx-font-size: 2em; -fx-background-color: black");

        howToG.getChildren().add(instructions);
        howToG.getChildren().add(back);
        tutorial.setText("How to play");
        tutorial.setOnAction(e -> primaryStage.setScene(howTo));
        tutorial.setPrefSize(300,50);
        tutorial.setLayoutX(490);
        tutorial.setLayoutY(370);
        tutorial.setTextFill(Color.GREEN);
        tutorial.setStyle("-fx-font-size: 2em; -fx-background-color: black");


        layout1.getChildren().add(btn);
        layout1.getChildren().add(tutorial);
        layout1.getChildren().add(title);

        Scene menu = new Scene(layout1, 1280, 720, Color.BLACK);
        primaryStage.setScene(menu);
        primaryStage.setResizable(false);
        back.setOnAction(event -> primaryStage.setScene(menu));

        //layout1 = menu, layout2 = gameOver

        Group layout2 = new Group();
        Button restart = new Button();

        restart.setText("Restart");
        restart.setOnAction(e -> setStartGame());//Map button to start game method
        restart.setPrefSize(300,50);
        restart.setLayoutX(490);
        restart.setLayoutY(400);
        restart.setTextFill(Color.GREEN);
        restart.setStyle("-fx-font-size: 2em; -fx-background-color: black");

        Button quit = new Button();

        quit.setText("Quit Game");
        quit.setOnAction(e -> exit());//Map button to exit program method
        quit.setPrefSize(300,50);
        quit.setLayoutX(490);
        quit.setLayoutY(500);
        quit.setTextFill(Color.GREEN);
        quit.setStyle("-fx-font-size: 2em; -fx-background-color: black");

        layout2.getChildren().add(restart);
        layout2.getChildren().add(quit);
        layout2.getChildren().add(lbl1);
        layout2.getChildren().add(lbl2);
        layout2.getChildren().add(lbl3);

        gameOver = new Scene(layout2,1280,720, Color.BLACK);

        primaryStage.show();

        gc.setLineWidth(1);
        //setup images:
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
                    primaryStage.setScene(game);
                    //Graphics
                    gc.setFill( Color.BLACK );
                    gc.fillRect(0, 0, 1281, 721);
                    //Update player sprite
                    me.update();
                    //Check if the mothership has been enabled
                    if(boss.enabled) {
                        //Update mothership and check if it has been hit.
                        boss.update();
                        if(boss.isHit(me.bullet)){
                            boss.enabled = false;
                            cntFrames = cntFrames / 600; //Divide by 600 so another mothership does not instantly spawn.
                            me.points+=500;
                        }
                    }
                    //debug System.out.println(me.lives);
                    //If player has won or lost, update message, startGame, and over
                    if(checkWin()){
                        message = "You Won!";
                        input.clear();
                        startGame = false;
                        over = true;
                    }
                    if(checkLose()){
                        message = "You Lose!";
                        input.clear();
                        startGame = false;
                        over = true;
                    }
                    //Iterate over the entire grid of aliens and update them all.
                    for(int i = 0; i < 11; i++) {
                        for(int j = 0; j < 5; j++) {
                            grid[i][j].update();
                            //Check if player has been hit by an aliens bullet
                            if(me.isHit(grid[i][j].bullet)) {
                                grid[i][j].bullet.disable();
                                me.destroy();
                            }
                            //Check if an alien has been hit by a players bullet
                            if (grid[i][j].isHit(me.bullet)) {
                                grid[i][j].enabled = false;
                                me.bullet.disable();
                                me.points += 100;
                            }
                            //Reset the isFiring variable of all aliens
                            if (cntFrames % 30 == 0) {
                                grid[i][j].isFiring = false;
                            }
                        }
                    }
                    //When cntFrames is divisible by 30, make all aliens move ni a certain direction.
                    if (cntFrames % 30 == 0) {
                        char direction;
                        //When the rightmost alien reaches the right boundary from the left, make all aliens move down and set nextDirection to left.
                        if(grid[10][0].x >= 1180 && nextDirection != 'L') {
                            direction = 'D';
                            moveAll(direction,speed);
                            nextDirection='L';
                        }
                        //When the leftmost alien reaches the left boundary from the right, make all aliens move down and set nextDirection to right.
                        else if(grid[0][0].x <= 40 && nextDirection != 'R') {
                            direction = 'D';
                            moveAll(direction,speed);
                            nextDirection='R';
                        }
                        //In between the boundaries, we simply set the currentDirection to nextDirection and move all aliens in that direction.
                        else{
                            direction = nextDirection;
                            moveAll(direction,speed);
                        }

                    }
                    //When cntFrames is divisible by 60, make a random active alien fire.
                    if (cntFrames % 60 == 30) {
                        shoot();
                    }
                    //When cntFrames is divisible by 600, enable the boss.
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
                    gc.strokeLine(0, 610, 1281, 610);
                    gc.setLineWidth(1);
                    gc.setFill(Color.WHITE);
                    gc.fillText("Score: " + me.points, 30, 50);
                    //Read user keyboard input.
                    if (input.contains("D") || input.contains("RIGHT")) me.moveRight();
                    if (input.contains("A") || input.contains("LEFT")) me.moveLeft();
                    if (input.contains("SPACE")) me.fire();
                    for (int i = 0; i < me.lives; i++) {
                        gc.drawImage(player, 50 + i * 120, 625);
                    }
                }
                //When game ends, update current score and high score as well as the labels.
                if(over){
                    high_score = Math.max(high_score,me.points);
                    current_score = me.points;
                    lbl1.setText("High Score: " + high_score);
                    lbl2.setText("Score: " + current_score);
                    lbl3.setText("" + message);
                    input.clear();
                    //Change the scene to gameOver
                    primaryStage.setScene(gameOver);


                }
                cntFrames++;
                //just in case of overflow lol
                //If you run the program for just over a year, it might overflow...
                if(cntFrames >= 2000000000)
                    cntFrames = 1;
            }
        }.start();
        //show the stage
        primaryStage.show();

    }
    //Check if the player has lost by checking either if the players live has dropped to 0 or if an active alien has reached the player.
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
    //Check if the player has won by checking each aliens status. If there is an alive alien, return false. Otherwise, return true.
    public boolean checkWin(){
        for(int i = 0; i < 11; i++)
            for(int j = 0; j < 5; j++)
                if(grid[i][j].enabled)
                    return false;
        return true;
    }
    //method to cause a random enabled alien to shoot a bullet.
    public void shoot() {
        int x = (int)(Math.random()*11), y = (int)(Math.random()*5);
        //debug System.out.println(x + " " + y);
        //keep generating random numbers until the alien at that index is active.
        while(!grid[x][y].enabled){
            x = (int)(Math.random()*11);
            y = (int)(Math.random()*5);
        }
        //fire bullet
        grid[x][y].bullet.fire(grid[x][y].x,grid[x][y].y);
        grid[x][y].isFiring=true;
        grid[x][y].update();
    }
    //Method to move all aliens in a certain direction and speed.
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
    //start game method
    private static void setStartGame() {
        startGame = true;
        over = false;
        initGame();
    }
    //initialize game by resetting player, aliens, boss, and cntFrames.
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
    //Exits the program
    public static void exit(){
        System.exit(0);
    }
    //Main method only passes args to javafx
    public static void main(String[] args) {
        launch(args);
    }
}
