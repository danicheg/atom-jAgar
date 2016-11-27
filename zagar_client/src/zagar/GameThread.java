package zagar;

public class GameThread extends Thread implements Runnable {

    public GameThread() {
        super("game");
    }

    @Override
    public void run() {
        while (true) {
            long preTickTime = System.nanoTime();
            try {
                Main.updateGame();
            } catch (Throwable e) {
                System.err.println(e);
            }
            if (System.nanoTime() % 100 == 0) {
                Game.fps = 1000 / (System.nanoTime() - preTickTime);
                Main.frame.setTitle("· zAgar · " + Game.fps + "fps");
            }
        }
    }

}
