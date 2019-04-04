package hu.oe.nik.szfmv.automatedcar.visualization;

import hu.oe.nik.szfmv.automatedcar.AutomatedCar;
import hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.VirtualFunctionBus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;

public class Gui extends JFrame {

    private static final Logger LOGGER = LogManager.getLogger();

    private final int windowWidth = 1020;
    private final int windowHeight = 700;

    private CourseDisplay courseDisplay;
    private Dashboard dashboard;
    private VirtualFunctionBus virtualFunctionBus;
    private AutomatedCar car;

    /**
     * Initialize the GUI class
     */
    public Gui(AutomatedCar car) {
        setTitle("AutomatedCar");
        setLocation(0, 0); // default is 0,0 (top left corner)
        addWindowListener(new GuiAdapter());
        setPreferredSize(new Dimension(windowWidth, windowHeight)); // inner size
        setResizable(false);
        pack();

        // Icon downloaded from:
        // http://www.iconarchive.com/show/toolbar-2-icons-by-shlyapnikova/car-icon.html
        // and available under the licence of:
        // https://creativecommons.org/licenses/by/4.0/
        ImageIcon carIcon = new ImageIcon(ClassLoader.getSystemResource("car-icon.png"));
        setIconImage(carIcon.getImage());
        this.car = car;
        // Not using any layout manager, but fixed coordinates
        setLayout(null);

        courseDisplay = new CourseDisplay(car);
        add(courseDisplay);

        dashboard = new Dashboard(this);
        add(dashboard);

        setVisible(true);
    }

    public VirtualFunctionBus getVirtualFunctionBus() {
        return virtualFunctionBus;
    }

    public void setVirtualFunctionBus(VirtualFunctionBus virtualFunctionBus) {
        this.virtualFunctionBus = virtualFunctionBus;
    }

    public CourseDisplay getCourseDisplay() {
        return courseDisplay;
    }

    public AutomatedCar getAutomatedCar(){
        return  this.car;
    }
}
