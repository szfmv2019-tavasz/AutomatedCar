package hu.oe.nik.szfmv.automatedcar.model.objects;

import hu.oe.nik.szfmv.automatedcar.model.ScriptedPath;
import hu.oe.nik.szfmv.automatedcar.model.Waypoint;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.util.ArrayList;
import java.util.List;

public class NpcCar extends Npc {

    public static final int[] WAYPOINTS_X = new int[]{198, 340, 618, 1300, 3354, 3698, 3732, 3754, 3764, 3610, 3284, 3128, 2919, 759};
    public static final int[] WAYPOINTS_Y = new int[]{701, 2154, 2667, 2736, 2734, 2552, 2536, 2187, 828, 547, 228, 206, 217, 213};

    public NpcCar(int x, int y, String healthyImageFileName, String damagedImageFileName, String deadImageFileName) {
        super(x, y, healthyImageFileName, damagedImageFileName, deadImageFileName);
        createDefaultPath();
    }

    public void createDefaultPath() {
        ScriptedPath path = new ScriptedPath(this);
        this.setPath(path);
        path.setWaypoints(createWaypointList());
        path.setLoopType(ScriptedPath.LoopType.LOOP);
        path.setDirection(ScriptedPath.Direction.FORWARDS);
        path.setMovementSpeed(300);
        path.init();
    }

    private List<Waypoint> createWaypointList() {
        List<Waypoint> waypoints = new ArrayList<>();
        int waypointCount = WAYPOINTS_X.length;
        for (int i = 0; i < waypointCount; i++) {
            Vector2D position = new Vector2D(WAYPOINTS_X[i], WAYPOINTS_Y[i]);
            Waypoint waypoint = new Waypoint(position, 0);
            waypoints.add(waypoint);
        }
        return waypoints;
    }
}
