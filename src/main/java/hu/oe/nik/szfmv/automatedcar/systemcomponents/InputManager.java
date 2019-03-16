package hu.oe.nik.szfmv.automatedcar.systemcomponents;

import hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.VirtualFunctionBus;
import hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.packets.InputPacket;
import hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.packets.ReadOnlyInputPacket;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

/**
 * System component class handling keyboard events.
 * The keyboard events are transformed to output values in the InputPacket class.
 */
public class InputManager extends SystemComponent implements KeyListener {

    private static final double ACC_DISTANCE_MIN = 0.8;
    private static final double ACC_DISTANCE_MAX = 1.4;
    private static final double ACC_DISTANCE_STEP = 0.2;

    private static final int ACC_SPEED_MIN = 30;
    private static final int ACC_SPEED_MAX = 160;
    private static final int ACC_SPEED_STEP = 10;

    //TODO Remove ACC_SPEED_INIT constant when init speed is from VFB
    private static final int ACC_SPEED_INIT = 120;

    private final InputPacket inputPacket;

    private final PedalRangeHandler gasPedalRangeHandler;

    private final PedalRangeHandler breakPedalRangeHandler;

    private final SteeringRangeHandler steeringRangeHandler;

    private ArrayList<Integer> pressedKeysList = new ArrayList<>();

    public InputManager(VirtualFunctionBus virtualFunctionBus) {
        super(virtualFunctionBus);
        inputPacket = new InputPacket();
        virtualFunctionBus.inputPacket = inputPacket;
        gasPedalRangeHandler = new PedalRangeHandler(0, 100);
        breakPedalRangeHandler = new PedalRangeHandler(0, 100);
        steeringRangeHandler = new SteeringRangeHandler(100);
    }

