package hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.packets;

public class PowertrainPacket implements ReadOnlyPowertrainPacket {

    private int rpm;
    private int speed;

    public PowertrainPacket() {
        this.rpm = 0;
        this.speed = 0;
    }

    public PowertrainPacket(int rpm, int speed) {
        this.rpm = rpm;
        this.speed = speed;
    }

    @Override
    public int getSpeed() {
        return this.speed;
    }

    @Override
    public int getRPM() {
        return this.rpm;
    }

    public void setRpm(int rpm) {
        this.rpm = rpm;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
