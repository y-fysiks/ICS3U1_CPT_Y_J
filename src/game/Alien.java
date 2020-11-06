package game;

import javafx.scene.image.Image;

import java.io.File;

public class Alien{
    private final int type;
    int x;
    int y;
    boolean enabled = true;
    Bullet bullet = new Bullet('D');

    public Alien(int type_, int x_, int y_) {
        type = type_;
        x = x_;
        y = y_;
    }
    public void move(char direction){
        if(direction=='L')
            x-=10;
        else if(direction=='R')
            x+=10;
        else if(direction=='D')
            y+=10;
    }
    public void update() {
        if (enabled) {
            if (type == 1) {
                Main.gc.drawImage(Main.enemy1, x, y);
            }
        }
    }
}