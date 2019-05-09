package hu.oe.nik.szfmv.automatedcar.systemcomponents;

import hu.oe.nik.szfmv.automatedcar.AutomatedCar;
import hu.oe.nik.szfmv.automatedcar.model.World;
import hu.oe.nik.szfmv.automatedcar.model.WorldObject;
import hu.oe.nik.szfmv.automatedcar.model.objects.*;
import hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.VirtualFunctionBus;
import hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.packets.CollisionPacket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Area;
import java.util.ArrayList;

public class Collision extends SystemComponent {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final int DAMAGE_ROADSIGN = 40;
    private static final int DAMAGE_NPCCAR = 50;
    private static final int DAMAGE_TREE = 70;

    private AutomatedCar car;

    private final CollisionPacket collisionPacket;

    private ArrayList<WorldObject> collideItems;

    public Collision(VirtualFunctionBus virtualFunctionBus, AutomatedCar car) {
        super(virtualFunctionBus);
        this.car = car;
        collisionPacket = new CollisionPacket();
        virtualFunctionBus.collisionPacket = collisionPacket;
        collideItems = new ArrayList<>();
    }

    @Override
    public void loop() {
        for (WorldObject wObject : World.getInstance().getWorldObjects()) {
            if (wObject instanceof Collidable) {
                // Check all collidable world objects
                if (checkCollision(wObject)) {
                    // Collision: handle only once
                    if (!collideItems.contains(wObject)) {
                        LOGGER.info("Add to collideItems: {}", wObject.getClass().getName());
                        collideItems.add(wObject);
                        collisionPacket.setCollision(true);
                        handleCollision(wObject);
                    } else {
                        collisionPacket.setCollision(false);
                    }
                } else {
                    // No collision: remove from the list if it contains the world object
                    if (collideItems.contains(wObject)) {
                        LOGGER.info("Remove from collideItems: {}", wObject.getClass().getName());
                        collideItems.remove(wObject);
                        collisionPacket.setCollision(false);
                    }
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

    private void handleCollision(WorldObject worldObject) {
        if (worldObject instanceof Tree) {
            handleCollisionWithTree();
        } else if (worldObject instanceof NpcPedestrian) {
            handleCollisionWithNPCPedestrian();
        } else if (worldObject instanceof NpcCar) {
            handleCollisionWithNPCCar();
        } else if (worldObject instanceof RoadSign) {
            handleCollisionWithRoadSign();
        }
    }

    private void handleCollisionWithNPCCar() {
        LOGGER.info("Collision with NPC car");
        collisionPacket.setSpeedAfterCollision(0);
        damage(calculateDamage(car.getSpeed(),DAMAGE_NPCCAR));
    }

    private void handleCollisionWithNPCPedestrian() {
        LOGGER.info("Collision with NPC pedestrian");
        handleGameOver();
    }

    private void handleCollisionWithTree() {
        LOGGER.info("Collision with tree");
        collisionPacket.setSpeedAfterCollision(0);
        damage(calculateDamage(car.getSpeed(),DAMAGE_TREE));
    }

    private void handleCollisionWithRoadSign() {
        LOGGER.info("Collision with road sign");
        collisionPacket.setSpeedAfterCollision(car.getSpeed() / 2);
        damage(calculateDamage(car.getSpeed(),DAMAGE_ROADSIGN));
    }

    private int calculateDamage(float speed, int value) {
        return Math.round(((speed/2)*value)/100);
    }

    private void damage(int damageValue) {
        LOGGER.info("Car has been damaged: {}", damageValue);
        car.setAutomatedCarHealth(car.getAutomatedCarHealth() - damageValue);
        if (car.getAutomatedCarHealth() == 0) {
            handleGameOver();
        }
    }

    private void handleGameOver() {
        LOGGER.info("Game Over - Collision");
        collisionPacket.setGameOver(true);

        JFrame exitFrame = new JFrame("Game over");
        exitFrame.setPreferredSize(new Dimension(300,200));
        JButton exitButton = new JButton("Exit");
        exitButton.setSize(50,25); //Nem ezt a nagyságot állítja be.
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        exitFrame.add(new JLabel("Game Over", SwingConstants.CENTER));
        exitFrame.add(exitButton, BorderLayout.SOUTH);
        exitFrame.pack();
        exitFrame.setLocationByPlatform(true);
        exitFrame.setVisible(true);
    }

}
