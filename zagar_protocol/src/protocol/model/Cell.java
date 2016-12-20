package protocol.model;

/**
 * @author apomosov
 */
public final class Cell {

    private final int cellId;
    private final int playerId;
    private String name;
    private final int size;
    private double x;
    private double y;

    public Cell(int cellId, int playerId, String name, int mass, double x, double y) {
        this.cellId = cellId;
        this.playerId = playerId;
        this.name = name;
        this.size = mass;
        this.x = x;
        this.y = y;
    }

    public int getPlayerId() {
        return playerId;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getCellId() {
        return cellId;
    }

    public int getSize() {
        return size;
    }

    public String getName() { return name;}
    
}
