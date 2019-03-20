package hu.oe.nik.szfmv.automatedcar.visualization;

import hu.oe.nik.szfmv.automatedcar.model.WorldObject;



import java.awt.*;
import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Mock {
    private List<WorldObject> RoadObjects = new ArrayList<>();

    public static float normalize(float value, float min, float max) {
        return Math.abs((value - min) / (max - min));
    }

    private static double convertMatrixToRadians(double m11, double m12, double m21, double m22) {
        double radian = Math.atan2(m21, m11);
        return radian > 0 ? radian : 2 * Math.PI + radian;
    }


    public Mock() {
        RoadObjects.add(new WorldObject((int)normalize(669,0,700),(int)normalize(474,0,770),"road_2lane_90right.png"));
        RoadObjects.add(new WorldObject((int)normalize(669,0,770),(int)normalize(124,0,700),"road_2lane_straight.png"));
        RoadObjects.get(0).setRotation((float)convertMatrixToRadians(1,0,0,1));
        RoadObjects.get(1).setRotation((float)convertMatrixToRadians(1,0,0,1));



    }

    public List<WorldObject> getRoadObjects(){
        return RoadObjects;
    }



}
