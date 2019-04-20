package hu.oe.nik.szfmv.automatedcar.systemcomponents;

import hu.oe.nik.szfmv.automatedcar.model.WorldObject;
import hu.oe.nik.szfmv.automatedcar.model.objects.NpcCar;
import hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.VirtualFunctionBus;
import hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.packets.TempomatPacket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
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
        decideActive();
        if (active) {
            setSpeedLimit();
            keepSpeed();
        }
    }

    private void decideActive() {
        if (active == true &&
            (virtualFunctionBus.inputPacket.getBreakPedal() > 0 || virtualFunctionBus.brakePacket.isBrake())) {
            deactivate();
        } else {
            if (active == false && virtualFunctionBus.inputPacket.isAccOn())
                activate();
        }
    }


    private void setTarget() {
        //TODO kiválasztani a legközelebbit ami megegyező irányba halad, lekezelni ha üres
        List<NpcCar> objects = filterObjects(getObjectsFromRadarSensor());
        target = objects.get(0);
    }

    private List<NpcCar> filterObjects(List<WorldObject> objects) {
        List<NpcCar> cars = new ArrayList<>();
        for (WorldObject object : objects) {
            if (!(object instanceof NpcCar))
                cars.add((NpcCar) object);
        }
        return cars;
    }

    private List<WorldObject> getObjectsFromRadarSensor() {
        //TODO másik csapattal kommunikálni
        /*
        List<WorldObject> objects = virtualFunctionBus.radarPacket.getObjects();
        return objects;
        */
        return new ArrayList<>();
    }

    private void setSpeedLimit() {
        manualLimit = virtualFunctionBus.inputPacket.getAccSpeed();
        if (target == null)
            speedLimit = manualLimit;
        else
            Math.min(target.getPath().getMovementSpeed(), speedLimit);
    }

    private void activate() {
        //TODO meghívását handout alapján megcsinálni Listener szerint
        setTarget();
        active = true;
        packet.setActive(true);
    }

    private void deactivate() {
        //TODO meghívását handout alapján megcsinálni Listener szerint
        target = null;
        active = false;
        packet.setActive(false);
    }

    private void keepSpeed() {
        packet.setAccSpeed(speedLimit);
    }

}
