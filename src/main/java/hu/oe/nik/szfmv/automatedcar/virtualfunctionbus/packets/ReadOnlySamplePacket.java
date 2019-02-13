package hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.packets;

public interface ReadOnlySamplePacket {

    /**
     * @return Gaspedal Position
     * Get the Gaspedal Position
     */
    int getGaspedalPosition();

    int getBreakpedalPosition();

    int getWheelPosition();

    String getGear();

}
