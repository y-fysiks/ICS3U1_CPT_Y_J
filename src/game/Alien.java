package game;


public class Alien{
    int type; //If alien type is 1, it is a regular alien. Otherwise, it is a mothership.
    int x;
    int y;
    boolean enabled = true;
    boolean isFiring = false;
    Bullet bullet = new Bullet('D');
    //Constructor
    public Alien(int type_, int x_, int y_) {
        type = type_;
        x = x_;
        y = y_;
        bullet.delta = 5;
    }
    //Move an individual alien in a certain direction.
    public void move(char direction, int speed){
        if(direction=='L')
            x-=speed;
        else if(direction=='R')
            x+=speed;
        else if(direction=='D')
            y+=speed*2;
    }
    //Check if an alien has been hit by a bullet.
    public boolean isHit(Bullet bullet) {
        return enabled && bullet.y > y && bullet.y < y + 24 && bullet.x + 2 > x && bullet.x - 2 < x + 33;
    }
    //Update alien and alien bullet
    public void update() {
        bullet.update();
        if (enabled) {
            if(isFiring){
                Main.gc.drawImage(Main.enemyFire,x,y);
            }
            else if (type == 1) {
                Main.gc.drawImage(Main.enemy1, x, y);
            }
            else if (type == 2){
                Main.gc.drawImage(Main.bossEnemy,x,y);
            }
        }
    }
}