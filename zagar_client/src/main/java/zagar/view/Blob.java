package zagar.view;

import org.jetbrains.annotations.NotNull;
import zagar.Game;

import java.awt.*;

public class Blob {

    private int x, y;
    private int id;
    private float size;
    private final int r, g, b;

    private float sizeRender;
    public double xRender;
    public double yRender;
    public int mass;

    public Blob(int x, int y, float size, int id) {
        this.r = 111;
        this.g = 111;
        this.b = 111;
        this.x = x;
        this.y = y;
        this.size = size;
        this.id = id;
        this.xRender = this.x;
        this.yRender = this.y;
        this.sizeRender = this.size;
    }

    public void tick() {
        this.xRender -= (this.xRender - x) / 5f;
        this.yRender -= (this.yRender - y) / 5f;
        this.sizeRender -= (this.sizeRender - size) / 9f;
        this.mass = Math.round((this.sizeRender * this.sizeRender) / 100);
    }

    public void render(@NotNull Graphics2D g, float scale) {

        Color color = new Color(this.r, this.g, this.b);
        g.setColor(color);
        int size = (int) ((this.size * 2f * scale) * Game.zoom);

        float avgX = 0;
        float avgY = 0;

        for (Cell c : Game.player) {
            if (c != null) {
                avgX += c.xRender;
                avgY += c.yRender;
            }
        }

        avgX /= Game.player.size();
        avgY /= Game.player.size();

        int x = (int) ((this.x - avgX) * Game.zoom) + GameFrame.size.width / 2 - size / 2;
        int y = (int) ((this.y - avgY) * Game.zoom) + GameFrame.size.height / 2 - size / 2;

        if (x < -size - 30 || x > GameFrame.size.width + 30 || y < -size - 30 || y > GameFrame.size.height + 30) {
            return;
        }
        g.fillOval(x,y,size,size);
    }

    @Override
    public String toString() {
        return "Virus{" +
                "x=" + x +
                ", y=" + y +
                ", id=" + id +
                ", size=" + size +
                '}';
    }
}
