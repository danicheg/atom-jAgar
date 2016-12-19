package utils;

import java.awt.*;
import java.util.Random;

public class RandomColorGenerator {
    public static Color generateRandomColor() {
        Random random = new Random();
        switch (random.nextInt(5)) {
            case 0:
                return Color.BLACK;
            case 1:
                return Color.BLUE;
            case 2:
                return Color.GREEN;
            case 3:
                return Color.ORANGE;
            case 4:
                return Color.RED;
            case 5:
                return Color.YELLOW;
            default:
                return Color.PINK;
        }
    }
}
