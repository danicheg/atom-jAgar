package zagar.view;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import protocol.GameConstraints;
import zagar.Game;
import zagar.controller.KeyboardListener;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class GameFrame extends JFrame {

    private static final long serialVersionUID = 3637327282806739934L;

    private static final Logger LOG = LogManager.getLogger(GameFrame.class);

    public static Dimension size = new Dimension(GameConstraints.FRAME_WIDTH, GameConstraints.FRAME_HEIGHT);
    public static double mouseX;
    public static double mouseY;

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
        LOG.info("[RENDER]");
        LOG.info("CELLS:\n" + Arrays.toString(Game.cells));
        LOG.info("VIRUSES:\n" + Arrays.toString(Game.viruses));
        LOG.info("FOODS:\n" + Arrays.toString(Game.foods));
        LOG.info("BLOBS:\n" + Arrays.toString(Game.blobs));
        LOG.info("PLAYER'S CELLS AMOUNT: " + Game.player.size());
        LOG.info("VIRUSES AMOUNT: " + Game.viruses.length);
        LOG.info("FOOD AMOUNT: " + Game.foods.length);
        LOG.info("BLOBS AMOUNT: " + Game.blobs.length);
        LOG.info("LEADERBOARD:\n" + Arrays.toString(Game.leaderBoard));
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

    //TODO: Possibly here we should recount position of the mouse due to position of the screen
    @NotNull
    private Point getMouseLocation() {
        int x = (MouseInfo.getPointerInfo().getLocation().x - getLocationOnScreen().x);
        int y = (MouseInfo.getPointerInfo().getLocation().y - getLocationOnScreen().y - 24);
        return new Point(x, y);
    }

}
