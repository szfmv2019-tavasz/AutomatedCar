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

    public InputManager(VirtualFunctionBus virtualFunctionBus) {
        super(virtualFunctionBus);
        inputPacket = new InputPacket();
        virtualFunctionBus.inputPacket = inputPacket;
    }

    @Override
    public void loop() {
    }

    @Override
    public void keyTyped(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_UP) {

        }
        if (keyCode == KeyEvent.VK_DOWN) {

        }
        if (keyCode == KeyEvent.VK_T) {

        }
        if (keyCode == KeyEvent.VK_L) {

        }

        // TODO ...

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_W) {

        }
        if (keyCode == KeyEvent.VK_S) {

        }
        if (keyCode == KeyEvent.VK_A) {

        }
        if (keyCode == KeyEvent.VK_D) {

        }

        // TODO ...

    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_W) {

        }
        if (keyCode == KeyEvent.VK_S) {

        }
        if (keyCode == KeyEvent.VK_A) {

        }
        if (keyCode == KeyEvent.VK_D) {

        }

        // TODO ...

    }
}
