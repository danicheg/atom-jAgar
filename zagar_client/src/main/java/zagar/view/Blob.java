package zagar.view;

import org.jetbrains.annotations.NotNull;
import protocol.utils.Calculator;
import zagar.Game;

import java.awt.*;

public class Blob {

    private final int id;
    private final float size;
    private final int r;
    private final int g;
    private final int b;
    public double xRender;
    public double yRender;
    public int mass;
    private double x;
    private double y;
    private float sizeRender;

    public Blob(double x, double y, float mass, int id) {
        this.r = 111;
        this.g = 111;
        this.b = 111;
        this.x = x;
        this.y = y;
        this.size = Calculator.calculateRadius(mass);
        this.mass = (int) mass;
        this.id = id;
        this.xRender = this.x;
        this.yRender = this.y;
        this.sizeRender = this.size;
    }

    public void tick() {
        this.xRender -= (this.xRender - x) / 5f;
        this.yRender -= (this.yRender - y) / 5f;
        this.sizeRender -= (this.sizeRender - size) / 9f;
    }

    public void render(@NotNull Graphics2D g, float scale) {

        Color color = new Color(this.r, this.g, this.b);
        g.setColor(color);
        int blobSize = (int) ((this.size * 2f * scale) * Game.zoom);

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

        int blobX = (int) ((this.x - avgX) * Game.zoom) + GameFrame.size.width / 2 - blobSize / 2;
        int blobY = (int) ((this.y - avgY) * Game.zoom) + GameFrame.size.height / 2 - blobSize / 2;

        if (blobX < -blobSize - 30 || blobX > GameFrame.size.width + 30 ||
                blobY < -blobSize - 30 || blobY > GameFrame.size.height + 30) {
            return;
        }
        g.fillOval(blobX, blobY, blobSize, blobSize);
    }

    @Override
    public String toString() {
        return "Blob{" +
                "x=" + x +
                ", y=" + y +
                ", id=" + id +
                ", size=" + size +
                '}';
    }
}
