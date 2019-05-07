package hu.oe.nik.szfmv.automatedcar.model;

import hu.oe.nik.szfmv.automatedcar.model.objects.CrossWalk;
import hu.oe.nik.szfmv.automatedcar.model.objects.NpcCar;
import hu.oe.nik.szfmv.automatedcar.model.objects.NpcPedestrian;
import hu.oe.nik.szfmv.automatedcar.model.objects.ParkingPlace;
import hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.packets.camera.SimpleDetector;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class World {

    private static final Logger LOGGER = LogManager.getLogger();

    public static final int WIDTH = 5120;
    public static final int HEIGHT = 3000;

    private static World instance;

    private List<WorldObject> worldObjects = new ArrayList<>();
    private List<ScriptedPath> npcPaths = new ArrayList<>();

    public static World getInstance() {
        // Thread safety is not needed in this singleton
        if (instance == null) {
            instance = new World();
        }
        return instance;
    }

    private World() {
        worldObjects = createWorld();
        SimpleDetector det = SimpleDetector.getDetector();
        det.setWorldObjects(this.worldObjects);
    }

    public List<WorldObject> createWorld() {

        try {
            File xml = new File(ClassLoader.getSystemResource("test_world.xml").getFile());
            return XmlParser.build(xml);
        } catch (Exception e) {
            System.out.printf(e.getMessage());
            return new ArrayList<WorldObject>();
        }
    }

    public List<WorldObject> getWorldObjects() {
        return worldObjects;
    }

    /**
     * Add an object to the virtual world.
     *
     * @param o {@link WorldObject} to be added to the virtual world
     */
    public void addObjectToWorld(WorldObject o) {
        worldObjects.add(o);
    }

    public void initializeNpcsAndPaths() {
        List<WorldObject> worldObjects = List.copyOf(this.worldObjects);
        for (WorldObject worldObject : worldObjects) {
            if (worldObject instanceof CrossWalk) {
                CrossWalk crossWalk = (CrossWalk) worldObject;
                Waypoint startPoint = crossWalk.getStartPoint();
                Waypoint endPoint = crossWalk.getEndPoint();
                NpcPedestrian pedestrian =
                    new NpcPedestrian(1000, 1000,
                        "man.png", "man.png", "man.png");
                ScriptedPath path = new ScriptedPath(pedestrian);
                List<Waypoint> waypoints = new ArrayList<>();
                waypoints.add(startPoint);
                waypoints.add(endPoint);
                path.setWaypoints(waypoints);
                path.setMovementSpeed(300);
                path.setLoopType(ScriptedPath.LoopType.PINGPONG);
                path.init();
                this.addObjectToWorld(pedestrian);
                npcPaths.add(path);
                pedestrian.setPath(path);
                path.start();
            }
        }
    }

    public void initializeParkingPlaces() {
        List<WorldObject> worldObjects = List.copyOf(this.worldObjects);
        for (WorldObject worldObject : worldObjects) {
            if (worldObject instanceof ParkingPlace) {
                ParkingPlace parkingPlace = (ParkingPlace) worldObject;
                parkingPlace.initializePaths(parkingPlace);
                placeCar(parkingPlace);
            }
        }
    }

    public void placeCar(ParkingPlace parkingPlace) {
        Random r = new Random();
        boolean upper = r.nextBoolean();
        Vector2D position;
        if (upper) {
            position = parkingPlace.getCenterUpper();
            parkingPlace.setUpperEmpty(false);
            parkingPlace.setLowerEmpty(true);
        } else {
            position = parkingPlace.getCenterLower();
            parkingPlace.setLowerEmpty(false);
            parkingPlace.setUpperEmpty(true);
        }
        NpcCar car = new NpcCar((int) position.getX(), (int) position.getY(), "car_1_blue.png", "car_1_blue.png", "car_1_blue.png");
        car.setRotation(parkingPlace.getRotation() + (float) Math.toRadians(-90));
        addObjectToWorld(car);
    }

    public List<ScriptedPath> getNpcPaths() {
        return npcPaths;
    }
}
