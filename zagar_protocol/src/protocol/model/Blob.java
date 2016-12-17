package protocol.model;

public class Blob {

    private final int blobId;
    private final float size;
    private int x;
    private int y;

    public Blob(int blobId, float size, int x, int y) {
        this.blobId = blobId;
        this.size = size;
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getVirusId() {
        return blobId;
    }

    public float getSize() {
        return size;
    }
}
