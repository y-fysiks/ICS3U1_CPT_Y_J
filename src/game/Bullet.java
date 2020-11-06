package game;

import javafx.scene.paint.Color;

public class Bullet {
    private int pos; //the y position of the bullet
    boolean done = false;
    int x = -100;
    int y = -100;
    boolean enabled = false;
    char dir = 'U';

    public void update() {
        final int delta = 4;
        if (enabled) {
            Main.gc.setFill(Color.WHITE);
            Main.gc.rect(x - 1, y, 2, 6);
            //Player bullets
            if(dir == 'U') {
                y -= delta;
                if (y < 0) disable();
            }
            //Enemy bullets
            else {
                y += delta;
                if (y > 720) disable();
            }
        }
    }

    public void fire(int xPos, int yPos, char dir_) {
        x = xPos;
        y = yPos;
        dir = dir_;
        enabled = true;
    }

    public void disable() {
        enabled = false;
        x = -100;
        y = -100;
    }
}
