package hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.packets;

import java.awt.*;

public interface ReadOnlyCarPacket {

    public Point getPosition();
    public void setPositon(Point pos);
    public  float getRotation();
    public  void setRotation(float rot);

}
