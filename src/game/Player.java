package game;

import javafx.scene.image.Image;

import java.io.File;

public class Player {
    int lives = 3;
    int x = 100;
    final int y = 660;
    int points = 0;
    Bullet bullet = new Bullet();
    boolean destroyed = false;

    Image player = new Image(new File("player.png").toURI().toString(), 80, 36, true, false);
    Image playerDestroyed = new Image(new File("playerDestroyed.png").toURI().toString(), 80, 36, true, false);

    public void shoot() {
        bullet.fire(x, y, 'U');
    }

    public void update() {
        Main.gc.drawImage(player, x, y);

    }
}