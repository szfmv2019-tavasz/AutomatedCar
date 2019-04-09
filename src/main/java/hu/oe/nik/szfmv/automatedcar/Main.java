package hu.oe.nik.szfmv.automatedcar;

import hu.oe.nik.szfmv.automatedcar.model.ScriptedPath;
import hu.oe.nik.szfmv.automatedcar.model.World;
import hu.oe.nik.szfmv.automatedcar.model.WorldObject;
import hu.oe.nik.szfmv.automatedcar.model.objects.NpcCar;
import hu.oe.nik.szfmv.automatedcar.systemcomponents.InputManager;
import hu.oe.nik.szfmv.automatedcar.visualization.Gui;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

public class Main {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final int CYCLE_PERIOD = 40;
    private static final int CAR_START_POS_X = 200;
    private static final int CAR_START_POS_Y = 200;
    // The window handle
    private Gui window;
    private AutomatedCar car;
    private World world;
    private List<ScriptedPath> npcPaths;

    private WorldObject pedestrian;
    private ScriptedPath pedPath;

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {

        new Main().run();

    }

    public void run() throws IOException, SAXException, ParserConfigurationException {
        init();
        loop();

    }

    private void init() {
        // create the world
        World world = World.getInstance();
        this.world = world;
        // create an automated car and add to the world
        car = new AutomatedCar(CAR_START_POS_X, CAR_START_POS_Y, "car_2_white.png");
        world.addObjectToWorld(car);
        world.initializeNpcsAndPaths();
        npcPaths = world.getNpcPaths();
        NpcCar npcCar = new NpcCar(0, 0, "car_1_blue.png", "car_1_blue.png", "car_1_blue.png");
        npcPaths.add(npcCar.getPath());
        world.addObjectToWorld(npcCar);
        window = new Gui(car);
        window.setVirtualFunctionBus(car.getVirtualFunctionBus());
        window.addKeyListener(new InputManager(car.getVirtualFunctionBus()));
    }

    private void loop() {
        while (true) {
            try {
                car.drive();
                window.getCourseDisplay().drawWorld();
                loopNpcPaths();
                Thread.sleep(CYCLE_PERIOD);
            } catch (InterruptedException e) {
                LOGGER.error(e.getMessage());
            }
        }
    }

    private void loopNpcPaths() {
        for (ScriptedPath path : npcPaths) {
            path.loop();
        }
    }


}
