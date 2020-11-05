package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class Main extends Application{
    Stage window;
    StackPane layout1, layout2; //layout1 = menu, layout2 = game
    Button btn;
    Scene menu,game;
    private int width = 1280, height = 720;
    @Override
    public void start(Stage primaryStage) throws Exception{
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
    }

    public static void main(String[] args) {
        launch(args);
    }
}
