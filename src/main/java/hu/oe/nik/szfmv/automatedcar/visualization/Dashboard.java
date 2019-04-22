package hu.oe.nik.szfmv.automatedcar.visualization;

//import hu.oe.nik.szfmv.automatedcar.model.WorldObject;
import hu.oe.nik.szfmv.automatedcar.AutomatedCar;
import hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.VirtualFunctionBus;
import hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.packets.ReadOnlyInputPacket;
import hu.oe.nik.szfmv.automatedcar.visualization.dashboard.DashboardText;
import hu.oe.nik.szfmv.automatedcar.visualization.dashboard.ProgressBars;
import hu.oe.nik.szfmv.automatedcar.visualization.dashboard.TextSignal;
import hu.oe.nik.szfmv.automatedcar.visualization.dashboard.Gauge;
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

    private static final int WIDTH = 250;
    private static final int HEIGHT = 700;
    private static final int BACKGROUND_COLOR = 0x888888;

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
    private DashboardText healthSignal;
    private DashboardText currentGear;
    private DashboardText speedLimit;
    private DashboardText steeringWheel;
    private DashboardText xCoordinate;
    private DashboardText yCoordinate;
    private JLabel emergencyWarning;
    private ProgressBars gasProgressBar;
    private ProgressBars breakProgressBar;
    private Gauge rpmGauge;
    private Gauge kmhGauge;

    private Thread timer = new Thread() {

        public void run() {
            while (true) {
                try {
                    VirtualFunctionBus vfb = parent.getVirtualFunctionBus();

                    if (vfb != null) {
                        handleInputPacket(vfb.inputPacket);

                        // Get from VFB (when available):
                        handleOtherPacketDummy();
                    }

                    Thread.sleep(40);
                } catch (InterruptedException ex) {
                    LOGGER.error("Error in Dashboard timer thread!", ex);
                }
            }
        }

        private void handleInputPacket(ReadOnlyInputPacket inputPacket) {
            currentGear.setText(String.valueOf(inputPacket.getGearShift()));
            leftTurnSignal.setSwitchedOn(inputPacket.isSignalLeftTurn());
            rightTurnSignal.setSwitchedOn(inputPacket.isSignalRightTurn());
            accSpeedSignal.setText(String.valueOf(inputPacket.getAccSpeed()));
            ppSignal.setSwitchedOn(inputPacket.isParkingPilotOn());
            lkaSignal.setSwitchedOn(inputPacket.isLaneKeepingOn());
            gasProgressBar.setValue(inputPacket.getGasPedal());
            breakProgressBar.setValue(inputPacket.getBreakPedal());
            steeringWheel.setText(String.valueOf(inputPacket.getSteeringWheel()));
            healthSignal.setText(String.valueOf(parent.getAutomatedCar().getAutomatedCarHealth()));
        }

        private void handleOtherPacketDummy() {
            rpmGauge.setValue(parent.getVirtualFunctionBus().powertrainPacket.getRPM());
            kmhGauge.setValue(parent.getVirtualFunctionBus().powertrainPacket.getSpeed());
            lkaWarningSignal.setSwitchedOn(false);
            speedLimit.setText("100");
            xCoordinate.setText(String.valueOf(parent.getAutomatedCar().getX()));
            yCoordinate.setText(String.valueOf(parent.getAutomatedCar().getY()));
            emergencyWarning.setVisible(parent.getVirtualFunctionBus().brakePacket.isWarning());
            accOnOffSignal.setSwitchedOn(parent.getVirtualFunctionBus().tempomatPacket.isActive());
            accDistanceSignal.setText(String.valueOf(parent.getVirtualFunctionBus().tempomatPacket.getDistance()));
        }
    };

    /**
     * Initialize the dashboard
     */
    public Dashboard(Gui pt) {
        // Not using any layout manager, but fixed coordinates
        setLayout(null);
        setBackground(new Color(BACKGROUND_COLOR));
        setBounds(770, 0, WIDTH, HEIGHT);
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
        addEmergencyWarning();
    }

    private void addTurnSignals() {
        leftTurnSignal = new TurnSignal(30, 150, true);
        rightTurnSignal = new TurnSignal(180, 150, false);
        add(leftTurnSignal);
        add(rightTurnSignal);
    }

    private void addTextSignals() {
        accSpeedSignal = new TextSignal(20, 200, 50, 25, " ", 14);
        accDistanceSignal = new TextSignal(70, 200, 50, 25, " ", 14);
        accOnOffSignal = new TextSignal(20, 235, 50, 25, "ACC", 14);
        ppSignal = new TextSignal(70, 235, 50, 25, "PP", 14);
        lkaSignal = new TextSignal(20, 270, 50, 25, "LKA", 14);
        lkaWarningSignal = new TextSignal(20, 295, 100, 25, "LKA WARN", 14);
        trafficSignSignal = new TextSignal(130, 200, 100, 100, "STOP", 14);
        aebWarningSignal = new TextSignal(130, 295, 100, 25, "AEB WARN", 14);
        rrWarningSignal = new TextSignal(130, 320, 100, 25, "RR WARN", 14);
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
        DashboardText accOpts = new DashboardText(20, 180, 70, 15, "ACC Opts");
        DashboardText gearText = new DashboardText(80, 155, 35, 15, "Gear: ");
        DashboardText gasText = new DashboardText(20, 350, 80, 15, "Gas Pedal:");
        DashboardText breakText = new DashboardText(20, 385, 80, 15, "Break Pedal:");
        DashboardText speedLimitText = new DashboardText(20, 440, 80, 15, "Speed Limit:");
        DashboardText debugText = new DashboardText(20, 475, 45, 15, "Debug: ");
        DashboardText steeringWheelText = new DashboardText(20, 490, 95, 15, "Steering Wheel: ");
        DashboardText xText = new DashboardText(20, 515, 20, 15, "X: ");
        DashboardText yText = new DashboardText(150, 515, 20, 15, "Y: ");
        DashboardText helathText = new DashboardText(20, 535, 70, 15, "Health:");
        add(accOpts);
        add(gearText);
        add(gasText);
        add(breakText);
        add(speedLimitText);
        add(debugText);
        add(steeringWheelText);
        add(xText);
        add(yText);
        add(helathText);
    }

    private void addDashboardActiveTexts() {
        currentGear = new DashboardText(130, 155, 10, 15, " ");
        speedLimit = new DashboardText(110, 440, 30, 15, "0");
        steeringWheel = new DashboardText(120, 490, 30, 15, " ");
        xCoordinate = new DashboardText(50, 515, 100, 15, "0");
        yCoordinate = new DashboardText(180, 515, 100, 15, "0");
        healthSignal = new DashboardText(100, 535, 25, 15, " ");
        add(currentGear);
        add(speedLimit);
        add(steeringWheel);
        add(xCoordinate);
        add(yCoordinate);
        add(healthSignal);
    }

    private void addProgressBars() {
        gasProgressBar = new ProgressBars(20, 365, 200, 20);
        breakProgressBar = new ProgressBars(20, 400, 200, 20);
        add(gasProgressBar);
        add(breakProgressBar);
    }

    private void addGauges() {
        rpmGauge = new Gauge(0, 15, 117, 118, "rpm", 0, 8000, 1000);
        kmhGauge = new Gauge(115, 15, 117, 118, "km / h", 0, 200, 20);
        add(rpmGauge);
        add(kmhGauge);
    }

    private void addEmergencyWarning() {
        emergencyWarning = new JLabel();
        emergencyWarning.setText("Emergency");
        emergencyWarning.setFont(new Font("Onyx", Font.BOLD, 20));
        emergencyWarning.setForeground(Color.red);
        emergencyWarning.setBounds(80, 585, 120, 30);
        add(emergencyWarning);
    }

}
