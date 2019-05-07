package hu.oe.nik.szfmv.automatedcar.systemcomponents;

import hu.oe.nik.szfmv.automatedcar.model.ScriptedPath;
import hu.oe.nik.szfmv.automatedcar.model.World;
import hu.oe.nik.szfmv.automatedcar.model.WorldObject;
import hu.oe.nik.szfmv.automatedcar.model.objects.ParkingPlace;
import hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.VirtualFunctionBus;
import hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.packets.*;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class ParkingPilot extends SystemComponent {

    private static final Logger LOGGER = LogManager.getLogger();

    private WorldObject car;
    private ScriptedPath path;

    private ReadOnlyCarPacket carPacket;
    private ReadOnlyInputPacket inputPacket;
    private ReadOnlyPowertrainPacket powertrainPacket;
    private ReadOnlySteeringPacket steeringPacket;

    private List<ParkingPlace> parkingPlaceList;
    private float searchDistance = 140f;
    private float searchRadius = 50f;

    private boolean isSearching;
    private ParkingPlace targetParkingPlace;

    private boolean isWorking = false;

    public ParkingPilot(VirtualFunctionBus virtualFunctionBus, WorldObject car) {
        super(virtualFunctionBus);
        this.car = car;
        loadParkingPlaces();
    }

    @Override
    public void loop() {
        readPacketsFromBus();
        isWorking = false;

        if (isParkingPilotOn() && !foundFreePlace()) {
            isSearching = true;
        }

        if (isParkingPilotOn()) {
            if (isSearching) {
                findFreeParkingPlace();
                if (foundFreePlace()) {
                    path.start();
                }
            } else {
                if (foundFreePlace()) {
                    isWorking = true;
                    path.loop();
                }
            }
        }

        if (!isParkingPilotOn() && path != null) {
            stop();
        }

        createAndSendPackets();
    }

    private void readPacketsFromBus() {
        carPacket = virtualFunctionBus.carPacket;
        inputPacket = virtualFunctionBus.inputPacket;
        powertrainPacket = virtualFunctionBus.powertrainPacket;
        steeringPacket = virtualFunctionBus.steeringPacket;
    }

    private void createAndSendPackets() {
        ParkingPilotPacket parkingPilotPacket = new ParkingPilotPacket();
        parkingPilotPacket.setWorking(isWorking);
        virtualFunctionBus.parkingPilotPacket = parkingPilotPacket;
    }

    private void loadParkingPlaces() {
        List<WorldObject> all = World.getInstance().getWorldObjects();
        List<ParkingPlace> parkingPlaces = new ArrayList<>();
        for (WorldObject worldObject : all) {
            if (worldObject instanceof ParkingPlace) {
                ParkingPlace parkingPlace = (ParkingPlace) worldObject;
                parkingPlaces.add(parkingPlace);
            }
        }
        parkingPlaceList = parkingPlaces;
    }

    private boolean isParkingPilotOn() {
        return inputPacket.isParkingPilotOn();
    }

    public void findFreeParkingPlace() {
        ScriptedPath parkingPlace = findParkingPlaceNextToCar();
        if (parkingPlace != null && this.targetParkingPlace.isEmpty(parkingPlace)) {
            loadPathFromParkingPlace(parkingPlace);
            isSearching = false;
        }
    }

    private void loadPathFromParkingPlace(ScriptedPath parkingPlace) {
        path = parkingPlace;
        path.setWorldObject(car);
        path.init();
    }

    private ScriptedPath findParkingPlaceNextToCar() {
        Vector2D searchCenter = getSearchCenter(searchDistance);
        ScriptedPath parkingPlace = findParkingPlaceInRange(searchCenter, searchRadius);
        return parkingPlace;
    }

    private Vector2D getSearchCenter(float searchDistance) {
        Vector2D position = new Vector2D(carPacket.getPosition().getX(), carPacket.getPosition().getY());
        float rotation = carPacket.getRotation() - (float) Math.toRadians(270);
        Vector2D searchCenter = position.add(new Vector2D(Math.cos(rotation), Math.sin(rotation)).scalarMultiply(searchDistance));
        return searchCenter;
    }

    private ScriptedPath findParkingPlaceInRange(Vector2D center, float radius) {
        float closestDistance = radius;
        ScriptedPath closestParkingPlace = null;
        for (ParkingPlace parkingPlace : parkingPlaceList) {
            float distance = (float) Vector2D.distance(center, parkingPlace.getCenterUpper());
            if (distance < closestDistance) {
                closestDistance = distance;
                closestParkingPlace = parkingPlace.getPathUpper();
                this.targetParkingPlace = parkingPlace;
            }

            distance = (float) Vector2D.distance(center, parkingPlace.getCenterLower());
            if (distance < closestDistance) {
                closestDistance = distance;
                closestParkingPlace = parkingPlace.getPathLower();
                this.targetParkingPlace = parkingPlace;
            }
        }
        return closestParkingPlace;
    }

    private boolean foundFreePlace() {
        return path != null;
    }

    public void start() {
        isSearching = true;
    }

    public void stop() {
        if (path != null) {
            path.stop();
            path.reset();
            path = null;
        }

    }

    public void setPath(ScriptedPath path) {
        this.path = path;
    }
}
