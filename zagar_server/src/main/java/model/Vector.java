package model;

public class Vector extends Object {
    public double x;
    public double y;

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

    public Location getEnd(Location start) {
        return new Location(start.getX() + x, start.getY() + y);
    }

    public Location getStart(Location end) {
        return new Location(end.getX() - x, end.getY() - y);
    }

    public double length() {
      return Math.sqrt(x*x + y*y);
    }

    public boolean is_collinear(Vector other) {
        return (this == other || extend(-1) == other);
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
        Vector other = ((Vector) o).normalize();
        Vector normalized = this.normalize();
        return normalized.x == other.x && normalized.y == other.y;
    }


    public Location intersectWith(Vector another, Location startOther, Location startThis) {
        if (is_collinear(another)) return null;
        Vector other = another.normalize();
        double alpha = (other.x * (startThis.getY() - startOther.getY()) - other.y * (startThis.getX() - startOther.getX()))
                / (this.x * other.y - this.y * other.x);
        return extend(alpha).getEnd(startOther);
    }
}