package hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.packets;

public class PowertrainPacket implements ReadOnlyPowertrainPacket {

    private int rpm;
    private int speed;
    private  int gearshift;

    public PowertrainPacket() {
        this.rpm = 0;
        this.speed = 0;
        this.gearshift = 0;
    }

    public PowertrainPacket(int rpm, int speed, int gearshift) {
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

    public int getGearshift() {
        return this.gearshift;
    }


    public void setRpm(int rpm) {
        this.rpm = rpm;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setGearshift(int gearshift) {
        this.gearshift = gearshift;
    }
}