    @Override
    public void loop() {
        gasPedalRangeHandler.loop();
        breakPedalRangeHandler.loop();
        steeringRangeHandler.loop();

        inputPacket.setGasPedal(gasPedalRangeHandler.getValue());
        inputPacket.setBreakPedal(breakPedalRangeHandler.getValue());
        inputPacket.setSteeringWheel(steeringRangeHandler.getValue());
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        Integer keyCode = e.getKeyCode();

        if (!pressedKeysList.contains(keyCode)) {
            pressedKeysList.add(keyCode);
        }
        for (Integer key : pressedKeysList) {
            handleKeyPressed(key);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        Integer keyCode = e.getKeyCode();
        pressedKeysList.remove(keyCode);
        handleKeyWSADReleased(keyCode);
    }

    private void handleKeyPressed(Integer key) {
        switch (key) {
            case KeyEvent.VK_UP: handleKeyUp();
            case KeyEvent.VK_DOWN: handleKeyDown();
            case KeyEvent.VK_T: handleKeyT();
            case KeyEvent.VK_PLUS: handleKeyPlus();
            case KeyEvent.VK_MINUS: handleKeyMinus();
            case KeyEvent.VK_L: handleKeyL();
            case KeyEvent.VK_P: handleKeyP();
            case KeyEvent.VK_Q: handleKeyQ();
            case KeyEvent.VK_E: handleKeyE();
            default:
                handleKeyWSADPressed(key);
        }
    }

    private void handleKeyWSADPressed(Integer key) {
        switch (key) {
            case KeyEvent.VK_W:
                gasPedalRangeHandler.setIncrease(true);
            case KeyEvent.VK_S:
                breakPedalRangeHandler.setIncrease(true);
                inputPacket.setAccSpeed(0);
            case KeyEvent.VK_A:
                steeringRangeHandler.turnLeft();
            case KeyEvent.VK_D:
                steeringRangeHandler.turnRight();
        }
    }

    private void handleKeyWSADReleased(Integer key) {
        switch (key) {
            case KeyEvent.VK_W:
                gasPedalRangeHandler.setIncrease(false);
            case KeyEvent.VK_S:
                breakPedalRangeHandler.setIncrease(false);
            case KeyEvent.VK_A:
                steeringRangeHandler.release();
            case KeyEvent.VK_D:
                steeringRangeHandler.release();
        }
    }

    private void handleKeyUp() {
        if (inputPacket.getGearShift() == ReadOnlyInputPacket.GearShiftValues.P) {
            inputPacket.setGearShift(ReadOnlyInputPacket.GearShiftValues.R);
        } else if (inputPacket.getGearShift() == ReadOnlyInputPacket.GearShiftValues.R) {
            inputPacket.setGearShift(ReadOnlyInputPacket.GearShiftValues.N);
        } else if (inputPacket.getGearShift() == ReadOnlyInputPacket.GearShiftValues.N) {
            inputPacket.setGearShift(ReadOnlyInputPacket.GearShiftValues.D);
        }
    }

    private void handleKeyDown() {
        if (inputPacket.getGearShift() == ReadOnlyInputPacket.GearShiftValues.D) {
            inputPacket.setGearShift(ReadOnlyInputPacket.GearShiftValues.N);
        } else if (inputPacket.getGearShift() == ReadOnlyInputPacket.GearShiftValues.N) {
            inputPacket.setGearShift(ReadOnlyInputPacket.GearShiftValues.R);
        } else if (inputPacket.getGearShift() == ReadOnlyInputPacket.GearShiftValues.R) {
            inputPacket.setGearShift(ReadOnlyInputPacket.GearShiftValues.P);
        }
    }

    private void handleKeyT() {
        if (inputPacket.getAccDistance() == ACC_DISTANCE_MAX) {
            inputPacket.setAccDistance(ACC_DISTANCE_MIN);
        } else {
            inputPacket.setAccDistance(inputPacket.getAccDistance() + ACC_DISTANCE_STEP);
        }
    }

    private void handleKeyPlus() {
        if (inputPacket.getAccSpeed() == 0) {
            //TODO Set the currend speed from VFB:
            inputPacket.setAccSpeed(ACC_SPEED_INIT);
        } else if (inputPacket.getAccSpeed() != 0) {
            inputPacket.setAccSpeed(inputPacket.getAccSpeed() + ACC_SPEED_STEP);
            if (inputPacket.getAccSpeed() > ACC_SPEED_MAX) {
                inputPacket.setAccSpeed(ACC_SPEED_MAX);
            }
        }
    }

    private void handleKeyMinus() {
        if (inputPacket.getAccSpeed() == 0) {
            //TODO Set the currend speed from VFB:
            inputPacket.setAccSpeed(120);
        } else if (inputPacket.getAccSpeed() != 0) {
            inputPacket.setAccSpeed(inputPacket.getAccSpeed() - ACC_SPEED_STEP);
            if (inputPacket.getAccSpeed() < ACC_SPEED_MIN) {
                inputPacket.setAccSpeed(ACC_SPEED_MIN);
            }
        }
    }

    private void handleKeyL() {
        if (!inputPacket.isLaneKeepingOn()) {
            inputPacket.setLaneKeepingOn(true);
        } else {
            inputPacket.setLaneKeepingOn(false);
        }
    }

    private void handleKeyP() {
        if (!inputPacket.isParkingPilotOn()) {
            inputPacket.setParkingPilotOn(true);
        } else {
            inputPacket.setParkingPilotOn(false);
        }
    }

    private void handleKeyQ() {
        if (!inputPacket.isSignalRightTurn()) {
            if (!inputPacket.isSignalLeftTurn()) {
                inputPacket.setSignalLeftTurn(true);
            } else {
                inputPacket.setSignalLeftTurn(false);
            }
        }
    }

    private void handleKeyE() {
        if (!inputPacket.isSignalLeftTurn()) {
            if (!inputPacket.isSignalRightTurn()) {
                inputPacket.setSignalRightTurn(true);
            } else {
                inputPacket.setSignalRightTurn(false);
            }
        }
    }
}
