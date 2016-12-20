package utils.generators;

import java.awt.*;
import java.util.Random;

public class RandomColorGenerator {
    public static Color generateRandomColor() {
        return new Color(new Random().nextInt());
    }
}
