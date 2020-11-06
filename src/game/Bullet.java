package game;

import javafx.scene.paint.Color;

public class Bullet {
    private int pos; //the y position of the bullet
    boolean done = false;
    int x = -100;
    int y = -100;
    boolean enabled = false;
    char dir;

    public Bullet(char dir_) {
        dir = dir_;
}
    public void update() {
        final int delta = 8;
        if (enabled) {
            Main.gc.setFill(Color.WHITE);
            Main.gc.fillRect(x - 2, y, 4, 10);
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

    public void fire(int xPos, int yPos) {
        if (!enabled) {
            x = xPos;
            y = yPos;
            enabled = true;
        }
    }
    public void disable() {
        enabled = false;
        x = -100;
        y = -100;
    }
}
