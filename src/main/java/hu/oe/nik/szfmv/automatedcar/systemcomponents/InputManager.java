package hu.oe.nik.szfmv.automatedcar.systemcomponents;

import hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.VirtualFunctionBus;
import hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.packets.InputPacket;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * System component class handling keyboard events.
 * The keyboard events are transformed to output values in the InputPacket class.
 */
public class InputManager extends SystemComponent implements KeyListener {

    private final InputPacket inputPacket;

    private final PedalRangeHandler gasPedalRangeHandler;

    private final PedalRangeHandler breakPedalRangeHandler;

    private final SteeringRangeHandler steeringRangeHandler;

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
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_W) {
            gasPedalRangeHandler.setIncrease(true);
        }
        if (keyCode == KeyEvent.VK_S) {
            breakPedalRangeHandler.setIncrease(true);
        }
        if (keyCode == KeyEvent.VK_A) {
            steeringRangeHandler.turnLeft();
        }
        if (keyCode == KeyEvent.VK_D) {
            steeringRangeHandler.turnRight();
        }


    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_W) {
            gasPedalRangeHandler.setIncrease(false);
        }
        if (keyCode == KeyEvent.VK_S) {
            breakPedalRangeHandler.setIncrease(false);
        }
        if (keyCode == KeyEvent.VK_A) {
            steeringRangeHandler.release();
        }
        if (keyCode == KeyEvent.VK_D) {
            steeringRangeHandler.release();
        }



    }
}
