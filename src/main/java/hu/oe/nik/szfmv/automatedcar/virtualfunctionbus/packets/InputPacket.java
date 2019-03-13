package hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.packets;

/**
 * Packet class containing the output values of the keyboard input component.
 */
public class InputPacket implements ReadOnlyInputPacket {

    private int gasPedal = 0;

    // TODO: A többi kimeneti adat...



    public int getGasPedal() {
        return gasPedal;
    }

    public void setGasPedal(int gasPedal) {
        this.gasPedal = gasPedal;
    }
}
