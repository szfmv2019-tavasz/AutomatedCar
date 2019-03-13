package hu.oe.nik.szfmv.automatedcar.systemcomponents;

import hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.VirtualFunctionBus;
import hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.packets.PowertrainPacket;
import hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.packets.SteeringPacket;

public class Powertrain extends SystemComponent {

    private final PowertrainPacket powertrainPacket;

    private enum GearBox {
        P, R, N, D
    }

    private int[] Gearshift = new int []{ 0,1,2,3,4,5};

    private  int shift;
    private int rpm;
    private int speed;
    private double throttlePower;
    private double brakePower;

    public Powertrain(VirtualFunctionBus virtualFunctionBus) {
        super(virtualFunctionBus);

        this.powertrainPacket = new PowertrainPacket();
        virtualFunctionBus.powertrainPacket = powertrainPacket;

        this.rpm = 0;
        this.throttlePower = 0;
        this.brakePower = 0;
        this.speed = 0;
    }

    @Override
    public void loop() {
        getValuesFromInputPacket();
        getValuesFromPowertrainPacket();
        //shifting = calculateGearShift(rpm, speed, throttlePower, brakePower);
        createAndSendPacket();
    }

    private void getValuesFromInputPacket() {
        //InputPacket packet = virtualFunctionBus.inputPacket;
        //
    }

    private void getValuesFromPowertrainPacket() {
        //PowertrainPacket packet = virtualFunctionBus.powertrainPacket;
        // speed = packet.getSpeed();
        //rpm = packet.getRPM();
        //shift = Gearshift[packet.getGearshift()];
    }

    private void createAndSendPacket() {
        //PowertrainPacket packet = new PowertrainPacket();
        //packet.setRpm(0);
        //packet.setGearshift(0);
        //packet.setSpeed(0);
        //virtualFunctionBus.powertrainPacket = packet;
    }
}
