package hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.packets;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class SteeringPacket implements ReadOnlySteeringPacket {
    private Vector2D steeringVector;

    public SteeringPacket(Vector2D steeringVector) {
        this.steeringVector = steeringVector;
    }

    @Override
    public Vector2D getSteeringVector() {
        return steeringVector;
    }

    public void setSteeringVector(Vector2D steeringVector) {
        this.steeringVector = steeringVector;
    }
}
