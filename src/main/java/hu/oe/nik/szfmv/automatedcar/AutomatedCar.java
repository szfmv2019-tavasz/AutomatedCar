package hu.oe.nik.szfmv.automatedcar;

import hu.oe.nik.szfmv.automatedcar.model.WorldObject;
import hu.oe.nik.szfmv.automatedcar.systemcomponents.Driver;
import hu.oe.nik.szfmv.automatedcar.systemcomponents.Powertrain;
import hu.oe.nik.szfmv.automatedcar.systemcomponents.Steering;
import hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.VirtualFunctionBus;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AutomatedCar extends WorldObject {

    private static final Logger LOGGER = LogManager.getLogger();

    private final VirtualFunctionBus virtualFunctionBus = new VirtualFunctionBus();
    private final float deltaTime = 0.04f;

    private float speed;
    private float wheelBase;
    private float steeringAngle;
    private float carHeading;  // in radians
    private Vector2D carLocation;

    public AutomatedCar(int x, int y, String imageFileName) {
        super(x, y, imageFileName);

        new Driver(virtualFunctionBus);
        new Powertrain(virtualFunctionBus);
        new Steering(virtualFunctionBus);

        wheelBase = calculateWheelBase();
        carLocation = new Vector2D(x, y);
    }

    public void drive() {
        virtualFunctionBus.loop();

        calculatePositionAndOrientation();
    }

    public VirtualFunctionBus getVirtualFunctionBus() {
        return virtualFunctionBus;
    }

    private void calculatePositionAndOrientation() {
        speed = virtualFunctionBus.powertrainPacket.getSpeed();
        steeringAngle = virtualFunctionBus.steeringPacket.getSteeringAngle();

        Vector2D frontWheelPosition = carLocation.add(new Vector2D(Math.cos(carHeading), Math.sin(carHeading))
                .scalarMultiply(wheelBase / 2));
        Vector2D backWheelPosition = carLocation.subtract(new Vector2D(Math.cos(carHeading), Math.sin(carHeading))
                .scalarMultiply(wheelBase / 2));

        backWheelPosition = backWheelPosition
                .add(new Vector2D(Math.cos(carHeading), Math.sin(carHeading))
                        .scalarMultiply(speed * deltaTime));
        frontWheelPosition = frontWheelPosition
                .add(new Vector2D(Math.cos(carHeading + steeringAngle), Math.sin(carHeading + steeringAngle))
                        .scalarMultiply(speed * deltaTime));

        carLocation = frontWheelPosition.add(backWheelPosition).scalarMultiply(0.5);
        carHeading = (float) Math.atan2(frontWheelPosition.getY() - backWheelPosition.getY(), frontWheelPosition.getX() - backWheelPosition.getX());

        this.x = (int) carLocation.getX();
        this.y = (int) carLocation.getY();

        rotation = (float) Math.toDegrees(carHeading);
    }

    private float calculateWheelBase() {
        return 120;
    }
}
