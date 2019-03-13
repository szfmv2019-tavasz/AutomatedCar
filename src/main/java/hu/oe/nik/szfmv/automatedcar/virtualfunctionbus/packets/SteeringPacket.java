package hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.packets;

public class SteeringPacket implements ReadOnlySteeringPacket {
    private float steeringAngle;

    public SteeringPacket() {
    }

    @Override
    public float getSteeringAngle() {
        return steeringAngle;
    }

    public void setSteeringAngle(float steeringAngle) {
        this.steeringAngle = steeringAngle;
    }
}
