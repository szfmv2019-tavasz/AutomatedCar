package hu.oe.nik.szfmv.automatedcar.visualization;

import hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.VirtualFunctionBus;
import hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.packets.ReadOnlyInputPacket;
import hu.oe.nik.szfmv.automatedcar.visualization.dashboard.TurnSignal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;

/**
 * Dashboard shows the state of the ego car, thus helps in debugging.
 */
public class Dashboard extends JPanel {

    private static final Logger LOGGER = LogManager.getLogger();

    private final int width = 250;
    private final int height = 700;
    private final int backgroundColor = 0x888888;

    private Gui parent;

    private TurnSignal leftTurnSignal;
    private TurnSignal rightTurnSignal;

    private Thread timer = new Thread() {
        int difference;    // ez nem tudom mire valo, egyelore maradjon

        public void run() {
            while (true) {
                try {
                    VirtualFunctionBus vfb = parent.getVirtualFunctionBus();
                    if (vfb != null) {
                        handleInputPacket(vfb.inputPacket);

                        //...

                    }
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    LOGGER.error("Error in Dashboard timer thread!", ex);
                }
            }
        }

        private void handleInputPacket(ReadOnlyInputPacket inputPacket) {
            if (inputPacket.isSignalLeftTurn()) {
                leftTurnSignal.setSwitchedOn(true);
            }
            if (inputPacket.isSignalRightTurn()) {
                leftTurnSignal.setSwitchedOn(false);
            }

            //...

        }

    };

    /**
     * Initialize the dashboard
     */
    public Dashboard(Gui pt) {
        // Not using any layout manager, but fixed coordinates
        setLayout(null);
        setBackground(new Color(backgroundColor));
        setBounds(770, 0, width, height);
        parent = pt;
        addComponents();
        timer.start();
    }

    private void addComponents() {
        addTurnSignals();

        //...

    }

    private void addTurnSignals() {
        leftTurnSignal = new TurnSignal(30, 150, true);
        rightTurnSignal = new TurnSignal(180, 150, false);
        add(leftTurnSignal);
        add(rightTurnSignal);
    }

}
