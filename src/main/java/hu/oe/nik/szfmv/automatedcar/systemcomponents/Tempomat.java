package hu.oe.nik.szfmv.automatedcar.systemcomponents;

import hu.oe.nik.szfmv.automatedcar.AutomatedCar;
import hu.oe.nik.szfmv.automatedcar.model.World;
import hu.oe.nik.szfmv.automatedcar.model.WorldObject;
import hu.oe.nik.szfmv.automatedcar.model.objects.NpcCar;
import hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.VirtualFunctionBus;
import hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.packets.TempomatPacket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jfree.data.xy.Vector;

import java.util.ArrayList;
import java.util.List;

public class Tempomat extends SystemComponent {
    private static final Logger LOGGER = LogManager.getLogger();

    private boolean active;
    private int speedLimit;
    private int manualLimit;
    private NpcCar target;
    private AutomatedCar car;

    private final TempomatPacket packet;

    public Tempomat(VirtualFunctionBus virtualFunctionBus, AutomatedCar car) {
        super(virtualFunctionBus);
        packet = new TempomatPacket();
        virtualFunctionBus.tempomatPacket = packet;
        this.car = car;
    }

    @Override
    public void loop() {
        decideActive();
        if (active) {
            setSpeedLimit();
            keepSpeed();
            updateDistance();
        }
    }

    private void decideActive() {
        if (active &&
            (virtualFunctionBus.inputPacket.getBreakPedal() > 0
                || virtualFunctionBus.brakePacket.isBrake()
                || virtualFunctionBus.inputPacket.getGasPedal() > 0))
            /*
            System.out.println("isBrake "+virtualFunctionBus.brakePacket.isBrake());
            System.out.println("brake "+virtualFunctionBus.inputPacket.getBreakPedal());
            System.out.println("gas "+virtualFunctionBus.inputPacket.getGasPedal());
            */
            deactivate();
        else {
            if (!active && virtualFunctionBus.inputPacket.isAccOn())
                activate();

        }
    }

    private void setTarget() {
        List<WorldObject> all = World.getInstance().getWorldObjects();
        List<NpcCar> objects = filterObjects(all);
        if (objects == null || objects.isEmpty()) {
            System.out.println("objects null");
            LOGGER.info("For setting Target filtered WorldObjects is NULL");
            target = null;
        } else {
            System.out.println("objects not null");
            NpcCar other;
            int index = 0;
            double distance = Double.MAX_VALUE;
            for (int i = 0; i < objects.size(); i++) {
                other = objects.get(i);
                System.out.println("Rotations: " + other.getRotation() + ", " + car.getRotation());

                if (car.getRotation() < other.getRotation() + 0.2 &&
                    car.getRotation() > other.getRotation() - 0.2) {
                    Double d = getDistance(other);
                    LOGGER.info("NpcCar found with Rotation: " + other.getRotation() + ", Distance: " + d);
                    if (d < distance) {
                        index = i;
                        distance = d;
                    }
                }
            }
            if (distance == Double.MAX_VALUE) {
                LOGGER.info("For setting Target no acceptable car");
                target = null;
            } else {
                LOGGER.info("Setting Target");
                target = objects.get(index);
            }
        }
        if (target != null)
            System.out.println("TARGET SET");
    }

    private double getDistance(NpcCar otherCar) {
        Vector v = new Vector(otherCar.getX() - car.getX(), otherCar.getY() - car.getY());
        return v.getLength();
    }

    private List<NpcCar> filterObjects(List<WorldObject> objects) {
        List<NpcCar> cars = new ArrayList<>();
        for (WorldObject object : objects) {
            if ((object instanceof NpcCar))
                cars.add((NpcCar) object);
        }
        return cars;
    }

    private List<WorldObject> getObjectsFromRadarSensor() {
        //TO DO másik csapattal kommunikálni
        /*
        List<WorldObject> objects = virtualFunctionBus.radarPacket.getObjects();
        return objects;
        */
        return new ArrayList<>();
    }

    private void setSpeedLimit() {
        manualLimit = virtualFunctionBus.inputPacket.getAccSpeed();
        if (target == null) {
            speedLimit = manualLimit;
            System.out.println("Set Manual Limit" + manualLimit);
        } else {
            speedLimit = Math.round(Math.min(target.getPath().getMovementSpeed(), manualLimit));
            System.out.println("Set Limit: " + speedLimit);
        }
        LOGGER.info("Speed Limit set to " + speedLimit);
    }

    private void activate() {
        System.out.println("ACC Activating");
        setTarget();
        active = true;
        LOGGER.info("Activating ACC");
        packet.setActive(true);
    }

    private void deactivate() {
        System.out.println("ACC Deactivating");
        target = null;
        active = false;
        LOGGER.info("Deactivating ACC");
        packet.setActive(false);
    }

    private void keepSpeed() {
        packet.setAccSpeed(speedLimit);
    }

    private void updateDistance() {
        if (target == null)
            packet.setDistance(0);
        else
            packet.setDistance(getDistance(target));
    }

}
