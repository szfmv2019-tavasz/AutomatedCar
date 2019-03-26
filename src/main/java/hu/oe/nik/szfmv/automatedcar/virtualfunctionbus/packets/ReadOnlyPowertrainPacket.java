package hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.packets;

public interface ReadOnlyPowertrainPacket {

    float getSpeed();

    int getRPM();

    int getActualAutoGear();

}
