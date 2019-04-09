package hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.packets;

public class PowertrainPacket implements ReadOnlyPowertrainPacket {

    private int rpm;
    private float speed;

    public PowertrainPacket() {
    }

    public PowertrainPacket(int rpm, float speed) {
        this.rpm = rpm;
        this.speed = speed;
    }

    @Override
    public float getSpeed() {
        return this.speed;
    }

    @Override
    public int getRPM() {
        return this.rpm;
    }

    public void setRpm(int rpm) {
        this.rpm = rpm;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
