package zagar.view;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import zagar.Game;
import zagar.controller.KeyboardListener;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class GameFrame extends JFrame {

    private static final long serialVersionUID = 3637327282806739934L;

    @NotNull
    private static final Logger log = LogManager.getLogger(GameFrame.class);

    @NotNull
    public static Dimension size = new Dimension(1100, 700);
    public static double mouseX, mouseY;

    private static long startTime = System.currentTimeMillis();
    private static long frames = 0;

    @NotNull
    public GameCanvas canvas;

    public GameFrame() {
        setSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setPreferredSize(size);
        addKeyListener(new KeyboardListener());
        canvas = new GameCanvas();
        getContentPane().add(canvas);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("· zAgar ·");
        pack();
        setVisible(false);
    }

    public void render() {
        log.info("[RENDER]");
        log.info("CELLS:\n" + Arrays.toString(Game.cells));
        log.info("VIRUSES:\n" + Arrays.toString(Game.viruses));
        log.info("FOODS:\n" + Arrays.toString(Game.foods));
        log.info("PLAYER'S CELLS AMOUNT: " + Game.player.size());
        log.info("VIRUSES AMOUNT: " + Game.viruses.length);
        log.info("FOOD AMOUNT: " + Game.foods.length);
        log.info("LEADERBOARD:\n" + Arrays.toString(Game.leaderBoard));
        Point mouseP = getMouseLocation();
        mouseX = mouseP.getX();
        mouseY = mouseP.getY();
        frames++;
        if (System.currentTimeMillis() - startTime > 1000) {
            if (frames < 10) {
                System.err.println("LAG > There were only " + frames + " frames in "
                        + (System.currentTimeMillis() - startTime) + "ms!!!");
            }
            frames = 0;
            startTime = System.currentTimeMillis();
        }
        canvas.render();
    }

    @NotNull
    private Point getMouseLocation() {
        int x = (MouseInfo.getPointerInfo().getLocation().x - getLocationOnScreen().x);
        int y = (MouseInfo.getPointerInfo().getLocation().y - getLocationOnScreen().y - 24);
        return new Point(x, y);
    }

}
