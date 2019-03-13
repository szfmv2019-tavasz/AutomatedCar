package hu.oe.nik.szfmv.automatedcar.systemcomponents;

import hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.VirtualFunctionBus;
import hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.packets.PowertrainPacket;

public class Powertrain extends SystemComponent {

    private static final int maxSpeed = 100;
    private static final int minSpeed = -30;
    private static final double deltaTime = 0.04;
    private static final int accelConst = 10;
    private static final int slowConst = 20;

    private final PowertrainPacket powertrainPacket;

    private enum GearBox {
        R, P, N, D
    }

    private int rpm;
    private int speed;
    private double throttlePower;
    private double brakePower;
    private GearBox drive;

    public Powertrain(VirtualFunctionBus virtualFunctionBus) {
        super(virtualFunctionBus);

        this.powertrainPacket = new PowertrainPacket();
        virtualFunctionBus.powertrainPacket = powertrainPacket;

        this.rpm = 0;
        this.throttlePower = 0;
        this.brakePower = 0;
        this.speed = 0;
        this.drive = GearBox.D;
    }

    @Override
    public void loop() {
        System.out.println(speed);

        handleCarMovement();

        updatePowertrain();
    }

    private void handleCarMovement() {
        switch (drive) {
            case R:
                if (throttlePower > 0 && speed < maxSpeed) {
                    speed += throttlePower * accelConst * deltaTime;
                }

                if (throttlePower == 0 && speed > 0) {
                    speed += accelConst * deltaTime;
                }

                if (brakePower > 0 && speed < minSpeed) {
                    speed -= brakePower * slowConst * deltaTime;
                }
                break;
            case P:
                if (speed > 0 && brakePower == 0) {
                    speed -= brakePower * deltaTime;
                }

                if (speed > 0 && brakePower > 0) {
                    speed -= brakePower * slowConst * deltaTime;
                }
                break;
            case N:
                if (speed > 0 && brakePower == 0) {
                    speed -= brakePower * deltaTime;
                }

                if (speed > 0 && brakePower > 0) {
                    speed -= brakePower * slowConst * deltaTime;
                }
                break;
            case D:
                if (throttlePower > 0 && speed < maxSpeed) {
                    speed += throttlePower * accelConst * deltaTime;
                }

                if (throttlePower == 0 && speed > 0) {
                    speed -= accelConst * deltaTime;
                }

                if (brakePower > 0 && speed > 0) {
                    speed -= brakePower * slowConst * deltaTime;
                }
                break;
            default: break;
        }

        powertrainPacket.setSpeed(speed);
    }

    private void updatePowertrain() {
        virtualFunctionBus.powertrainPacket = powertrainPacket;
    }
}
