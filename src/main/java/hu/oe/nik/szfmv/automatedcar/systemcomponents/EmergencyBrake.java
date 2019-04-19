package hu.oe.nik.szfmv.automatedcar.systemcomponents;

import hu.oe.nik.szfmv.automatedcar.AutomatedCar;
import hu.oe.nik.szfmv.automatedcar.model.WorldObject;
import hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.VirtualFunctionBus;
import hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.packets.BrakePacket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.List;

public class EmergencyBrake extends SystemComponent {
    private static final Logger LOGGER = LogManager.getLogger();

    private Rectangle emergencyDistance;
    private Rectangle warningDistance;
    private AutomatedCar car;
    private double emergencyDistance_height;
    private double emergencyDistance_height_warning;

    private final BrakePacket packet;

    public EmergencyBrake(VirtualFunctionBus virtualFunctionBus, AutomatedCar car) {
        super(virtualFunctionBus);
        packet = new BrakePacket();
        virtualFunctionBus.brakePacket = packet;
        this.car = car;
        emergencyDistance.width = car.getWidth();
        emergencyDistance_height = 0;
        emergencyDistance_height_warning = 0;
        warningDistance.width = car.getWidth();
    }

    @Override
    public void loop() {
        drawEmergencyDistances();
        checkEmergency();
    }

    private void drawEmergencyDistances() {
        //TODO
        emergencyDistance_height = calculateEmergencyDistance();
        emergencyDistance_height_warning = calculateWarningDistance();

    }

    private double calculateEmergencyDistance() { // Gives back the minimal distance at which the car can stop with 9 m/s^2 slowing power, the speed is in km/h
        // s = (a/2) * (t^2)
        // t = s/v
        // minimal breaking distance => s = 9/2 * (s/v)^2 = (v^2) / 4.5
        // s = v^2 / 4.5
        double speed_ms = car.getSpeed() * 3.6;
        return speed_ms * speed_ms / 4.5;
    }

    private double calculateWarningDistance() {
        //TODO
        return 0;
    }

    private void checkEmergency() {
        //TODO
        checkIntersection();
        warnDriver();
        applyBrake();
    }

    private boolean checkIntersection() {
        //TODO
        getObjectsFromRadarSensor();
        filterObjects();
        return false;
    }

    private List<WorldObject> getObjectsFromRadarSensor() {
        //TODO
        return null;
    }

    private List<WorldObject> filterObjects() {
        //TODO
        return null;
    }

    private void warnDriver() {
        //TODO
    }

    private void applyBrake() {
        //TODO
    }

}
