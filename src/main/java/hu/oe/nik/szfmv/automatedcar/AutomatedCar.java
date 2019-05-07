package hu.oe.nik.szfmv.automatedcar;

import hu.oe.nik.szfmv.automatedcar.model.World;
import hu.oe.nik.szfmv.automatedcar.model.WorldObject;
import hu.oe.nik.szfmv.automatedcar.systemcomponents.Collision;

import hu.oe.nik.szfmv.automatedcar.systemcomponents.AutomatedCarPos;
import hu.oe.nik.szfmv.automatedcar.systemcomponents.Driver;
import hu.oe.nik.szfmv.automatedcar.systemcomponents.Powertrain;
import hu.oe.nik.szfmv.automatedcar.systemcomponents.Steering;
import hu.oe.nik.szfmv.automatedcar.systemcomponents.Camera;
import hu.oe.nik.szfmv.automatedcar.systemcomponents.EmergencyBrake;
import hu.oe.nik.szfmv.automatedcar.systemcomponents.Tempomat;
import hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.VirtualFunctionBus;
import hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.packets.ReadOnlyCarPacket;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class AutomatedCar extends WorldObject {

    private static final Logger LOGGER = LogManager.getLogger();

    private final VirtualFunctionBus virtualFunctionBus = new VirtualFunctionBus();
    private final float deltaTime = 0.04f;

    private final float bumperAxleDistance = 12f;

    private float speed;
    private float wheelBase;
    private float steeringAngle;
    private float carHeading;  // in radians
    private Vector2D carLocation;
    private int health;
    private AutomatedCarPos positionTracker;
    private Tempomat tempomat;
    private EmergencyBrake brake;

    public AutomatedCar(int x, int y, String imageFileName) {
        super(x, y, imageFileName);

        new Driver(virtualFunctionBus);
        new Powertrain(virtualFunctionBus, this);
        new Steering(virtualFunctionBus);
        new Collision(virtualFunctionBus, this);
        positionTracker = new AutomatedCarPos(virtualFunctionBus);
        new Camera(virtualFunctionBus);
        brake = new EmergencyBrake(virtualFunctionBus, this);
        tempomat = new Tempomat(virtualFunctionBus, this);

        wheelBase = calculateWheelBase();
        carLocation = new Vector2D(x, y);
        health = 100;
    }

    public void setAllWorld(java.util.List<WorldObject> allWorld){
        tempomat.setAllWorld(allWorld);
    }

    public int getAutomatedCarHealth() {
        return this.health;
    }

    public void setAutomatedCarHealth(int health) {
        this.health = health;
        if (this.health < 0) {
            this.health = 0;
        }
    }

    public float getSpeed() {
        return speed;
    }

    public void drive() {
        if (!virtualFunctionBus.collisionPacket.isGameOver()) {
            virtualFunctionBus.loop();

            calculatePositionAndOrientation();
            updateCarPositionAndOrientation();
            positionTracker.handleLocationChange(new Point((int)carLocation.getX(),
                (int)carLocation.getY()), this.carHeading);

        }
    }

    public VirtualFunctionBus getVirtualFunctionBus() {
        return virtualFunctionBus;
    }

    private void calculatePositionAndOrientation() {
        speed = virtualFunctionBus.collisionPacket.isCollision() ?
            virtualFunctionBus.collisionPacket.getSpeedAfterCollision() :
            virtualFunctionBus.powertrainPacket.getSpeed();
        steeringAngle = virtualFunctionBus.steeringPacket.getSteeringAngle();

        Vector2D frontWheelPosition = carLocation.add(new Vector2D(Math.cos(carHeading), Math.sin(carHeading))
            .scalarMultiply(wheelBase / 2));
        Vector2D backWheelPosition = carLocation.subtract(new Vector2D(Math.cos(carHeading), Math.sin(carHeading))
            .scalarMultiply(wheelBase / 2));

        backWheelPosition = backWheelPosition
            .add(new Vector2D(Math.cos(carHeading), Math.sin(carHeading))
                .scalarMultiply(speed * deltaTime));
        frontWheelPosition = frontWheelPosition
            .add(new Vector2D(Math.cos(carHeading + Math.toRadians(steeringAngle)),
                Math.sin(carHeading + Math.toRadians(steeringAngle)))
                .scalarMultiply(speed * deltaTime));

        carLocation = frontWheelPosition.add(backWheelPosition).scalarMultiply(0.5);
        carHeading = (float) Math.atan2(frontWheelPosition.getY() - backWheelPosition.getY(),
            frontWheelPosition.getX() - backWheelPosition.getX());
    }

    private void updateCarPositionAndOrientation() {
        this.x = (int) carLocation.getX();
        this.y = (int) carLocation.getY();
        this.rotation = carHeading;
    }

    @Override
    public void generateShape() {
        AffineTransform tx = new AffineTransform();
        tx.rotate(this.getRotation() + Math.toRadians(90), this.x, this.y);

        this.shape = tx.createTransformedShape(
            new Rectangle(
                this.getX() - this.getWidth() / 2,
                this.getY() - this.getHeight() / 2,
                this.getWidth(), this.getHeight()));

    }

    public ReadOnlyCarPacket getCarValues() {
        return virtualFunctionBus.carPacket;
    }
    private float calculateWheelBase() {
        return this.height - bumperAxleDistance;
    }

}
