package game;

import javafx.scene.image.Image;

import java.io.File;

public class Player {
    int lives = 3;
    int x = 100;
    final int y = 660;
    int points = 0;
    Bullet bullet = new Bullet();
    int destroyed = 0;

    Image player = new Image(new File("player.png").toURI().toString(), 80, 36, true, false);
    Image playerDestroyed = new Image(new File("playerDestroyed.png").toURI().toString(), 80, 36, true, false);

    public void shoot() {
        bullet.fire(x, y, 'U');
    }

    public void update() {
        if (destroyed == 0) {
            Main.gc.drawImage(player, x, y);
            bullet.update();
        } else {
            Main.gc.drawImage(playerDestroyed, x, y);
            bullet.update();
            destroyed -= 1;
        }
    }
    public void destroy() {
        destroyed = 50;
        x = 100;
    }

    public boolean isHit(Bullet bullet) {
        return bullet.y > y + 10 && bullet.y < y + 36 && bullet.x > x && bullet.x < x + 80;
    }
}