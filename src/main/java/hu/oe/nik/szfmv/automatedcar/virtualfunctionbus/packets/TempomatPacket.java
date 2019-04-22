package hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.packets;

public class TempomatPacket implements ReadOnlyTempomatPacket {
    boolean active;
    float speed;
    double distance;

    public TempomatPacket() {
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public float getAccSpeed() {
        return speed;
    }

    public void setAccSpeed(float speed) {
        this.speed = speed;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

}
