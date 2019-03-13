package hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.packets;

/**
 * Read only interface for the InputPacket class. Has only getters for the values.
 */
public interface ReadOnlyInputPacket {

    /**
     * Get the Gas pedal Position
     *
     * @return The Gas pedal Position value, in the range of 0 - 100
     */
    int getGasPedal();

    // TODO: A többi kimeneti adat...

}
