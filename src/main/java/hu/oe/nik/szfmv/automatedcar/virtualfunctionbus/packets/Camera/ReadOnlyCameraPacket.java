package hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.packets.Camera;

import hu.oe.nik.szfmv.automatedcar.model.objects.RoadSign;

import java.awt.*;

public interface ReadOnlyCameraPacket {

    RoadSign getRoadSign();


    Point[] getTrianglePoints();
}
