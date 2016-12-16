package protocol.model;

/**
 * @author apomosov
 */
public final class Food {

    private int x;
    private int y;
    private int id;
    private int r;
    private int g;
    private int b;

    public Food(int x, int y, int id,int r, int g, int b) {
        this.x = x;
        this.y = y;
        this.id = id;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public int getX() {
        return x;
    }

    public int getY() {
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
