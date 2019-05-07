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
import java.util.ArrayList;
import java.util.List;

public class EmergencyBrake extends SystemComponent {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final int emergencyDistanceWidth = 51;
    private static final int warningDistanceWidth = 51;

    private Shape rectEmergencyDistance;
    private Shape rectWarningDistance;
    private AutomatedCar car;
    private int emergencyDistanceHeight;
    private int warningDistanceHeight;
    private double currentAcceleration;
    private long timeInMilisBaseTime; // SystemStartTime
    private int timeInMilisLast;
    private int timeInMilisCurr;
    private double pastSpeed;
    private float emergencyRotation;
    private final BrakePacket packet;

    public EmergencyBrake(VirtualFunctionBus virtualFunctionBus, AutomatedCar car) {
        super(virtualFunctionBus);
        packet = new BrakePacket();
        virtualFunctionBus.brakePacket = packet;
        this.car = car;
        emergencyDistanceHeight = 0;
        warningDistanceHeight = 0;
        currentAcceleration = 0;
        // getting current time in miliseconds to calculate acceleration later
        timeInMilisBaseTime = System.currentTimeMillis();
        timeInMilisLast = (int) (System.currentTimeMillis() - timeInMilisBaseTime);
        timeInMilisCurr = (int) (System.currentTimeMillis() - timeInMilisBaseTime);
        rectWarningDistance = new Rectangle();
        rectEmergencyDistance = new Rectangle();
    }

    @Override
    public void loop() {
        drawEmergencyDistances();
        checkEmergency();
    }

    private void drawEmergencyDistances() {
        //TODO
        // emergencyDistance_height = calculateEmergencyDistance();
        emergencyDistanceHeight = car.getHeight() * 2;
        warningDistanceHeight = (int) calculateWarningDistance(emergencyDistanceHeight);
        emergencyRotation = car.getRotation();


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

        return (int) emergency_distance_minimal * 50;
    }

    private void setRectranglePosition(int x, int y, Rectangle rect) {

        generateShape();
    }

    private double calculateWarningDistance(double minimal_breaking_distance) {
        //TODO
        return minimal_breaking_distance * (5.0 / 3.0);
    }

    private double calculateCurrentAcceleration() {
        timeInMilisCurr = (int) (System.currentTimeMillis() - timeInMilisBaseTime);
        double dT = timeInMilisCurr - timeInMilisLast;
        if (dT >= 20) {
            timeInMilisLast = timeInMilisCurr;
            pastSpeed = car.getSpeed();
            return ((pastSpeed / car.getSpeed()) * 50 * 3.6) / (dT / 1000);
        }
        return 1;
    }


    public void generateShape() {
        AffineTransform tx = new AffineTransform();
        tx.rotate(this.emergencyRotation, car.getX(), car.getY());

        this.rectEmergencyDistance = tx.createTransformedShape(
            new Rectangle(
                this.car.getX(),
                this.car.getY(),
                this.car.getWidth(), this.emergencyDistanceHeight));

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
            if (tmp.getShape().intersects(rectWarningDistance.getBounds())) {
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
