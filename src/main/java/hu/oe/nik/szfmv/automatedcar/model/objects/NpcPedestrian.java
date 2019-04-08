package hu.oe.nik.szfmv.automatedcar.model.objects;

public class NpcPedestrian extends Npc {
    public NpcPedestrian(int x, int y,
                         String healthyImageFileName,
                         String damagedImageFileName,
                         String deadImageFileName) {
        super(x, y, healthyImageFileName, damagedImageFileName, deadImageFileName);
    }
}
