package hu.oe.nik.szfmv.automatedcar.model.objects;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class CrossWalk extends Crossable {
    /**
     * Creates an object of the virtual world on the given coordinates with the given image.
     *
     *
     * @param x             the initial x coordinate of the object
     * @param y             the initial y coordinate of the object
     * @param imageFileName the filename of the image representing the object in the virtual world
     */
    public CrossWalk(int x, int y, String imageFileName) {
        super(x, y, imageFileName);
    }

    /**
     * Creates an object with default parameter values.
     */
    public CrossWalk() {
        super(0, 0, "crosswalk.png");
    }

    public Vector2D getStartPoint() {
        Vector2D startPoint = new Vector2D(this.x, this.y);
        return startPoint;
    }

    public Vector2D getEndPoint() {
        Vector2D endPoint = new Vector2D(this.x, this.y - this.width);

        return endPoint;
    }
}

