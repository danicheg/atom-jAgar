package zagar.view;

import zagar.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GameCanvas extends JPanel {

    private static final long serialVersionUID = 5570080027060608254L;

    public Font cellsFont = new Font("Ubuntu", Font.BOLD, 18);

    private BufferedImage screen;
    private Font scoreFont = new Font("Ubuntu", Font.BOLD, 30);
    private Font leaderboardFont = new Font("Ubuntu", Font.BOLD, 25);

    public GameCanvas() {
        screen = new BufferedImage(GameFrame.size.width, GameFrame.size.height, BufferedImage.TYPE_INT_ARGB);
        setFont(scoreFont);
        setSize(GameFrame.size);
        setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        render();
    }

    public void render() {
        Graphics ggg = screen.getGraphics();
        Graphics2D g = (Graphics2D) ggg;
        g.setColor(new Color(255, 255, 255));
        g.fillRect(0, 0, GameFrame.size.width, GameFrame.size.height);
        g.setColor(new Color(220, 220, 220));

        if (Game.fps < 30) {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        } else {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }

        if (!Game.player.isEmpty() && Game.zoom > 0.0) {

            int size = 1;
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

            g.setStroke(new BasicStroke(2));

            Double displacement = (GameFrame.size.width / 2) / Game.zoom;
            if (!(displacement.equals(Double.POSITIVE_INFINITY) || displacement.equals(Double.NEGATIVE_INFINITY))) {
                for (double i = avgX - displacement; i < avgX + displacement; i += 100) {
                    i = (int) (i / 100) * 100;
                    int x = (int) ((i - avgX) * Game.zoom) + GameFrame.size.width / 2 - size / 2;
                    g.drawLine(x, (int) Game.minSizeY, x, (int) Game.maxSizeY);
                }
            }

            displacement = (GameFrame.size.height / 2) / Game.zoom;
            if (!(displacement.equals(Double.POSITIVE_INFINITY) || displacement.equals(Double.NEGATIVE_INFINITY))) {
                for (double i = avgY - displacement; i < avgY + displacement; i += 100) {
                    i = (int) (i / 100) * 100;
                    int y = (int) ((i - avgY) * Game.zoom) + GameFrame.size.height / 2 - size / 2;
                    g.drawLine((int) Game.minSizeX, y, (int) Game.maxSizeX, y);
                }
            }

        }

        g.setFont(cellsFont);


        for (int foodsCounter = 0; foodsCounter < Game.foods.length; foodsCounter++) {
            Food food = Game.foods[foodsCounter];
            if (food != null) {
                food.render(g, 1);
            }
        }

        for (int blobsCounter = 0; blobsCounter < Game.blobs.length; blobsCounter++) {
            Blob blob = Game.blobs[blobsCounter];
            if (blob != null) {
                blob.render(g, 1);
                if (blob.mass > 9) {
                    blob.render(g, Math.max(1 - 1f / (blob.mass / 10f), 0.87f));
                }
            }
        }

        for (int cellsCounter = 0; cellsCounter < Game.cells.length; cellsCounter++) {
            Cell cell = Game.cells[cellsCounter];
            if (cell != null) {
                cell.render(g, 1);
                if (cell.mass > 9) {
                    cell.render(g, Math.max(1 - 1f / (cell.mass / 10f), 0.87f));
                }
            }
        }

        for (int virusesCounter = 0; virusesCounter < Game.viruses.length; virusesCounter++) {
            Virus virus = Game.viruses[virusesCounter];
            if (virus != null) {
                virus.render(g, 1);
            }
        }


        g.setFont(scoreFont);
        String scoreString = "Score: " + Game.score;
        g.setColor(new Color(0, 0, 0, 0.5f));
        g.fillRect(GameFrame.size.width - 202, 10, 184, 265);
        g.fillRect(7, GameFrame.size.height - 85, getStringWidth(g, scoreString) + 26, 47);
        g.setColor(Color.WHITE);
        g.drawString(scoreString, 20, GameFrame.size.height - 50);
        g.setFont(leaderboardFont);
        g.drawString(
                "Leaderboard",
                GameFrame.size.width - 110 - getStringWidth(g, "Leaderboard") / 2,
                40
        );
        g.setFont(cellsFont);

        int i = 0;

        for (String s : Game.leaderBoard) {
            if (s != null) {
                g.drawString(
                        s,
                        GameFrame.size.width - 110 - getStringWidth(g, s) / 2,
                        40 + 22 * (i + 1)
                );
            }
            i++;
        }

        g.dispose();

        Graphics gg = getGraphics();
        gg.drawImage(screen, 0, 0, null);
        gg.dispose();
    }

    private int getStringWidth(Graphics2D g, String string) {
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        FontMetrics fm = img.getGraphics().getFontMetrics(g.getFont());
        return fm.stringWidth(string);
    }

}
