package hu.oe.nik.szfmv.automatedcar;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import hu.oe.nik.szfmv.automatedcar.model.World;
import hu.oe.nik.szfmv.automatedcar.visualization.Gui;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

public class Main {

    // The window handle
    private Gui window;

    public static void main(String[] args) {

        new Main().run();

    }

    public void run() {
        init();
        loop();

        try (Reader reader = new InputStreamReader(ClassLoader.getSystemResource("reference_points.json").openStream())) {
            Gson gson = new Gson();
            // create the type for the collection. In this case define that the collection is of type Dataset
            Type referencePointListType = new TypeToken<Collection<ReferencePoint>>() {
            }.getType();
            List<ReferencePoint> points = gson.fromJson(reader, referencePointListType);
            for (ReferencePoint point : points) {
                System.out.println(point.getName());
                System.out.println(point.getX());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void init() {
        // create the world
        World w = new World(5000, 3000);
        // create an automated car
        AutomatedCar car = new AutomatedCar(20, 20, "car_2_white.png");
        w.addObjectToWorld(car);

        window = new Gui();
    }

    private void loop() {

    }

    public class ReferencePoint {

        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("x")
        @Expose
        private int x;
        @SerializedName("y")
        @Expose
        private int y;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

    }

}