package hu.oe.nik.szfmv.automatedcar.systemcomponents;

import hu.oe.nik.szfmv.automatedcar.model.WorldObject;
import hu.oe.nik.szfmv.automatedcar.model.objects.NpcCar;
import hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.VirtualFunctionBus;
import hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.packets.TempomatPacket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class Tempomat extends SystemComponent {
    private static final Logger LOGGER = LogManager.getLogger();

    boolean active;
    int speedLimit;
    int manualLimit;
    NpcCar target;

    private final TempomatPacket packet;

    public Tempomat(VirtualFunctionBus virtualFunctionBus) {
        super(virtualFunctionBus);
        packet = new TempomatPacket();
        virtualFunctionBus.tempomatPacket = packet;
    }

    @Override
    public void loop() {
        setSpeedLimit();
        keepSpeed();
    }

    private void setTarget() {
        //TODO
        getObjectsFromRadarSensor();
    }

    private List<WorldObject> getObjectsFromRadarSensor() {
        //TODO
        return null;
    }

    private void setSpeedLimit() {
        //TODO
    }

    private void setManualLimit() {
        //TODO
    }

    private void setActive() {
        //TODO
        setTarget();
    }

    private void keepSpeed() {
        //TODO
        accelerate();
        decelerate();
    }

    private void accelerate() {
        //TODO
    }

    private void decelerate() {
        //TODO
    }

}
