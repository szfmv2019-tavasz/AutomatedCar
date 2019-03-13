package hu.oe.nik.szfmv.automatedcar.systemcomponents;

import hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.VirtualFunctionBus;

public class Powertrain extends SystemComponent {

    private enum GearBox {
        P, R, N, D
    }
    private int rpm;
    private float throttle;
    private float brake;
    private int speed;

    public Powertrain(VirtualFunctionBus virtualFunctionBus) {
        super(virtualFunctionBus);
    }

    @Override
    public void loop() {

    }
}
