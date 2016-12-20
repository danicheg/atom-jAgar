package zagar.view;

import org.jetbrains.annotations.NotNull;
import protocol.utils.Calculator;
import zagar.Game;
import zagar.Main;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Cell {

    public final int id;
    public double x;
    public double y;
    public float size;
    public String name = "";
    public double xRender;
    public double yRender;
    public int mass;
    private float sizeRender;
    private int r;
    private int g;
    private int b;

    public Cell(double x, double y, float mass, int id, String name, int r, int g, int b) {
        this.x = x;
        this.y = y;
        this.size = Calculator.calculateRadius(mass);
        this.id = id;
        this.name = name;
        this.xRender = this.x;
        this.yRender = this.y;
        this.r = r;
        this.g = g;
        this.b = b;
        this.sizeRender = this.size;
        this.mass = (int) mass;
    }

    public void tick() {
        this.xRender -= (this.xRender - x) / 5f;
        this.yRender -= (this.yRender - y) / 5f;
        this.sizeRender -= (this.sizeRender - size) / 9f;
        if (Game.cellNames.containsKey(this.id)) {
            this.name = Game.cellNames.get(this.id);
        }
    }

    public void render(@NotNull Graphics2D g, float scale) {
        if (!Game.player.isEmpty()) {
            Color color = new Color(this.r, this.g, this.b);
            if (scale == 1) {
                color = new Color((int) (this.r / 1.3), (int) (this.g / 1.3), (int) (this.b / 1.3));
            }
            g.setColor(color);
            int cellSize = (int) ((this.sizeRender * 2f * scale) * Game.zoom);

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

            int cellX = (int) ((this.xRender - avgX) * Game.zoom) + GameFrame.size.width / 2 - cellSize / 2;
            int cellY = (int) ((this.yRender - avgY) * Game.zoom) + GameFrame.size.height / 2 - cellSize / 2;

            if (cellX < -cellSize - 30 || cellX > GameFrame.size.width + 30 ||
                    cellY < -cellSize - 30 || cellY > GameFrame.size.height + 30) {
                return;
            }

            g.fillOval(cellX, cellY, cellSize, cellSize);

            if (this.name.length() > 0 || this.mass > 30) {
                Font font = Main.frame.canvas.cellsFont;
                BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
                FontMetrics fm = img.getGraphics().getFontMetrics(font);
                int fontSize = fm.stringWidth(this.name);
                outlineString(g, this.name, cellX + cellSize / 2 - fontSize / 2, cellY + cellSize / 2 + cellSize / 10);
            }
        }
    }

    private void outlineString(Graphics2D g, String string, int x, int y) {
        g.setColor(new Color(70, 70, 70));
        g.drawString(string, x - 1, y);
        g.drawString(string, x + 1, y);
        g.drawString(string, x, y - 1);
        g.drawString(string, x, y + 1);
        g.setColor(new Color(255, 255, 255));
        g.drawString(string, x, y);
    }

    @Override
    public String toString() {
        return "Cell{" +
                "x=" + x +
                ", y=" + y +
                ", id=" + id +
                ", size=" + size +
                '}';
    }
}