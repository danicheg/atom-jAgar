package protocol.model;

public class Virus {
    private final int virusId;
    private final float size;
    private int x;
    private int y;

    public Virus(int cellId, float size, int x, int y) {
        this.virusId = cellId;
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
        return virusId;
    }

    public float getSize() {
        return size;
    }
}
