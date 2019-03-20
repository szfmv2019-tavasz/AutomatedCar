package hu.oe.nik.szfmv.automatedcar.systemcomponents;

import hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.VirtualFunctionBus;
import hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.packets.PowertrainPacket;

public class Powertrain extends SystemComponent {

    private final float deltaTime = 0.04f;

    private final float maxSpeed = 200f;
    private final float minSpeed = -100f;

    private final float accelConst = 20f;
    private final float reverseAccelConst = 25f;
    private final float slowConst = 20f;

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
        float pedalRate = 10.0f;
        switch (virtualFunctionBus.inputPacket.getGearShift()) {
            case R:
                if (virtualFunctionBus.inputPacket.getBreakPedal() > 0 && speed > minSpeed) {
                    speed -= virtualFunctionBus.inputPacket.getBreakPedal() / pedalRate * reverseAccelConst * deltaTime;
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
                    speed += virtualFunctionBus.inputPacket.getGasPedal() / pedalRate * accelConst * deltaTime;
                }

                // Ez tolatás, de még nincs váltónk
                if (virtualFunctionBus.inputPacket.getBreakPedal() > 0 && speed > minSpeed) {
                    speed -= virtualFunctionBus.inputPacket.getBreakPedal() / pedalRate * slowConst * deltaTime;
                }

                releasedPedals();
                break;
            default:
                break;
        }
    }

    private void releasedPedals() {
        if (virtualFunctionBus.inputPacket.getGasPedal() == 0
                && virtualFunctionBus.inputPacket.getBreakPedal() == 0 && speed > 0) {
            speed -= slowConst * deltaTime;
            if (speed < 0) {
                speed = 0;
            }
        }

        if (virtualFunctionBus.inputPacket.getGasPedal() == 0
                && virtualFunctionBus.inputPacket.getBreakPedal() == 0 && speed < 0) {
            speed += slowConst * deltaTime;
            if (speed > 0) {
                speed = 0;
            }
        }
    }
}
