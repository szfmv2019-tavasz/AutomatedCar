package hu.oe.nik.szfmv.automatedcar.model.objects;

import hu.oe.nik.szfmv.automatedcar.model.Waypoint;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CrossWalk extends Crossable {

    private static final Logger LOGGER = LogManager.getLogger();
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

    public Waypoint getStartPoint() {
        float rot = -rotation + (float) Math.toRadians(90);
        Vector2D pivot = new Vector2D(this.x, this.y);
        Vector2D startPoint = pivot.add(new Vector2D(Math.cos(rot), Math.sin(rot)).scalarMultiply(this.height / 2));
        startPoint = startPoint.subtract(new Vector2D(Math.cos(-rotation), Math.sin(-rotation)).scalarMultiply(300));
        return new Waypoint(startPoint, 0f);
    }

    public Waypoint getEndPoint() {
        float rot = -rotation + (float) Math.toRadians(90);
        Vector2D pivot = new Vector2D(this.x, this.y);
        Vector2D endPoint = pivot.add(new Vector2D(Math.cos(rot), Math.sin(rot)).scalarMultiply(this.height / 2));
        endPoint = endPoint.add(new Vector2D(Math.cos(-rotation), Math.sin(-rotation))
            .scalarMultiply(this.width + 300));
        return new Waypoint(endPoint, 0f);
    }
}

