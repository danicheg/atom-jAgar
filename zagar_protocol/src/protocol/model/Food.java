package protocol.model;

/**
 * @author apomosov
 */
public final class Food {

    private double x;
    private double y;
    private int id;
    private int r;
    private int g;
    private int b;

    public Food(double x, double y, int id,int r, int g, int b) {
        this.x = x;
        this.y = y;
        this.id = id;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getId() {
        return id;
    }

    public int getR() {
        return r;
    }

    public int getG() {
        return g;
    }

    public int getB() {
        return b;
    }

}
