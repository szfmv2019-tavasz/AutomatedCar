package hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.packets;

import java.awt.*;

public class CarPacket implements ReadOnlyCarPacket {

    private Point position;
    private float rotation;
    @Override
    public Point getPosition() {
        return position;
    }

    @Override
    public void setPositon(Point pos) {
        this.position = pos;
    }

    @Override
    public float getRotation() {
        return rotation;
    }

    @Override
    public void setRotation(float rot) {
        this.rotation = rot;
    }
}
