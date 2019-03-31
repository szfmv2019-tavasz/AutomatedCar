package hu.oe.nik.szfmv.automatedcar.systemcomponents;

import hu.oe.nik.szfmv.automatedcar.AutomatedCar;
import hu.oe.nik.szfmv.automatedcar.model.World;
import hu.oe.nik.szfmv.automatedcar.model.WorldObject;
import hu.oe.nik.szfmv.automatedcar.model.objects.Collidable;
import hu.oe.nik.szfmv.automatedcar.model.objects.RoadSign;
import hu.oe.nik.szfmv.automatedcar.model.objects.Tree;
import hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.VirtualFunctionBus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.geom.Area;

public class Collision extends SystemComponent {

    private static final Logger LOGGER = LogManager.getLogger();

    private AutomatedCar car;

    public Collision(VirtualFunctionBus virtualFunctionBus, AutomatedCar car) {
        super(virtualFunctionBus);
        this.car = car;
    }

    @Override
    public void loop() {
        for (WorldObject worldObject : World.getInstance().getWorldObjects()) {
            if (worldObject instanceof Collidable && checkCollision(worldObject)) {
                if (worldObject instanceof Tree) {
                    handleGameOver();
// Ezek meg nincsenek a vilagmodelben:
//                } else if (worldObject instanceof NPCPedestrian) {
//                    handleGameOver();
//                } else if (worldObject instanceof NPCCar) {
//                    handleCollisionWithNPCCar();
                } else if (worldObject instanceof RoadSign) {
                    handleCollissionWithRoadSign();
                }
            }
        }
    }

    private boolean checkCollision(WorldObject worldObject) {
        boolean collision = false;

        Shape carShape = car.getShape();
        Shape worldObjectShape = worldObject.getShape();

        // First time check the bounds intersection for better performance (Area intersection is much more expensive)
        if (carShape.getBounds().intersects(worldObjectShape.getBounds())) {
            Area carArea = new Area(carShape);
            carArea.intersect(new Area(worldObjectShape));
            collision = !carArea.isEmpty();
        }

        return collision;
    }

    private void handleGameOver() {


    }

    private void handleCollisionWithNPCCar() {


        damage();
    }

    private void handleCollissionWithRoadSign() {


        damage();
    }

    private void damage() {


    }

}
