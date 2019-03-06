package hu.oe.nik.szfmv.automatedcar.model;

public abstract class Crossable extends WorldObject{

    public Crossable(int x, int y, String imageFileName){
        super(x, y, imageFileName);
    }
    
    public Crossable(){
        super(0, 0, null);
    }
}