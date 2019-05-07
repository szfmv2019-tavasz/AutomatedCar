package hu.oe.nik.szfmv.automatedcar.systemcomponents;

import hu.oe.nik.szfmv.automatedcar.model.WorldObject;
import hu.oe.nik.szfmv.automatedcar.model.objects.RoadSign;
import hu.oe.nik.szfmv.automatedcar.sensors.Triangle;
import hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.VirtualFunctionBus;
import hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.packets.camera.CameraPacket;
import hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.packets.ReadOnlyCarPacket;
import hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.packets.camera.SimpleDetector;

import java.awt.*;

import java.util.List;

public class Camera extends SystemComponent {

    private static final double CAMERAANGLE = 60;
    private static final double CAMERARANGE = 10 * 50; // 50 pixel 1 méter
    private CameraPacket cameraPacket;

    public Camera(VirtualFunctionBus virtualFunctionBus) {
        super(virtualFunctionBus);
        cameraPacket = CameraPacket.getPacket();
        virtualFunctionBus.cameraPacket = cameraPacket;
    }


    private RoadSign setRoadSign(ReadOnlyCarPacket carPacket) {
        Point cameraPosition = new Point(0, 0);
        cameraPosition.x = (int)carPacket.getPosition().x;
        cameraPosition.y = (int)carPacket.getPosition().y;
        double cameraRotation = carPacket.getRotation();

        Triangle triangle = new Triangle();
        Point[] trianglePoints = triangle.createTrianglePoints(cameraPosition, CAMERARANGE, CAMERAANGLE,
            (cameraRotation));
        cameraPacket.setTriangle(trianglePoints);

        List<WorldObject>  objects;
        objects = SimpleDetector.getDetector().getWorldObjects(trianglePoints[0], trianglePoints[1], trianglePoints[2]);

        RoadSign roadSigns = null;
        for (int i = 0; i < objects.size(); i++) {
            if (objects.get(i) instanceof RoadSign) {
                roadSigns = (RoadSign) objects.get(i);
            }
        }
        return roadSigns;
    }





    @Override
    public void loop() {
        ReadOnlyCarPacket carPacket = virtualFunctionBus.carPacket;
        RoadSign roadSign = setRoadSign(carPacket);
        cameraPacket.setRoadSign(roadSign);
        if (cameraPacket.getRoadSign() != null) {
            //System.out.println(cameraPacket.getRoadSign().getImageFileName());
        }

    }
}
