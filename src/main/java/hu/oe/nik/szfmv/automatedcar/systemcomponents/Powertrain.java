package hu.oe.nik.szfmv.automatedcar.systemcomponents;

import hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.VirtualFunctionBus;

public class Powertrain extends SystemComponent {

    private enum GearBox {
        P, R, N, D
    }

    private int rpm;
    private int speed;
    private double throttlePower;
    private double brakePower;

    public Powertrain(VirtualFunctionBus virtualFunctionBus) {
        super(virtualFunctionBus);

        this.rpm = 0;
        this.throttlePower = 0;
        this.brakePower = 0;
        this.speed = 0;
    }




    @Override
    public void loop() {

    }
}
