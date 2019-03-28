package hu.oe.nik.szfmv.automatedcar.visualization;

import hu.oe.nik.szfmv.automatedcar.model.WorldObject;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("checkstyle:magicnumber")
public class Mock {
    private List<WorldObject> roadObjects = new ArrayList<>();

    public Mock() {
        roadObjects.add(new WorldObject(474, 669, "road_2lane_straight.png"));
        roadObjects.add(new WorldObject(124, 669, "road_2lane_straight.png"));
        roadObjects.add(new WorldObject(124, 1019, "road_2lane_straight.png"));
        roadObjects.add(new WorldObject(124, 1369, "road_2lane_straight.png"));
        roadObjects.add(new WorldObject(124, 1719, "road_2lane_90right.png"));


        roadObjects.get(0).setRotation((float) convertMatrixToRadians(1, 0, 0, 1));
        roadObjects.get(1).setRotation((float) convertMatrixToRadians(1, 0, 0, 1));
        roadObjects.get(2).setRotation((float) convertMatrixToRadians(1, 0, 0, 1));
        roadObjects.get(3).setRotation((float) convertMatrixToRadians(1, 0, 0, 1));
        roadObjects.get(4).setRotation((float) convertMatrixToRadians(1, 0, 0, 1));


    }

    public static float normalize(float value, float min, float max) {
        return Math.abs((value - min) / (max - min));
    }

    private static double convertMatrixToRadians(double m11, double m12, double m21, double m22) {
        double radian = Math.atan2(m21, m11);
        return radian > 0 ? radian : 2 * Math.PI + radian;
    }

    public List<WorldObject> getRoadObjects() {
        return roadObjects;
    }


}
