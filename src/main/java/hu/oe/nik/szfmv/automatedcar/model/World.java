package hu.oe.nik.szfmv.automatedcar.model;

import java.util.ArrayList;
import java.util.List;

public class World {
    private int width = 0;
    private int height = 0;
    private List<WorldObject> worldObjects = new ArrayList<>();

    public World(int width, int height) {
        this.width = width;
        this.height = height;
        worldObjects=this.createWorld("./src/main/resources/test_world.xml");
        System.out.println( );
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

    public List<WorldObject> createWorld(String xmlLocation) {
        try {
            return XmlConverter.build(xmlLocation);
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
