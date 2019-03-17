package hu.oe.nik.szfmv.automatedcar.visualization;

import hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.VirtualFunctionBus;
import hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.packets.ReadOnlyInputPacket;
import hu.oe.nik.szfmv.automatedcar.visualization.dashboard.TextSignal;
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

    private TextSignal accSpeedSignal;
    private TextSignal accDistanceSignal;
    private TextSignal accOnOffSignal;
    private TextSignal ppSignal;
    private TextSignal lkaSignal;
    private TextSignal lkaWarningSignal;
    private TextSignal trafficSignSignal;
    private TextSignal aebWarningSignal;
    private TextSignal rrWarningSignal;

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

                    setTestValues();    // csak teszteleshez

                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    LOGGER.error("Error in Dashboard timer thread!", ex);
                }
            }
        }

        // Csak tesztelesi celokra:
        private void setTestValues() {
            //leftTurnSignal.setSwitchedOn(true);
            //accSpeedSignal.setText("130");

        }

        private void handleInputPacket(ReadOnlyInputPacket inputPacket) {
            leftTurnSignal.setSwitchedOn(inputPacket.isSignalLeftTurn());
            rightTurnSignal.setSwitchedOn(inputPacket.isSignalRightTurn());
            accSpeedSignal.setText(String.valueOf(inputPacket.getAccSpeed()));
            accDistanceSignal.setText(String.valueOf(inputPacket.getAccDistance()));
            accOnOffSignal.setSwitchedOn(false);
            ppSignal.setSwitchedOn(false);
            lkaSignal.setSwitchedOn(false);
            lkaSignal.setSwitchedOn(false);

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
        addTextSignals();

        //...

    }

    private void addTurnSignals() {
        leftTurnSignal = new TurnSignal(30, 150, true);
        rightTurnSignal = new TurnSignal(180, 150, false);
        add(leftTurnSignal);
        add(rightTurnSignal);
    }

    private void addTextSignals() {
        accSpeedSignal = new TextSignal(20, 200, 50, 25, "0", 14);
        accDistanceSignal = new TextSignal(70,200,50,25,"0.8",14);
        accOnOffSignal = new TextSignal(20,235,50,25,"ACC",14);
        ppSignal = new TextSignal(70,235,50,25,"PP",14);
        lkaSignal = new TextSignal(20,270,50,25,"LKA",14);
        lkaWarningSignal = new TextSignal(20,295,100,25,"LKA WARN",14);
        trafficSignSignal = new TextSignal(130,200,100,100,"STOP",14);
        aebWarningSignal = new TextSignal(130,295,100,25,"AEB WARN",14);
        rrWarningSignal = new TextSignal(130,320,100,25,"RR WARN",14);

        // ...

        add(accSpeedSignal);
        add(accDistanceSignal);
        add(accOnOffSignal);
        add(ppSignal);
        add(lkaSignal);
        add(lkaWarningSignal);
        add(trafficSignSignal);
        add(aebWarningSignal);
        add(rrWarningSignal);

        // ...

    }
}
