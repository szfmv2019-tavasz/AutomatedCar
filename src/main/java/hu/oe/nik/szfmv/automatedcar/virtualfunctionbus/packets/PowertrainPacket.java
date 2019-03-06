package hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.packets;

public class PowertrainPacket implements IPowertrainPacket {

    private int rpm;
    private int speed;

    @Override
    public int getSpeed() {
        return 0;
    }

    @Override
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    @Override
    public int getRPM() {
        return 0;
    }

    @Override
    public void setRPM(int rpm) {
        this.rpm = rpm;
    }
}
