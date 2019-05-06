package hu.oe.nik.szfmv.automatedcar.model;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class ScriptedPath {
    private final Logger LOGGER = LogManager.getLogger();

    public enum LoopType {
        NONE, PINGPONG, LOOP
    }

    public enum Direction {
        FORWARDS, BACKWARDS
    }

    private final float deltaTime = 0.04f;

    private float waypointReachedTreshold = 50f;

    //private List<Vector2D> waypoints = new ArrayList<>();
    private List<Waypoint> waypoints = new ArrayList<>();
    private LoopType loopType = LoopType.NONE;
    private Direction direction = Direction.FORWARDS;

    private WorldObject worldObject;
    private Vector2D currentPosition;
    private boolean isStopped = true;
    private float movementSpeed = 10f;
    private float rotation = 0f;
    private boolean useWaypointRotations = false;

    private int targetId;
    //private Vector2D targetWaypoint;
    private Waypoint previousWaypoint;
    private Waypoint targetWaypoint;

    public ScriptedPath(WorldObject worldObject) {
        this.worldObject = worldObject;
    }

    public void start() {
        isStopped = false;
    }

    public void stop() {
        isStopped = true;
    }

    public void reset() {
        currentPosition = new Vector2D(worldObject.getX(), worldObject.getY());
        targetId = 0;
        previousWaypoint = new Waypoint(currentPosition, worldObject.getRotation());
        targetWaypoint = waypoints.get(targetId);
    }

    public void init() {
        currentPosition = new Vector2D(worldObject.getX(), worldObject.getY());
        targetId = 0;
        previousWaypoint = new Waypoint(currentPosition, worldObject.getRotation());
        targetWaypoint = waypoints.get(targetId);
    }

    public void loop() {
        if (!isStopped) {
            moveObjectTowardsWaypoint();
            rotateObject();
            applyPositionAndRotationToObject();
            if (isTargetWaypointReached()) {
                getNewTargetWaypoint();
                if (isPathFinished()) {
                    correctFinalPosition();
                    isStopped = true;
                }
            }
        }
    }

    public static List<Waypoint> createWaypointList(int[] x, int[] y, float[] rotation) {
        int count = x.length;

        List<Waypoint> waypoints = new ArrayList<>(count);

        for (int i = 0; i < count; i++) {
            Waypoint waypoint = new Waypoint(new Vector2D(x[i], y[i]), rotation[i]);
            waypoints.add(waypoint);
        }

        return waypoints;
    }

    private void applyPositionAndRotationToObject() {
        worldObject.setX((int) currentPosition.getX());
        worldObject.setY((int) currentPosition.getY());
        worldObject.setRotation(rotation);
    }

    private void moveObjectTowardsWaypoint() {
        Vector2D directionVector = targetWaypoint.getPosition().subtract(currentPosition).normalize();
        Vector2D speedVector = directionVector.scalarMultiply(movementSpeed * deltaTime);
        currentPosition = currentPosition.add(speedVector);
    }

    private void rotateObjectTowardsWaypoint() {
        Vector2D targetPosition = targetWaypoint.getPosition();
        rotation = (float) Math.atan2(targetPosition.getY() - currentPosition.getY(),
            targetPosition.getX() - currentPosition.getX());
    }

    private float lerp(float a, float b, float f) {
        return (a * (1.0f - f)) + (b * f);
    }

    private void rotateObjectToWaypointRotation() {
        float totalDistance = (float) targetWaypoint.getPosition().distance(previousWaypoint.getPosition());
        float coveredDistance = (float) previousWaypoint.getPosition().distance(currentPosition);
        float fraction = coveredDistance / totalDistance;
        rotation = lerp(previousWaypoint.getRotation(), targetWaypoint.getRotation(), fraction);
    }

    private void rotateObject() {
        if (useWaypointRotations) {
            rotateObjectToWaypointRotation();
        } else {
            rotateObjectTowardsWaypoint();
        }
    }

    private boolean isTargetWaypointReached() {
        Vector2D targetPosition = targetWaypoint.getPosition();
        return Vector2D.distance(currentPosition, targetPosition) <= waypointReachedTreshold;
    }

    private void correctFinalPosition() {
        currentPosition = targetWaypoint.getPosition();
    }

    private void getNewTargetWaypoint() {
        if (direction == Direction.FORWARDS) {
            getNewForwardWaypointId();
        } else {
            getNewBackwardWaypointId();
        }
        previousWaypoint = targetWaypoint;
        targetWaypoint = waypoints.get(targetId);
    }

    private void getNewForwardWaypointId() {
        if (targetId + 1 < waypoints.size()) {
            targetId += 1;
        } else {
            if (loopType == LoopType.LOOP) {
                targetId = 0;
            }
            if (loopType == LoopType.PINGPONG) {
                targetId -= 1;
                direction = Direction.BACKWARDS;
            }
        }
    }

    private void getNewBackwardWaypointId() {
        if (targetId - 1 >= 0) {
            targetId -= 1;
        } else {
            if (loopType == LoopType.LOOP) {
                targetId = waypoints.size() - 1;
            }
            if (loopType == LoopType.PINGPONG) {
                targetId += 1;
                direction = Direction.FORWARDS;
            }
        }
    }

    private boolean isPathFinished() {
        return loopType == LoopType.NONE && targetId == waypoints.size() - 1;
    }

    public List<Waypoint> getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(List<Waypoint> waypoints) {
        this.waypoints = waypoints;
    }

    public LoopType getLoopType() {
        return loopType;
    }

    public void setLoopType(LoopType loopType) {
        this.loopType = loopType;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public float getMovementSpeed() {
        return movementSpeed;
    }

    public void setMovementSpeed(float movementSpeed) {
        this.movementSpeed = movementSpeed;
    }

    public boolean isUseWaypointRotations() {
        return useWaypointRotations;
    }

    public void setUseWaypointRotations(boolean useWaypointRotations) {
        this.useWaypointRotations = useWaypointRotations;
    }

    public float getWaypointReachedTreshold() {
        return waypointReachedTreshold;
    }

    public void setWaypointReachedTreshold(float waypointReachedTreshold) {
        this.waypointReachedTreshold = waypointReachedTreshold;
    }
}
