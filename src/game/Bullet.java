package game;

import javafx.scene.paint.Color;

public class Bullet {
    int x = -100;
    int y = -100;
    boolean enabled = false;
    char dir;
    int delta = 8;

    public Bullet(char dir_) {
        dir = dir_;
    }
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