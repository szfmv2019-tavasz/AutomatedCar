package hu.oe.nik.szfmv.automatedcar.systemcomponents;

import hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.VirtualFunctionBus;
import hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.packets.PowertrainPacket;
import hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.packets.ReadOnlyInputPacket;

public class Powertrain extends SystemComponent {

    private static final float deltaTime = 0.04f;

    private static final float maxSpeed = 10f;
    private static final float minSpeed = -10f;

    private static final float accelConst = 10f;
    private static final float slowConst = 20f;

    private int rpm = 0;
    private float speed = 0f;   // in m/s

    public Powertrain(VirtualFunctionBus virtualFunctionBus) {
        super(virtualFunctionBus);
    }

    @Override
    public void loop() {
        handleCarMovement();

        createAndSendPacket();
    }

    private void createAndSendPacket() {
        PowertrainPacket packet = new PowertrainPacket();
        packet.setSpeed(speed);
        packet.setRpm(rpm);
        virtualFunctionBus.powertrainPacket = packet;
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
