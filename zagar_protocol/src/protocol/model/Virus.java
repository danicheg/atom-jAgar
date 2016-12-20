package protocol.model;

public class Virus {

    private final int virusId;
    private final float size;
    private double x;
    private double y;

    public Virus(int cellId, float size, double x, double y) {
        this.virusId = cellId;
        this.size = size;
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getVirusId() {
        return virusId;
    }

    public float getSize() {
        return size;
    }

}
