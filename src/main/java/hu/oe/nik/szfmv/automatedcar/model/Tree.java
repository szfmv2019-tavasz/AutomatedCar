package hu.oe.nik.szfmv.automatedcar.model;

public class Tree extends Collidable {

    private static double TREEDIAMETER = 20;

    public Tree(int x, int y, String imageFileName){
        super(x, y, imageFileName);
    }

    public Tree(){
        super(0,0,null);
    }

}