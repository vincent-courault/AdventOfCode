package adventofcode;

public class Coord3D {
    double x, y, z;

    public Coord3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Coord3D(String x, String y, String z) {
        this.x = Double.parseDouble(x);
        this.y = Double.parseDouble(y);
        this.z = Double.parseDouble(z);
    }

    public double distanceTo(Coord3D o) {
        double dx = x - o.x;
        double dy = y - o.y;
        double dz = z - o.z;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    @Override
    public String toString() {
        return "Coord3D{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}