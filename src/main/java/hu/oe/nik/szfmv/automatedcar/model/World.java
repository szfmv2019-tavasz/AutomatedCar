package hu.oe.nik.szfmv.automatedcar.model;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class World {
    private int width = 0;
    private int height = 0;
    private List<WorldObject> worldObjects = new ArrayList<>();

    public World(int width, int height) {
        this.width = width;
        this.height = height;
        worldObjects = this.createWorld();
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public List<WorldObject> createWorld() {
        try {
            File xml = new File(ClassLoader.getSystemResource("test_world.xml").getFile());
            return XmlParser.build(xml);
        } catch (Exception ex) {
            System.out.printf(ex.getMessage());
            return  new ArrayList<WorldObject>();

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
