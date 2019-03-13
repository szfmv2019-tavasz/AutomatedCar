package hu.oe.nik.szfmv.automatedcar.systemcomponents;

import hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.VirtualFunctionBus;
import hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.packets.SteeringPacket;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class Steering extends SystemComponent {
    private int steeringWheel;
    private float steeringAngle;

    public Steering(VirtualFunctionBus virtualFunctionBus) {
        super(virtualFunctionBus);
    }

    @Override
    public void loop() {
        getValuesFromInputPacket();
        calculateSteeringAngleFromWheelPosition();
        createAndSendPacket();
    }

    private void getValuesFromInputPacket() {
        // InputPacket packet = virtualFunctionBus.inputPacket;
        // steeringWheel = packet.getSteering();
    }

    private void calculateSteeringAngleFromWheelPosition() {

    }

    private void createAndSendPacket() {
        SteeringPacket packet = new SteeringPacket();
        packet.setSteeringAngle(steeringAngle);
        virtualFunctionBus.steeringPacket = packet;
    }
}
