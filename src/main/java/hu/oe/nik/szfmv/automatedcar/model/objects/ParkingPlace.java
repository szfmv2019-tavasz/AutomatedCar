package hu.oe.nik.szfmv.automatedcar.model.objects;

import hu.oe.nik.szfmv.automatedcar.model.ScriptedPath;
import hu.oe.nik.szfmv.automatedcar.model.Waypoint;
import hu.oe.nik.szfmv.automatedcar.model.WorldObject;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class ParkingPlace extends Crossable {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final int[] WAYPOINTS_X = new int[]{-69, -78, -82, -76, -43, -15, 22, 52, 67, 67, 67};
    private static final int[] WAYPOINTS_Y = new int[]{-108, -83, 0, 42, 91, 121, 145, 177, 200, 150, 150};
    private static final float[] WAYPOINTS_ROT = new float[]{-1.5919616f, -1.6931952f, -2.0228622f, -2.1854513f, -2.2121763f, -2.1591303f, -1.935371f, -1.7069743f, -1.57281f, -1.5728093f, -1.5728093f};

    private ScriptedPath pathUpper;
    private ScriptedPath pathLower;

    private boolean isUpperEmpty = false;
    private boolean isLowerEmpty = true;

    /**
     * Creates an object of the virtual world on the given coordinates with the given image.
     *
     * @param x             the initial x coordinate of the object
     * @param y             the initial y coordinate of the object
     * @param imageFileName the filename of the image representing the object in the virtual world
     */
    public ParkingPlace(int x, int y, String imageFileName) {
        super(x, y, imageFileName);
    }

    /**
     * Creates an object with default parameter values.
     */
    public ParkingPlace() {
        super(0, 0, "parking_space_parallel.png");
    }

    public void initializePaths(WorldObject target) {
        this.pathUpper = initializePath(target, 0);
        this.pathLower = initializePath(target, 300);
    }

    private ScriptedPath initializePath(WorldObject target, int offsetY) {
        ScriptedPath path = new ScriptedPath(target);
        int[] waypointsY = addConstantToElements(WAYPOINTS_Y, offsetY);
        List<Waypoint> waypoints = ScriptedPath.createWaypointList(WAYPOINTS_X, waypointsY, WAYPOINTS_ROT);
        transformAndRotateToObjectPositionAndRotation(waypoints, new Vector2D(x, y), rotation);
        path.setWaypoints(waypoints);
        path.setDirection(ScriptedPath.Direction.FORWARDS);
        path.setLoopType(ScriptedPath.LoopType.NONE);
        path.setUseWaypointRotations(true);
        path.setMovementSpeed(50);
        path.setWaypointReachedTreshold(10);
        path.init();

        return path;
    }

    private int[] addConstantToElements(int[] array, int value) {
        int[] result = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i] + value;
        }
        return result;
    }

    private void transformAndRotateToObjectPositionAndRotation(List<Waypoint> waypoints, Vector2D objectPosition, float objectRotation) {
        final Vector2D right = new Vector2D(1, 0);
        for (Waypoint waypoint :
            waypoints) {
            Vector2D position = waypoint.getPosition();
            float length = (float) Vector2D.distance(position, Vector2D.ZERO);
            float angle = (float) Vector2D.angle(position, right);
            if (position.getY() < 0) angle = -angle;
            float newAngle = angle + objectRotation;
            Vector2D newPosition = new Vector2D(Math.cos(newAngle), Math.sin(newAngle)).scalarMultiply(length).add(objectPosition);
            waypoint.setPosition(newPosition);
        }
    }

    public boolean isEmpty(ScriptedPath path) {
        if (pathUpper.equals(path)) return isUpperEmpty;
        else if (pathLower.equals(path)) return isLowerEmpty;
        else return false;
    }

    public Vector2D getCenterUpper() {
        Vector2D pivot = new Vector2D(this.x, this.y);
        float rot = -rotation + (float) Math.toRadians(90);
        Vector2D center = pivot.add(new Vector2D(Math.cos(rot), Math.sin(rot)).scalarMultiply(this.height / 4));
        center = center.add(new Vector2D(Math.cos(-rotation), Math.sin(-rotation)).scalarMultiply(this.width / 2));
        return center;
    }

    public Vector2D getCenterLower() {
        Vector2D pivot = new Vector2D(this.x, this.y);
        float rot = -rotation + (float) Math.toRadians(90);
        Vector2D center = pivot.add(new Vector2D(Math.cos(rot), Math.sin(rot)).scalarMultiply(3 * (this.height / 4)));
        center = center.add(new Vector2D(Math.cos(-rotation), Math.sin(-rotation)).scalarMultiply(this.width / 2));
        return center;
    }

    public ScriptedPath getPathUpper() {
        return pathUpper;
    }

    public ScriptedPath getPathLower() {
        return pathLower;
    }
}

