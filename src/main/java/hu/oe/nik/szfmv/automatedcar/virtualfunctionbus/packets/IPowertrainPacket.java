package hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.packets;

public interface IPowertrainPacket {

    int getSpeed();

    void setSpeed(int speed);

    int getRPM();

    void setRPM(int rpm);
}
