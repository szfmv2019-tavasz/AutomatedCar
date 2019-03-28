package hu.oe.nik.szfmv.automatedcar;

import hu.oe.nik.szfmv.automatedcar.model.World;
import hu.oe.nik.szfmv.automatedcar.systemcomponents.InputManager;
import hu.oe.nik.szfmv.automatedcar.visualization.Gui;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class Main {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final int CYCLE_PERIOD = 40;
    // The window handle
    private Gui window;
    private AutomatedCar car;
    private World world;

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {

        new Main().run();

    }

    public void run() throws IOException, SAXException, ParserConfigurationException {
        init();
        loop();

    }

    private void init() {
        // create the world
        world = new World(5000, 3000);
        // create an automated car
        car = new AutomatedCar(20, 20, "car_2_white.png");
        world.addObjectToWorld(car);

        window = new Gui();
        window.setVirtualFunctionBus(car.getVirtualFunctionBus());
        window.addKeyListener(new InputManager(car.getVirtualFunctionBus()));
    }

    private void loop() {
        while (true) {
            try {
                car.drive();

                window.getCourseDisplay().drawWorld(world);
                //window.getCourseDisplay().refreshFrame();
                Thread.sleep(CYCLE_PERIOD);
            } catch (InterruptedException e) {
                LOGGER.error(e.getMessage());
            }
        }
    }

}
