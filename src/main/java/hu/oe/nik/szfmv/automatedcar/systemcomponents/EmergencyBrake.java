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
    private static final int emergencyDistance_width = 51;
    private static final int warningDistance_width = 51;

    private Rectangle emergencyDistance;
    private Rectangle warningDistance;
    private AutomatedCar car;
    private double emergencyDistance_height;
    private double emergencyDistance_height_warning;
    private double current_acceleration;
    private long time_in_Milis_baseTime; // SystemStartTime
    private int time_in_Milis_last;
    private int time_in_Milis_curr;
    private double pastSpeed;
    private final BrakePacket packet;

    public EmergencyBrake(VirtualFunctionBus virtualFunctionBus, AutomatedCar car) {
        super(virtualFunctionBus);
        packet = new BrakePacket();
        virtualFunctionBus.brakePacket = packet;
        this.car = car;
        emergencyDistance_height = 0;
        emergencyDistance_height_warning = 0;
        emergencyDistance.width = emergencyDistance_width;
        warningDistance.width = warningDistance_width;
        current_acceleration = 0;
        time_in_Milis_baseTime = System.currentTimeMillis();
        time_in_Milis_last = (int)(System.currentTimeMillis() - time_in_Milis_baseTime);
        time_in_Milis_curr = (int)(System.currentTimeMillis() - time_in_Milis_baseTime);

    }

    @Override
    public void loop() {
        drawEmergencyDistances();
        checkEmergency();
    }

    private void drawEmergencyDistances() {
        //TODO
        emergencyDistance_height = calculateEmergencyDistance();
        emergencyDistance_height_warning = calculateWarningDistance(emergencyDistance_height);

    }

    private double calculateEmergencyDistance() { // Gives back the minimal distance at which the car can stop with 9 m/s^2 slowing power, the speed is in km/h
        // s = (a/2) * (t^2)
        // t = s/v
        // minimal breaking distance => s = 9/2 * (s/v)^2 = (v^2) / 4.5
        // s = v^2 / 4.5
        //TODO
        // Conversion between pixels and meters
        // scale between speed, distance and acceleration

        double speed_ms = car.getSpeed() * 3.6;
        double emergency_distance_minimal = speed_ms * speed_ms / 4.5;

        return emergency_distance_minimal;
    }

    private double calculateWarningDistance(double minimal_breaking_distance) {
        //TODO
        return minimal_breaking_distance * (5/3);
    }

    private void checkEmergency() {
        //TODO
        checkIntersection();
        warnDriver(false);
        applyBrake(false);
    }

    private boolean checkIntersection() {
        //TODO
        getObjectsFromRadarSensor();
        filterObjects();
        return false;
    }

    private double calculateCurrentAcceleration(){
        time_in_Milis_curr = (int)(System.currentTimeMillis() - time_in_Milis_baseTime);
        int dT = time_in_Milis_curr % time_in_Milis_last;
        if( dT>= 20){
            time_in_Milis_last  = time_in_Milis_curr;
            pastSpeed = car.getSpeed();
            return ((pastSpeed / car.getSpeed()) * 3.6) / (dT * 1000);
        }
        return 1;
    }


    private List<WorldObject> getObjectsFromRadarSensor() {
        //TODO
        return null;
    }

    private List<WorldObject> filterObjects() {
        //TODO
        return null;
    }

    private void warnDriver(boolean isWarning) {
        packet.setWarning(isWarning);
    }

    private void applyBrake(boolean isBrake) {
        packet.setBrake(isBrake);
    }

}
