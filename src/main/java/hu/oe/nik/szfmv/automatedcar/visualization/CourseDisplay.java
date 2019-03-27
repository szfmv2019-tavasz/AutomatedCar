package hu.oe.nik.szfmv.automatedcar.visualization;


import hu.oe.nik.szfmv.automatedcar.model.World;
import hu.oe.nik.szfmv.automatedcar.model.WorldObject;
import hu.oe.nik.szfmv.automatedcar.model.objects.Crossable;
import hu.oe.nik.szfmv.automatedcar.model.objects.Stationary;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * CourseDisplay is for providing a viewport to the virtual world where the simulation happens.
 */
public class CourseDisplay extends JPanel {

    private final int width = 770;
    private final int height = 700;
    private final int backgroundColor = 0xEEEEEE;
    private Gui parent;
    private int worldH = 3000;
    private int worldW = 5120;
    private final int carWidth = 102;
    private final int carHeight = 208;
    private final float scale = 0.89f;
    private   Map<String, Point> refPoints;
    private  final boolean useMock = false;
    private WorldObject car;
    private World world;
    private BufferedImage environment = null;
    /**
     * Initialize the course display
     *
     * @param pt parent Gui
     */
    CourseDisplay(Gui pt) {
        // Not using any layout manager, but fixed coordinates

        setLayout(null);
        setDoubleBuffered(true);
        setBounds(0, 0, width, height);
        setBackground(new Color(backgroundColor));
        try {
            refPoints = Utils.LoadReferencePointsFromXml("./src/main/resources/reference_points.xml");
        } catch (Exception e) {
            e.hashCode();

        }
        parent = pt;
    }


    /**
     * Inherited method that can paint on the JPanel.
     *
     * @param g     {@link Graphics} object that can draw to the canvas
     * @param world {@link World} object that describes the virtual world
     */
    private void paintComponent(Graphics g, World world) {

        g.drawImage(renderDoubleBufferedScreen(world), 0, 0, this);
        this.world  = world;

    }

    private Point2D getOffset(int scaledWidth, int scaledHeight) {
        car = world.getWorldObjects().get(world.getWorldObjects().size()-1);
        double offsetX = 0;
        double offsetY = 0;
        double diffX = (scaledWidth / 2) - car.getX() - carWidth / 2;
        if (diffX < 0) {
            offsetX = diffX;
        }
        double diffY = scaledHeight / 2 - car.getY() - carHeight / 2;
        if (diffY < 0) {
            offsetY = diffY;
        }
        return new Point2D.Double(offsetX, offsetY);
    }

    /**
     * Rendering method to avoid flickering
     *
     * @param world {@link World} object that describes the virtual world
     * @return the ready to render doubleBufferedScreen
     */
    private BufferedImage renderDoubleBufferedScreen(World world) {
        BufferedImage doubleBufferedScreen = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = ( Graphics2D ) doubleBufferedScreen.getGraphics();
        Rectangle r = new Rectangle(0, 0, width, height);
        g2d.setPaint(new Color(backgroundColor));
        g2d.fill(r);

        drawObjects(g2d, world);
        return doubleBufferedScreen;
    }


    public void drawWorld(World world) {

        this.world  = world;
        paintComponent(getGraphics(), world);


    }

    private void drawWorldObject(WorldObject object, Graphics g, double offsetX, double offsetY) {
        BufferedImage image = null;

        try {
            image = ImageIO.read(new File(ClassLoader.getSystemResource(object.getImageFileName()).getFile()));
        } catch (IOException e) {

        }


        Point referencePoint = refPoints.getOrDefault(object.getImageFileName(), null);
        if (referencePoint == null) {
            referencePoint = new Point(0, 0);
        }

        AffineTransform at = new AffineTransform();
        at.scale(scale, scale);
        at.rotate(-object.getRotation(), object.getX() + offsetX, object.getY() + offsetY);
        at.translate(object.getX() - referencePoint.x + offsetX, object.getY() - referencePoint.y + offsetY);

        ((Graphics2D) g).drawImage(image, at, this);
    }

    public void drawEnvironment() {

        environment = new BufferedImage((int) (worldW * scale),
            (int) (worldH * scale), BufferedImage.TYPE_INT_ARGB);

        Graphics2D environmentGrap = environment.createGraphics();



//        if (useMock) {
////            Mock m = new Mock();
////
////            for (WorldObject object : m.getRoadObjects()) {
////
////                drawWorldObject(object, environmentGrap, 0, 0);
////            }
////
////            }
        for (WorldObject object : world.getWorldObjects()) {
            //if(Stationary.class.isAssignableFrom(object.getClass()) ||
            if(Crossable.class.isAssignableFrom(object.getClass()) || Stationary.class.isAssignableFrom(object.getClass())){
                drawWorldObject(object, environmentGrap, 0, 0);


            }

        }

        }


    private void drawObjects(Graphics2D g2d, World world) {


        car = world.getWorldObjects().get(world.getWorldObjects().size()-1);
        int scaledWidth = (int) (width / scale);
        int scaledHeight = (int) (height / scale);
        Point2D offset = getOffset(scaledWidth, scaledHeight);

        //Statikus objektumok kirajzolása csak egyszer
        if (environment == null) {
            drawEnvironment();
        }

        g2d.drawImage(environment, (int) (offset.getX() * scale), (int) (offset.getY() * scale), this);

        //Mozgo objektumok
        for (WorldObject object : world.getWorldObjects()) {
            if(!Stationary.class.isAssignableFrom(object.getClass()) && !Crossable.class.isAssignableFrom(object.getClass())){
                drawWorldObject(object, g2d, offset.getX(), offset.getY());
                System.out.println(object.getX()+": "+object.getY());

            }

        }
    }
}
