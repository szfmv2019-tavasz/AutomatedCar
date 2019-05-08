package hu.oe.nik.szfmv.automatedcar.model;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class Waypoint {
    private Vector2D position;
    private float rotation;

    public Waypoint() {
        position = new Vector2D(0, 0);
        rotation = 0;
    }

    public Waypoint(Vector2D position, float rotation) {
        this.position = position;
        this.rotation = rotation;
    }

    public Vector2D getPosition() {
        return position;
    }

    public void setPosition(Vector2D position) {
        this.position = position;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }
}
