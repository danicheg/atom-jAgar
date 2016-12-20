package protocol.model;

public class Blob {

    private final int blobId;
    private final float size;
    private double x;
    private double y;

    public Blob(int blobId, float size, double x,double y) {
        this.blobId = blobId;
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
        return blobId;
    }

    public float getSize() {
        return size;
    }
}
