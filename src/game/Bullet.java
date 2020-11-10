package game;

import javafx.scene.paint.Color;

public class Bullet {
    int x = -100;
    int y = -100;
    boolean enabled = false;
    char dir;
    int delta = 12; //subtract delta if it is a player bullet, add delta if it is an enemy bullet
    //Constructor
    public Bullet(char dir_) {
        dir = dir_;
    }
    //updates the bullet sprite
    public void update() {
        if (enabled) {

            //Player bullets
            if(dir == 'U') {
                Main.gc.setFill(Color.GREEN);
                Main.gc.fillRect(x - 3, y, 6, 20);
                y -= delta;
                if (y < 0) disable();
            }
            //Enemy bullets
            else {
                Main.gc.setFill(Color.WHITE);
                Main.gc.fillRect(x - 2, y, 4, 10);
                y += delta;
                if (y > 720) disable();
            }
        }
    }
    //Fires a bullet from (xPos,yPos)
    public void fire(int xPos, int yPos) {
        if (!enabled) {
            x = xPos;
            y = yPos;
            enabled = true;
        }
    }
    //Disables and resets the bullet
    public void disable() {
        enabled = false;
        x = -100;
        y = -100;
    }
}