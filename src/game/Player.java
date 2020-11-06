package game;

import javafx.scene.image.Image;


public class Player {
    int lives = 3;
    int x = 100;
    final int y = 660;
    int points = 0;
    Bullet bullet = new Bullet('U');
    int destroyed = 0;

    public void shoot() {
        bullet.fire(x, y);
    }

    public void update() {
        bullet.update();
        if (destroyed == 0) {
            Main.gc.drawImage(Main.player, x, y);
        } else {
            Main.gc.drawImage(Main.playerDestroyed, x, y);
            destroyed -= 1;
        }
    }
    public void destroy() {
        destroyed = 50;
        lives--;
        x = 100;
    }

    public boolean isHit(Bullet bullet) {
        return bullet.y > y + 10 && bullet.y < y + 36 && bullet.x > x && bullet.x < x + 80;
    }
}