package protocol.utils;

import static java.lang.Math.sqrt;

public class Calculator {

    public static float calculateRadius(int mass) {
        return (float) sqrt(mass * 100);
    }

    public static float calculateRadius(float mass) {
        return (float) sqrt(mass * 100);
    }

    public static double calculateDestinationOnTurn (double oldValue, double aim, double radius, int mass, int defaultMass) {
        return oldValue + 2 * (Math.atan((aim - oldValue) / radius) / Math.log(sqrt(mass) / sqrt(defaultMass) * Math.E));
    }
}
