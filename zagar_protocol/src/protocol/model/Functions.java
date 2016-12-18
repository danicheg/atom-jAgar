package protocol.model;

public class Functions {

    public static float calculateRadius(int mass) {
        return (float) Math.sqrt(mass * 100);
    }

    public static float calculateRadius(float mass) {
        return (float) Math.sqrt(mass * 100);
    }
}
