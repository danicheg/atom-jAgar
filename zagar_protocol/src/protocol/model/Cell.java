package protocol.model;

import java.awt.*;

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
    private int r;
    private int g;
    private int b;

    public Cell(int cellId, int playerId, String name, int mass, double x, double y, int r, int g, int b) {
        this.cellId = cellId;
        this.playerId = playerId;
        this.name = name;
        this.size = mass;
        this.x = x;
        this.y = y;
        this.r = r;
        this.g = g;
        this.b = b;
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

    public Color getColor() {
        return new Color(r,g,b);
    }
    
}
