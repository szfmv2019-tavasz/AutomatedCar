package hu.oe.nik.szfmv.automatedcar.model;

public class Crosswalk extends Crossable{

    public Crosswalk(int x, int y, String imageFileName){
        super(x, y, imageFileName);
    }

    public Crosswalk(){
        super(0,0,null);
    }
}