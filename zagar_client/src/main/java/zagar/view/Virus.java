package zagar.view;


import org.jetbrains.annotations.NotNull;
import zagar.Game;
import zagar.Main;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Virus {

        public double x, y;
        public int id;
        public float size;

        private float sizeRender;
        public double xRender;
        public double yRender;
        public int mass;
        private float rotation = 0;

        public Virus(double x, double y, float size, int id) {
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
            this.rotation += (1f / (Math.max(this.mass, 20) * 2));
        }

        public void render(@NotNull Graphics2D g, float scale) {

                g.setColor(Color.GREEN);
                int virusSize = (int) ((this.sizeRender * 2f * scale) * Game.zoom);

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

                int virusX = (int) ((this.xRender - avgX) * Game.zoom) + GameFrame.size.width / 2 - virusSize / 2;
                int virusY = (int) ((this.yRender - avgY) * Game.zoom) + GameFrame.size.height / 2 - virusSize / 2;

                if (virusX < -virusSize - 30 || virusX > GameFrame.size.width + 30 ||
                        virusY < -virusSize - 30 || virusY > GameFrame.size.height + 30) {
                    return;
                }

                int massRender = (int) ((this.size * this.size) / 100);

                    Polygon hexagon = new Polygon();
                    int a = 2 * (massRender / 8 + 10);
                    a = Math.min(a, 100);
                    for (int i = 0; i < a; i++) {
                        float pi = 3.14f;
                        int spike = 0;
                        if (i % 2 == 0) {
                            spike = (int) (20 * Math.min(Math.max(1, massRender / 80f), 8) * Game.zoom);
                        }
                        hexagon.addPoint(
                                (int) (virusX + ((virusSize + spike) / 2) * Math.cos(-rotation + i * 2 * pi / a))
                                        + virusSize / 2,
                                (int) (virusY + ((virusSize + spike) / 2) * Math.sin(-rotation + i * 2 * pi / a))
                                        + virusSize / 2
                        );
                    }
                    g.fillPolygon(hexagon);
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
