package hu.oe.nik.szfmv.automatedcar.systemcomponents;

import hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.VirtualFunctionBus;
import hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.packets.PowertrainPacket;
import hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.packets.ReadOnlyInputPacket;

public class Powertrain extends SystemComponent {

    private static final int maxSpeed = 10;
    private static final int minSpeed = -10;
    private static final double deltaTime = 0.04;
    private static final int accelConst = 10;
    private static final int slowConst = 20;

    private final PowertrainPacket powertrainPacket;

    private int rpm;
    private double speed = 0.0;

    public Powertrain(VirtualFunctionBus virtualFunctionBus) {
        super(virtualFunctionBus);

        this.powertrainPacket = new PowertrainPacket();
        virtualFunctionBus.powertrainPacket = powertrainPacket;

        this.rpm = 0;
    }

    @Override
    public void loop() {
        handleCarMovement();

        updatePowertrain();
    }

    private void handleCarMovement() {
        switch (virtualFunctionBus.inputPacket.getGearShift()) {
            case R:
                if ( virtualFunctionBus.inputPacket.getBreakPedal() > 0 && speed > minSpeed) {
                    speed -= virtualFunctionBus.inputPacket.getBreakPedal()/10.0 * slowConst * deltaTime;
                }

                releasedPedals();
                break;
            case P:
                releasedPedals();
                break;
            case N:
                releasedPedals();
                break;
            case D:

                if (virtualFunctionBus.inputPacket.getGasPedal() > 0 && speed < maxSpeed) {
                    speed += virtualFunctionBus.inputPacket.getGasPedal()/10.0 * accelConst * deltaTime;
                }

                // Ez tolatás, de még nincs váltónk
                if ( virtualFunctionBus.inputPacket.getBreakPedal() > 0 && speed > minSpeed) {
                    speed -= virtualFunctionBus.inputPacket.getBreakPedal()/10.0 * slowConst * deltaTime;
                }

                releasedPedals();
                break;
            default: break;
        }

        powertrainPacket.setSpeed((int)speed);
    }

    private void updatePowertrain() {
        virtualFunctionBus.powertrainPacket = powertrainPacket;
    }

    private void releasedPedals() {
        if (virtualFunctionBus.inputPacket.getGasPedal() == 0 && virtualFunctionBus.inputPacket.getBreakPedal() == 0 && speed > 0) {
            speed -= 25 * deltaTime;
            if (speed < 0) {
                speed = 0;
            }
        }

        if (virtualFunctionBus.inputPacket.getGasPedal() == 0 && virtualFunctionBus.inputPacket.getBreakPedal() == 0 && speed < 0) {
            speed += 25 * deltaTime;
            if (speed > 0) {
                speed = 0;
            }
        }
    }
}
