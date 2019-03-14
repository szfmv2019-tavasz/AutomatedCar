package hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.packets;

/**
 * Read only interface for the InputPacket class. Has only getters for the values.
 */
public interface ReadOnlyInputPacket {

    /**
     * Gear shift values:
     * P - Park
     * R - Reverse
     * N - Neutral
     * D - Drive
     */
    public enum GEARSHIFTVALUES {
        P, R, N, D
    }

    /**
     * Get the Gas pedal Position
     *
     * @return The Gas pedal Position value, in the range of 0 - 100
     */
    int getGasPedal();

    int getBreakPedal();

    int getSteeringWheel();

    GEARSHIFTVALUES getGearShift();

    boolean isSignalLeftTurn();

    public boolean isSignalRightTurn();

    public boolean isLaneKeepingOn();

    public boolean isParkingPilotOn();

    public int getAccSpeed();

    public double getAccDistance();

}
