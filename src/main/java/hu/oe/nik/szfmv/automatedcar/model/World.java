package hu.oe.nik.szfmv.automatedcar.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class World {

    private static final Logger LOGGER = LogManager.getLogger();

    public static final int WIDTH = 5120;
    public static final int HEIGHT = 3000;

    private static World instance;

    private List<WorldObject> worldObjects = new ArrayList<>();

    public static World getInstance() {
        // Thread safety is not needed in this singleton
        if (instance == null) {
            instance = new World();
        }
        return instance;
    }

    private World() {
        worldObjects = createWorld();
    }

    private List<WorldObject> createWorld() {
        try {
            File xml = new File(ClassLoader.getSystemResource("test_world.xml").getFile());
            return XmlParser.build(xml);
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
}
