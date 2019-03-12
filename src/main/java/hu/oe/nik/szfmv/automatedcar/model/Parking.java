package hu.oe.nik.szfmv.automatedcar.model;

public class Parking extends Crossable {

    public Parking(int x, int y, String imageFileName){
        super(x, y, imageFileName);
    }

    public Parking(){
        super(0,0,null);
    }
}