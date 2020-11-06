package game;

import javafx.scene.image.Image;

import java.io.File;

public class Alien{
    private final int type;
    int x;
    int y;
    boolean enabled = true;
    Bullet bullet = new Bullet('D');
    Image enemy1 = new Image(new File("enemy.png").toURI().toString(), 88, 64, true, false);


    public Alien(int type_, int x_, int y_) {
        type = type_;
        x = x_;
        y = y_;
    }
    public void update() {
        if (enabled) {
            if (type == 1) {
                Main.gc.drawImage(enemy1, x, y);
            }

        }
    }
}