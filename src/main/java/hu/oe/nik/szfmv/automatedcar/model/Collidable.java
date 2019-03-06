package hu.oe.nik.szfmv.automatedcar.model;

public abstract class Collidable extends WorldObject {

    public Collidable(int x, int y, String imageFileName){
        super(x, y, imageFileName);
    }

    public Collidable() {
        super(0, 0, null);
    }
}