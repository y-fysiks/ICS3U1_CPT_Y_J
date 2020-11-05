package game;

public class Bullet {
    private int pos; //the y position of the bullet
    boolean done = false;
    public Bullet(int pos){
        this.pos = pos;
    }
    public void move(char direction){
        int delta = 0;
        if(direction=='U'){ //Player bullets
            delta=-10;
        }
        else if(direction=='D') { //Enemy bullets
            delta+=10;
        }
        while(!collide()){
            pos+=delta;
        }
    }
    public boolean collide(){
        //If bullet goes out of bounds or hits an enemy/obstacle, return true
        if(pos<0||pos>720)
            return true;
        //TODO handle case of bullet hitting enemy/obstacle

    }
}
