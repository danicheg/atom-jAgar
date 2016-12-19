package protocol.model;

public class Functions {

    public static float calculateRadius(int mass) {
        return (float) Math.sqrt(mass * 100);
    }

    public static float calculateRadius(float mass) {
        return (float) Math.sqrt(mass * 100);
    }

    public static double calculateDestinationOnTurn (double oldValue, double aim, double radius, int mass, int defaultMass) {
        return oldValue + 2 * (Math.atan((aim - oldValue) / radius) / Math.log(mass / defaultMass * Math.E));
    }
}
