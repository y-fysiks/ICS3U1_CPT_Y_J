package game;

public class Player {
    private int lives = 3;
    private int pos = 0;


    public void shoot() {

    }

    public void move(char direction) {
        if (direction == 'L') {
            if (this.pos > 0) {
                this.pos -= 10;
            }
        }
        if (direction == 'R') {
            if (this.pos < 1280) {
                this.pos += 10;
            }
        }
    }
}