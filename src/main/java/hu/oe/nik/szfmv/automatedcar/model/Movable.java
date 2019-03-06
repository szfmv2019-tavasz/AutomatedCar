package hu.oe.nik.szfmv.automatedcar.model;

public class Movable extends Collidable{

    public Movable(int x, int y, String imageFileName){
        super(x, y, imageFileName);
    }

    public Movable(){
        super(0,0,null);
    }
}