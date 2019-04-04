package hu.oe.nik.szfmv.automatedcar.model;

import hu.oe.nik.szfmv.automatedcar.AutomatedCar;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class WorldObject {

    private static final Logger LOGGER = LogManager.getLogger();
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected float rotation = 0f;
    protected String imageFileName;
    protected BufferedImage image;
    protected Shape shape;

    public WorldObject(int x, int y, String imageFileName) {
        this.x = x;
        this.y = y;
        this.imageFileName = imageFileName;
        initImage();
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
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

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    public BufferedImage getImage() {
        return this.image;
    }

    public void initImage() {
        try {
            image = ImageIO.read(new File(ClassLoader.getSystemResource(imageFileName).getFile()));
            this.width = image.getWidth();
            this.height = image.getHeight();


        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }
    public Shape getShape() {
        generateShape();
        return this.shape;
    }

    public void generateDimens() {
        try {
            BufferedImage image = ImageIO.read(
                new File(
                    ClassLoader.getSystemResource(this.getImageFileName())
                        .getFile()));
            width = image.getWidth();
            height = image.getHeight();

        } catch (IOException e) {
            LOGGER.error(e);
        }
    }

    /**
     *Create rectangle
     */
    public void generateShape() {
       AffineTransform tx = new AffineTransform();
            tx.rotate(-this.getRotation(), this.getX(), this.getY());
            if (!AutomatedCar.class.isInstance(this)) {
                this.shape = tx.createTransformedShape(
                    new Rectangle(
                        (int) this.getX(), (int) this.getY(),
                        this.getWidth(), this.getHeight()));
            } else {
                this.shape = tx.createTransformedShape(
                    new Rectangle(
                        (int) this.getX() - this.getWidth() / 2,
                    (int) this.getY() - this.getHeight() / 2,
                    this.getWidth(), this.getHeight()));
        }
    }

}
