package hu.oe.nik.szfmv.automatedcar.model.objects;

public class ParkingPlace extends Crossable {
    /**
     * Creates an object of the virtual world on the given coordinates with the given image.
     *
     *
     * @param x             the initial x coordinate of the object
     * @param y             the initial y coordinate of the object
     * @param imageFileName the filename of the image representing the object in the virtual world
     */
    public ParkingPlace(int x, int y, String imageFileName) {
        super(x, y, imageFileName);
    }

    /**
     * Creates an object with default parameter values.
     */
    public ParkingPlace() {
        super(0, 0, "parking_space_parallel.png");
    }
}
