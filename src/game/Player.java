package game;

//Player class, stores information about the player
public class Player {
    int lives = 3;
    //player position
    int x = 100;
    final int y = 560;
    int points = 0;
    Bullet bullet = new Bullet('U');
    private int destroyed = 0;

    //method to shoot
    public void fire() {
        bullet.fire(x + 40, y - 10);
    }
    //updates the player sprite and bullet
    public void update() {
        bullet.update();
        if (destroyed == 0) {
            Main.gc.drawImage(Main.player, x, y);
        } else {
            Main.gc.drawImage(Main.playerDestroyed, x, y);
            destroyed -= 1;
        }
    }
    //when player gets hit by an alien bullet
    public void destroy() {
        destroyed = 50;
        lives--;
        x = 100;
    }
    //returns true if player was hit by bullet, false otherwise
    public boolean isHit(Bullet bullet) {
        return bullet.y > y + 10 && bullet.y < y + 36 && bullet.x > x && bullet.x < x + 80;
    }
    //movement
    public void moveRight() {
        if (x < 1280 - 90) x += 7;
    }
    public void moveLeft() {
        if (x > 10) x -= 7;
    }
}