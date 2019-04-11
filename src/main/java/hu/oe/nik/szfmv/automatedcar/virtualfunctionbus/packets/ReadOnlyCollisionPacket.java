package hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.packets;

public interface ReadOnlyCollisionPacket {

    boolean isCollision();

    float getSpeedAfterCollision();

    boolean isGameOver();

}
