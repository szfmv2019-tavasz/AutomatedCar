package hu.oe.nik.szfmv.automatedcar.systemcomponents;

import hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.VirtualFunctionBus;
import hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.packets.SteeringPacket;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class Steering extends SystemComponent {
    private float steering;
    private int speed;

    private Vector2D steeringVector;

    public Steering(VirtualFunctionBus virtualFunctionBus) {
        super(virtualFunctionBus);
    }

    @Override
    public void loop() {
        getValuesFromInputPacket();
        getValuesFromPowertrainPacket();
        // steeringVector = calculateSteeringVector(steering, speed);
        createAndSendPacket();
    }

    private void getValuesFromInputPacket() {
        // InputPacket packet = virtualFunctionBus.inputPacket;
        // steering = packet.getSteering();
    }

    private void getValuesFromPowertrainPacket() {
        // PowertrainPacket packet = virtualFunctionBus.powertrainPacket;
        // speed = packet.getSpeed();
    }

    private void createAndSendPacket() {
        SteeringPacket packet = new SteeringPacket();
        packet.setSteeringVector(steeringVector);
        virtualFunctionBus.steeringPacket = packet;
    }
}
