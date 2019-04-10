package hu.oe.nik.szfmv.automatedcar.model;

import hu.oe.nik.szfmv.automatedcar.model.objects.CrossWalk;
import hu.oe.nik.szfmv.automatedcar.model.objects.NpcPedestrian;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class World {

    private static final Logger LOGGER = LogManager.getLogger();

    public static final int WIDTH = 5120;
    public static final int HEIGHT = 3000;
    private static final String XML_LOCATION = "./src/main/resources/test_world.xml";

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
        worldObjects = createWorld(XML_LOCATION);
    }

    public List<WorldObject> createWorld(String xmlLocation) {
        try {
            return XmlConverter.build(xmlLocation);
        } catch (Exception e) {
            String msg = "Failed to create world: " + e.getMessage();
            LOGGER.error(msg, e);
            throw new RuntimeException(msg, e);
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
                Vector2D startPoint = crossWalk.getStartPoint();
                Vector2D endPoint = crossWalk.getEndPoint();
                NpcPedestrian pedestrian =
                    new NpcPedestrian(1000, 1000,
                        "man.png", "man.png", "man.png");
                ScriptedPath path = new ScriptedPath(pedestrian);
                List<Vector2D> waypoints = new ArrayList<>();
                waypoints.add(startPoint);
                waypoints.add(endPoint);
                path.setWaypoints(waypoints);
                path.setMovementSpeed(300);
                path.setLoopType(ScriptedPath.LoopType.PINGPONG);
                path.init();
                this.addObjectToWorld(pedestrian);
                npcPaths.add(path);
                pedestrian.setPath(path);
            }
        }
    }

    public List<ScriptedPath> getNpcPaths() {
        return npcPaths;
    }
}
