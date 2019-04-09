package hu.oe.nik.szfmv.automatedcar;

import hu.oe.nik.szfmv.automatedcar.model.ScriptedPath;
import hu.oe.nik.szfmv.automatedcar.model.World;
import hu.oe.nik.szfmv.automatedcar.model.WorldObject;
import hu.oe.nik.szfmv.automatedcar.model.objects.NpcPedestrian;
import hu.oe.nik.szfmv.automatedcar.systemcomponents.InputManager;
import hu.oe.nik.szfmv.automatedcar.visualization.Gui;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final int CYCLE_PERIOD = 40;
    // The window handle
    private Gui window;
    private AutomatedCar car;
    private World world;

    private NpcPedestrian pedestrian;
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
        // create an automated car and add to the world
        car = new AutomatedCar(200, 200, "car_2_white.png");
        world.addObjectToWorld(car);

        pedestrian = new NpcPedestrian(500, 500, "man.png",
            "man.png", "man.png");
        pedPath = new ScriptedPath(pedestrian);
        pedPath.setMovementSpeed(200);
        pedPath.setWaypoints(createPedWaypoint());
        pedPath.setLoopType(ScriptedPath.LoopType.PINGPONG);
        pedPath.init();
        world.addObjectToWorld(pedestrian);

        window = new Gui(car);
        window.setVirtualFunctionBus(car.getVirtualFunctionBus());
        window.addKeyListener(new InputManager(car.getVirtualFunctionBus()));
    }

    private void loop() {
        while (true) {
            try {
                car.drive();
                window.getCourseDisplay().drawWorld();
                pedPath.loop();
                Thread.sleep(CYCLE_PERIOD);
            } catch (InterruptedException e) {
                LOGGER.error(e.getMessage());
            }
        }
    }

    private List<Vector2D> createPedWaypoint() {
        List<Vector2D> waypoints = new ArrayList<Vector2D>();
        waypoints.add(new Vector2D(1000, 1000));
        waypoints.add(new Vector2D(1000, 2000));
        waypoints.add(new Vector2D(2000, 2000));
        waypoints.add(new Vector2D(2000, 1000));

        return waypoints;
    }

}
