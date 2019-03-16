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
            this.pressedKeysList.add(keyCode);
        }
        for (Integer key : this.pressedKeysList) {
            this.keyDown(key);
        }
    }

    private void keyDown(Integer key) {

        if (key == KeyEvent.VK_W) {
            gasPedalRangeHandler.setIncrease(true);
        }
        if (key == KeyEvent.VK_S) {
            breakPedalRangeHandler.setIncrease(true);
            inputPacket.setAccSpeed(0);
        }
        if (key == KeyEvent.VK_A) {
            steeringRangeHandler.turnLeft();
        }
        if (key == KeyEvent.VK_D) {
            steeringRangeHandler.turnRight();
        }
        if (key == KeyEvent.VK_UP) {
            if (inputPacket.getGearShift().equals(ReadOnlyInputPacket.GEARSHIFTVALUES.P)) {
                inputPacket.setGearShift(ReadOnlyInputPacket.GEARSHIFTVALUES.R);
            } else if (inputPacket.getGearShift().equals(ReadOnlyInputPacket.GEARSHIFTVALUES.R)) { //Sebesség adatot bekérni
                inputPacket.setGearShift(ReadOnlyInputPacket.GEARSHIFTVALUES.N);
            } else if (inputPacket.getGearShift().equals(ReadOnlyInputPacket.GEARSHIFTVALUES.N)) {
                inputPacket.setGearShift(ReadOnlyInputPacket.GEARSHIFTVALUES.D);
            }
        }
        if (key == KeyEvent.VK_DOWN) {
            if (inputPacket.getGearShift().equals(ReadOnlyInputPacket.GEARSHIFTVALUES.D)) {
                inputPacket.setGearShift(ReadOnlyInputPacket.GEARSHIFTVALUES.N);
            } else if (inputPacket.getGearShift().equals(ReadOnlyInputPacket.GEARSHIFTVALUES.N)) {  //Sebesség adatot bekérni
                inputPacket.setGearShift(ReadOnlyInputPacket.GEARSHIFTVALUES.R);
            } else if (inputPacket.getGearShift().equals(ReadOnlyInputPacket.GEARSHIFTVALUES.R)) {
                inputPacket.setGearShift(ReadOnlyInputPacket.GEARSHIFTVALUES.P);
            }
        }
        if (key == KeyEvent.VK_T) {
            if (inputPacket.getAccDistance() == 1.4) {
                inputPacket.setAccDistance(0.8);
            } else {
                inputPacket.setAccDistance(inputPacket.getAccDistance() + 0.2);
            }
        }
        if (key == KeyEvent.VK_PLUS) {
            if (inputPacket.getAccSpeed() == 0) {
                inputPacket.setAccSpeed(120); //Teszt céljából
            } else if (inputPacket.getAccSpeed() != 0) {
                if (inputPacket.getAccSpeed() >= 30 && inputPacket.getAccSpeed() <= 150) {
                    inputPacket.setAccSpeed(inputPacket.getAccSpeed() + 10); // AccSpeedet -> aktuális sebességg legyen
                }
            }
        }
        if (key == KeyEvent.VK_MINUS) {
            if (inputPacket.getAccSpeed() == 0) {
                inputPacket.setAccSpeed(120);
            } else if (inputPacket.getAccSpeed()!= 0) {
                if (inputPacket.getAccSpeed() >= 40 && inputPacket.getAccSpeed() <= 160) {
                    inputPacket.setAccSpeed(inputPacket.getAccSpeed() - 10);
                }
            }
        }
        if (key == KeyEvent.VK_L) {
            if (!inputPacket.isLaneKeepingOn()) {
                inputPacket.setLaneKeepingOn(true);
            } else {
                inputPacket.setLaneKeepingOn(false);
            }
        }
        if (key == KeyEvent.VK_P) {
            if (!inputPacket.isParkingPilotOn()) {
                inputPacket.setParkingPilotOn(true);
            } else {
                inputPacket.setParkingPilotOn(false);
            }
        }
        if (key == KeyEvent.VK_Q) {
            if (!inputPacket.isSignalRightTurn()) {
                if (!inputPacket.isSignalLeftTurn()) {
                    inputPacket.setSignalLeftTurn(true);
                } else {
                    inputPacket.setSignalLeftTurn(false);
                }
            }
        }
        if (key == KeyEvent.VK_E) {
            if (!inputPacket.isSignalLeftTurn()) {
                if (!inputPacket.isSignalRightTurn()) {
                    inputPacket.setSignalRightTurn(true);
                } else {
                    inputPacket.setSignalRightTurn(false);
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        Integer keyCode = e.getKeyCode();
        this.pressedKeysList.remove(keyCode);
        this.keyUp(keyCode);
    }

    private void keyUp(Integer key)
    {
        if (key == KeyEvent.VK_W) {
            gasPedalRangeHandler.setIncrease(false);
        }
        if (key == KeyEvent.VK_S) {
            breakPedalRangeHandler.setIncrease(false);
        }
        if (key == KeyEvent.VK_A) {
            steeringRangeHandler.release();
        }
        if (key == KeyEvent.VK_D) {
            steeringRangeHandler.release();
        }
    }
}
