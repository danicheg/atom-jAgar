package zagar;

import org.jetbrains.annotations.NotNull;
import zagar.view.GameFrame;

public class Main {

    @NotNull
    public static GameFrame frame;
    @NotNull
    public static Game game;

    private static GameThread thread;

    public static void main(@NotNull String[] args) {
        try {
            game = new Game();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        thread = new GameThread();
        frame = new GameFrame();
        start();
    }

    private static void start() {
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
