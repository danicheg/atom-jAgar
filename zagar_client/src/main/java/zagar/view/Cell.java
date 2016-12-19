package zagar.view;

import org.jetbrains.annotations.NotNull;
import protocol.utils.Calculator;
import zagar.Game;
import zagar.Main;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Cell {

    public double x;
    public double y;
    public final int id;
    public float size;
    public String name = "";

    private float sizeRender;
    public double xRender;
    public double yRender;
    public int mass;
    private int r;
    private int g;
    private int b;
    private float rotation = 0;

    public Cell(double x, double y, float mass, int id) {
        this.x = x;
        this.y = y;
        this.size = Calculator.calculateRadius(mass);
        this.id = id;
        this.xRender = this.x;
        this.yRender = this.y;
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

            int massRender = (int) this.size;
                Polygon hexagon = new Polygon();
                int a = massRender / 20 + 5;
                a = Math.min(a, 50);
                for (int i = 0; i < a; i++) {
                    float pi = 3.14f;
                    int pointX = (int) (cellX + (cellSize / 2) * Math.cos(rotation + i * 2 * pi / a)) + cellSize / 2;
                    int pointY = (int) (cellY + (cellSize / 2) * Math.sin(rotation + i * 2 * pi / a)) + cellSize / 2;
                    hexagon.addPoint(pointX, pointY);
                }
                g.fillPolygon(hexagon);

            if (this.name.length() > 0 || this.mass > 30) {
                Font font = Main.frame.canvas.cellsFont;
                BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
                FontMetrics fm = img.getGraphics().getFontMetrics(font);
                int fontSize = fm.stringWidth(this.name);
                outlineString(g, this.name, cellX + cellSize / 2 - fontSize / 2, cellY + cellSize / 2);
                String cellMass = Integer.toString(this.mass);
                int massSize = fm.stringWidth(cellMass);
                outlineString(g, cellMass, cellX + cellSize / 2 - massSize / 2, cellY + cellSize / 2 + 17);
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