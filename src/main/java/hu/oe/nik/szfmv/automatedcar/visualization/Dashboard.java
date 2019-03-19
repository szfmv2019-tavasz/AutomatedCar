package hu.oe.nik.szfmv.automatedcar.visualization;

import hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.VirtualFunctionBus;
import hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.packets.InputPacket;
import hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.packets.ReadOnlyInputPacket;
import hu.oe.nik.szfmv.automatedcar.visualization.dashboard.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;

/**
 * Dashboard shows the state of the ego car, thus helps in debugging.
 */
public class Dashboard extends JPanel {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final int width = 250;
    private static final int height = 700;
    private static final int backgroundColor = 0x888888;

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
    private DashboardText currentGear;
    private DashboardText speedLimit;
    private DashboardText steeringWheel;
    private DashboardText xCoordinate;
    private DashboardText yCoordinate;
    private ProgressBars gasProgressBar;
    private ProgressBars breakProgressBar;
    private Gauge rpmGauge;
    private Gauge kmhGauge;


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

                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    LOGGER.error("Error in Dashboard timer thread!", ex);
                }
            }
        }

        // Csak tesztelesi celokra:
        private void setTestValues() {
            //leftTurnSignal.setSwitchedOn(true);
            //accSpeedSignal.setText("130");

            //rpmGauge.setValue(3000);
            //kmhGauge.setValue(130);

        }

        private void handleInputPacket(ReadOnlyInputPacket inputPacket) {
            leftTurnSignal.setSwitchedOn(inputPacket.isSignalLeftTurn());
            rightTurnSignal.setSwitchedOn(inputPacket.isSignalRightTurn());
            accSpeedSignal.setText(String.valueOf(inputPacket.getAccSpeed()));
            accDistanceSignal.setText(String.valueOf(inputPacket.getAccDistance()));
            accOnOffSignal.setSwitchedOn(false);
            ppSignal.setSwitchedOn(false);
            lkaSignal.setSwitchedOn(false);
            lkaWarningSignal.setSwitchedOn(false);
            currentGear.setText(String.valueOf(inputPacket.getGearShift()));
            //speedLimit.setText(); Read from the bus
            steeringWheel.setText(String.valueOf(inputPacket.getSteeringWheel()));
            //xCoordinate.setText(); Read car X coordinate
            //yCoordinate.setText(); Read car y coordinate
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
        addDashboardPlainTexts();
        addDashboardActiveTexts();
        addProgressBars();
        addGauges();
    }

    private void addTurnSignals() {
        leftTurnSignal = new TurnSignal(30, 150, true);
        rightTurnSignal = new TurnSignal(180, 150, false);
        add(leftTurnSignal);
        add(rightTurnSignal);
    }

    private void addTextSignals() {
        accSpeedSignal = new TextSignal(20, 200, 50, 25, " ", 14);
        accDistanceSignal = new TextSignal(70,200,50,25," ",14);
        accOnOffSignal = new TextSignal(20,235,50,25,"ACC",14);
        ppSignal = new TextSignal(70,235,50,25,"PP",14);
        lkaSignal = new TextSignal(20,270,50,25,"LKA",14);
        lkaWarningSignal = new TextSignal(20,295,100,25,"LKA WARN",14);
        trafficSignSignal = new TextSignal(130,200,100,100,"STOP",14);
        aebWarningSignal = new TextSignal(130,295,100,25,"AEB WARN",14);
        rrWarningSignal = new TextSignal(130,320,100,25,"RR WARN",14);
        add(accSpeedSignal);
        add(accDistanceSignal);
        add(accOnOffSignal);
        add(ppSignal);
        add(lkaSignal);
        add(lkaWarningSignal);
        add(trafficSignSignal);
        add(aebWarningSignal);
        add(rrWarningSignal);
    }

    private void addDashboardPlainTexts() {
        DashboardText accOpts = new DashboardText(20,180,70,15,"ACC Opts");
        DashboardText gearText = new DashboardText(90,155,35,15, "Gear: ");
        DashboardText gasText = new DashboardText(20,350,80,15, "Gas Pedal:");
        DashboardText breakText = new DashboardText(20,385,80,15,"Break Pedal:");
        DashboardText speedLimitText = new DashboardText(20,440,70,15,"Speed Limit:");
        DashboardText debugText = new DashboardText(20,475,45,15,"Debug: ");
        DashboardText steeringWheelText = new DashboardText(20,490,95,15,"Steering Wheel: ");
        DashboardText xText = new DashboardText(20,515,20,15, "X: ");
        DashboardText yText = new DashboardText(100,515,20,15, "Y: ");
        add(accOpts);
        add(gearText);
        add(gasText);
        add(breakText);
        add(speedLimitText);
        add(debugText);
        add(steeringWheelText);
        add(xText);
        add(yText);
    }

    private void addDashboardActiveTexts() {
        currentGear = new DashboardText(125,155,10,15, " ");
        speedLimit = new DashboardText(100, 440,50,15,"0");
        steeringWheel = new DashboardText(120,490,20,15," ");
        xCoordinate = new DashboardText(50,515,20,15,"0");
        yCoordinate = new DashboardText(130,515,20,15,"0");
        add(currentGear);
        add(speedLimit);
        add(steeringWheel);
        add(xCoordinate);
        add(yCoordinate);
    }

    private void addProgressBars() {
        gasProgressBar = new ProgressBars(20,365,200,20);
        breakProgressBar = new ProgressBars(20,400,200,20);
        add(gasProgressBar);
        add(breakProgressBar);
    }

    private void addGauges() {
        rpmGauge = new Gauge(0, 15, 117, 118, "rpm", 0, 8000, 1000);
        kmhGauge = new Gauge(115, 15, 117, 118, "km / h", 0, 200, 20);
        add(rpmGauge);
        add(kmhGauge);
    }
}
