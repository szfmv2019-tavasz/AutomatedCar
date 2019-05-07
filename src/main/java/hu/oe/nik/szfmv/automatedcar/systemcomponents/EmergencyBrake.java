package hu.oe.nik.szfmv.automatedcar.systemcomponents;

import hu.oe.nik.szfmv.automatedcar.AutomatedCar;
import hu.oe.nik.szfmv.automatedcar.model.World;
import hu.oe.nik.szfmv.automatedcar.model.WorldObject;
import hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.VirtualFunctionBus;
import hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.packets.BrakePacket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class EmergencyBrake extends SystemComponent {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final int emergencyDistance_width = 51;
    private static final int warningDistance_width = 51;

    private Shape rect_emergencyDistance;
    private Shape rect_warningDistance;
    private AutomatedCar car;
    private int emergencyDistance_height;
    private int emergencyDistance_height_warning;
    private double current_acceleration;
    private long time_in_Milis_baseTime; // SystemStartTime
    private int time_in_Milis_last;
    private int time_in_Milis_curr;
    private double pastSpeed;
    private float emergency_rotation;
    private final BrakePacket packet;

    public EmergencyBrake(VirtualFunctionBus virtualFunctionBus, AutomatedCar car) {
        super(virtualFunctionBus);
        packet = new BrakePacket();
        virtualFunctionBus.brakePacket = packet;
        this.car = car;
        emergencyDistance_height = 0;
        emergencyDistance_height_warning = 0;
        current_acceleration = 0;
        // getting current time in miliseconds to calculate acceleration later
        time_in_Milis_baseTime = System.currentTimeMillis();
        time_in_Milis_last = (int)(System.currentTimeMillis() - time_in_Milis_baseTime);
        time_in_Milis_curr = (int)(System.currentTimeMillis() - time_in_Milis_baseTime);
        rect_warningDistance = new Rectangle();
        rect_emergencyDistance = new Rectangle();
    }

    @Override
    public void loop() {
        drawEmergencyDistances();
        checkEmergency();
    }

    private void drawEmergencyDistances() {
        //TODO
        // emergencyDistance_height = calculateEmergencyDistance();
        emergencyDistance_height = car.getHeight() * 2;
        emergencyDistance_height_warning = (int)calculateWarningDistance(emergencyDistance_height);
        emergency_rotation = car.getRotation();


    }

    private int calculateEmergencyDistance() { // Gives back the minimal distance at which the car can stop with 9 m/s^2 slowing power, the speed is in km/h
        // s = (a/2) * (t^2)
        // t = s/v
        // minimal breaking distance => s = 9/2 * (s/v)^2 = (v^2) / 4.5
        // s = v^2 / 4.5
        //TODO
        // Conversion between pixels and meters
        // scale between speed, distance and acceleration

        double speed_ms = car.getSpeed() / 50 * 3.6;
        double emergency_distance_minimal = speed_ms * speed_ms / 4.5;

        return (int)emergency_distance_minimal * 50;
    }

    private void setRectranglePosition(int x, int y, Rectangle rect){

        generateShape();
    }

    private double calculateWarningDistance(double minimal_breaking_distance) {
        //TODO
        return minimal_breaking_distance * (5.0/3.0);
    }

    private double calculateCurrentAcceleration(){
        time_in_Milis_curr = (int)(System.currentTimeMillis() - time_in_Milis_baseTime);
        double dT = time_in_Milis_curr - time_in_Milis_last;
        if( dT>= 20){
            time_in_Milis_last  = time_in_Milis_curr;
            pastSpeed = car.getSpeed();
            return ((pastSpeed / car.getSpeed())*50 * 3.6) / (dT / 1000);
        }
        return 1;
    }


    public void generateShape() {
            AffineTransform tx = new AffineTransform();
            tx.rotate(this.emergency_rotation, car.getX(), car.getY());

            this.rect_emergencyDistance = tx.createTransformedShape(
                new Rectangle(
                    this.car.getX(),
                    this.car.getY(),
                    this.car.getWidth(), this.emergencyDistance_height));

    }
    
    private void checkEmergency() {
        //TODO
        boolean intersected = checkIntersection();
        warnDriver(intersected);
        applyBrake(false);
    }

    private boolean checkIntersection() {
        List<WorldObject> filtered = filterObjects();
        if (filtered.size() >= 1)
            return true;
        else
            return false;
    }

    private List<WorldObject> filterObjects() {
        //TODO
        List<WorldObject> all = World.getInstance().getWorldObjects();
        ArrayList<WorldObject> filtered = new ArrayList<WorldObject>();
        // Rectangle2D rekt = rect_warningDistance.getBounds();
        for (int i = 0; i < all.size(); i++) {
            WorldObject tmp = all.get(i);
            if(tmp.getShape().intersects(rect_warningDistance.getBounds())){
                filtered.add(tmp);
            }
        }
        return filtered;
    }

    private void warnDriver(boolean isWarning) {
        packet.setWarning(isWarning);
    }

    private void applyBrake(boolean isBrake) {
        packet.setBrake(isBrake);
    }

}
