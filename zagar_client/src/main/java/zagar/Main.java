package zagar;

import org.jetbrains.annotations.NotNull;
import zagar.view.GameFrame;

import static java.lang.Thread.sleep;

public class Main {

    @NotNull
    public static GameFrame frame;
    @NotNull
    private static Game game;

    public static GameThread thread;

    public static void main(@NotNull String[] args) {
        thread = new GameThread();
        frame = new GameFrame();
        game = new Game();
        start();

    }

    public static void start() {
        Main.frame.setVisible(true);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static synchronized void updateGame() {
        try {
            game.tick();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            frame.render();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
