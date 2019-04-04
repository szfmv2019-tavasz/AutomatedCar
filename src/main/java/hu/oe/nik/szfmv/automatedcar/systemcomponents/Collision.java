package hu.oe.nik.szfmv.automatedcar.systemcomponents;

import hu.oe.nik.szfmv.automatedcar.AutomatedCar;
import hu.oe.nik.szfmv.automatedcar.model.World;
import hu.oe.nik.szfmv.automatedcar.model.WorldObject;
import hu.oe.nik.szfmv.automatedcar.model.objects.Collidable;
import hu.oe.nik.szfmv.automatedcar.model.objects.Road;
import hu.oe.nik.szfmv.automatedcar.model.objects.RoadSign;
import hu.oe.nik.szfmv.automatedcar.model.objects.Tree;
import hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.VirtualFunctionBus;
import hu.oe.nik.szfmv.automatedcar.visualization.Gui;
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

    private AutomatedCar car;
    private ArrayList<WorldObject> collideItems;

    public Collision(VirtualFunctionBus virtualFunctionBus, AutomatedCar car) {
        super(virtualFunctionBus);
        this.car = car;
        collideItems = new ArrayList<>();
    }

    @Override
    public void loop() {
        for (WorldObject worldObject : World.getInstance().getWorldObjects()) {
            if (worldObject instanceof Collidable && checkCollision(worldObject)) {
                if (worldObject instanceof Tree) {
                    System.out.println("FA");
                    handleCollisionWithTree();
// Ezek meg nincsenek a vilagmodelben:
//                } else if (worldObject instanceof NPCPedestrian) {
//                    handleCollisionWithNPCPedestrian();
//                } else if (worldObject instanceof NPCCar) {
//                    handleCollisionWithNPCCar();
                } else if (worldObject instanceof RoadSign) {
                    handleCollissionWithRoadSign();
                }
            }
        }
    }




    // Nem úgy működik ahogy kellene.
    private boolean checkCollision(WorldObject worldObject) {
        boolean collision = false;

        Shape carShape = car.getShape();
        Shape worldObjectShape = worldObject.getShape();

        if (!collideItems.contains(worldObject)) {
            // First time check the bounds intersection for better performance (Area intersection is much more expensive)
            if (carShape.getBounds().intersects(worldObjectShape.getBounds())) {
                Area carArea = new Area(carShape);
                carArea.intersect(new Area(worldObjectShape));
                collision = !carArea.isEmpty();
                this.collideItems.add(worldObject);
            }

        }
        if (collideItems.contains(worldObject)){
            if (!carShape.getBounds().intersects(worldObjectShape.getBounds())){
                collision = false;
                this.collideItems.remove(worldObject);
            }
        }
        return collision;
    }

    private void handleGameOver() {
        LOGGER.info("Game Over - Collision");
        JFrame exitFrame = new JFrame("Game over");
        //exitFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        //exitFrame.setEnabled();
    }

    private void handleCollisionWithNPCCar() {
        LOGGER.info("Collision with NPC car");


        damage(50);
    }

    private void handleCollisionWithNPCPedestrian() {
        LOGGER.info("Collision with NPC pedestrian");
        handleGameOver();
    }

    private void handleCollisionWithTree() {
        LOGGER.info("Collision with tree");


        damage(70);
    }

    private void handleCollissionWithRoadSign() {
        LOGGER.info("Collision with road sign");

        //this.car.setCarSpeed(0); //Ez szar
        damage(100);
    }

    private void damage(int damageValue) {
        LOGGER.info("Car has been damaged");
        this.car.setAutomatedCarHealth(this.car.getAutomatedCarHealth() - damageValue);
        if (this.car.getAutomatedCarHealth() == 0 ){
            handleGameOver();
        }
    }

}
