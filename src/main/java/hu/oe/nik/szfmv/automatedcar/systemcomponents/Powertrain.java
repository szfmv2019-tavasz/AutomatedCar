package hu.oe.nik.szfmv.automatedcar.systemcomponents;

import hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.VirtualFunctionBus;
import hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.packets.PowertrainPacket;

public class Powertrain extends SystemComponent {

    private final PowertrainPacket powertrainPacket;

    private enum GearBox {
        P, R, N, D
    }

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

    }
}
