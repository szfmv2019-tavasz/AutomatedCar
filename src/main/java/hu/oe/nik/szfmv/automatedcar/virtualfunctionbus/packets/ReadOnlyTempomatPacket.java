package hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.packets;

public interface ReadOnlyTempomatPacket {

    boolean isActive();

    float getAccSpeed();
}
