package model;

public class Vector {

    private double x;
    private double y;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static Vector createVector(Location start, Location end) {
        return new Vector(end.getX() - start.getX(), end.getY() - start.getY());
    }

    public Vector divide(double value) {
        return new Vector(x / value, y / value);
    }

    public Vector normalize() {
        return divide(length());
    }

    public Vector makeNormal() {
        return new Vector(y * (-1), x * 1);
    }

    public Vector extend(double value) {
        return new Vector(x * value, y * value);
    }

    public Vector plus(Vector vector) {
        return new Vector(x + vector.x, y + vector.y);
    }

    public Vector minus(Vector vector) {
        return new Vector(x - vector.x, y - vector.y);
    }

    public Location getEnd(Location start) {
        return new Location(start.getX() + x, start.getY() + y);
    }

    public Location getStart(Location end) {
        return new Location(end.getX() - x, end.getY() - y);
    }

    public double length() {
      return Math.sqrt(Math.pow(x, 2d) + Math.pow(y, 2d));
    }

    public boolean isCollinear(Vector other) {
        return this.equals(other) || this.extend(-1).equals(other);
    }

    //Vectors are parallel
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Vector vector = (Vector) o;
        return Double.compare(vector.x, x) == 0 && Double.compare(vector.y, y) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    public Location intersectWith(Vector another, Location startOther, Location startThis) {
        if (isCollinear(another)) {
            return null;
        }
        Vector other = another.normalize();
        double alpha = (other.x * (startThis.getY() - startOther.getY())
                - other.y * (startThis.getX() - startOther.getX()))
                / (this.x * other.y - this.y * other.x);
        return extend(alpha).getEnd(startOther);
    }

}