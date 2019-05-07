package hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.packets;

public class ParkingPilotPacket implements ReadOnlyParkingPilotPacket {
    private boolean isWorking;

    @Override
    public boolean isWorking() {
        return this.isWorking;
    }

    public void setWorking(boolean working) {
        isWorking = working;
    }
}
