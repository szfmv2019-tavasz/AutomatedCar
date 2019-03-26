package hu.oe.nik.szfmv.automatedcar.model.objects;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

@SuppressWarnings("ALL")
public class RoadSign extends Stationary {


    private static double D = 10;

    private final int substringStart = 15;
    private final int getSubstringEnd = 17;
    /**
     * Creates an object of the virtual world on the given coordinates with the given image.
     *
     * @param x             the initial x coordinate of the object
     * @param y             the initial y coordinate of the object
     * @param imageFileName the filename of the image representing the object in the virtual world
     */
    public RoadSign(int x, int y, String imageFileName) {
        super(x, y, imageFileName);
    }

    /**
     * Creates an object with default parameter values.
     */
    public RoadSign() {
        super(0, 0, null);
    }

    public int getSpeedLimit() {
        if (this.getImageFileName().equals("roadsign_priority_stop.png")) {
            return 0;
        } else {
            return isSpeedTable ();
        }

    }
    private int isSpeedTable() {
        try {
            return Integer.parseInt (this.imageFileName.substring (substringStart , getSubstringEnd));
        } catch (Exception e) {
            return - 1;
        }
    }

    @Override
    public void generateShape() {
        //Circle radius for shape property
        AffineTransform tx = new AffineTransform();
        tx.rotate(-this.getRotation(), this.getX(), this.getY());
        this.shape = tx.createTransformedShape(new Ellipse2D.Double(
            this.getX() + this.getWidth() / 2 - D / 2,
            this.getY() + this.getHeight() / 2 - D / 2,
            D, D));
    }
}
