package game;

import javafx.scene.image.Image;

import java.io.File;


public class Alien{
    private final int type;
    int x;
    int y;
    boolean enabled = true;
    boolean isFiring = false;
    Bullet bullet = new Bullet('D');

    public Alien(int type_, int x_, int y_) {
        type = type_;
        x = x_;
        y = y_;
    }
    public void move(char direction, int speed){
        if(direction=='L')
            x-=speed;
        else if(direction=='R')
            x+=speed;
        else if(direction=='D')
            y+=speed*2;
    }
    public void update() {
        bullet.update();
        if (enabled) {
            if(isFiring){
                Main.gc.drawImage(Main.enemyFire,x,y);
            }
            else if (type == 1) {
                Main.gc.drawImage(Main.enemy1, x, y);
            }
        }
    }
}