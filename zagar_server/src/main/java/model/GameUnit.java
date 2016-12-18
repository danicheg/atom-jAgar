package model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Random;
import java.awt.*;

import static java.lang.Math.*;

public class GameUnit {
    @NotNull
    private int id;
    @NotNull
    private Location location;
    @NotNull
    private Color color;
    private float speed = 0;
    private float radius;
    private int mass;

    public GameUnit(@NotNull Color color, @NotNull Location location, float speed, int mass) {
        this.id = new Random().nextInt();
        this.speed = speed;
        this.color = color;
        this.location = location;
        this.mass = mass;
        this.radius = calcRadius(mass);
    }

    public GameUnit(@NotNull Color color, @NotNull Location location, int mass) {
        this.id = new Random().nextInt();
        this.color = color;
        this.location = location;
        this.mass = mass;
        this.radius = calcRadius(mass);
    }

    public GameUnit(@NotNull Location location, float speed, int mass) {
        this.id = new Random().nextInt();
        this.speed = speed;
        this.location = location;
        this.color = getRandomColor();
        this.mass = mass;
        this.radius = calcRadius(mass);
    }

    public GameUnit(@NotNull Location location, int mass) {
        this.id = new Random().nextInt();
        this.color = getRandomColor();
        this.location = location;
        this.mass = mass;
        this.radius = calcRadius(mass);
    }

    public int getId() {
        return this.id;
    }
    @NotNull
    public Location getLocation() {
        return this.location;
    }

    public void setLocation(@NotNull Location newLocation) {
        this.location = newLocation;
    }

    public float getSpeed() {
        return this.speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getRadius() {
        return this.radius;
    }

    public int getMass() {
        return this.mass;
    }

    public void setMass(int mass) {
        this.mass = mass;
        calcRadius(mass);
    }

    public int getX() {
        return this.getLocation().getX();
    }

    public int getY() {
        return this.getLocation().getY();
    }

    public void setX(float x) {
        int newX = Math.round(x);
        this.setLocation(new Location(newX, this.getLocation().getY()));
    }

    public void setY(float x) {
        int newY = Math.round(x);
        this.setLocation(new Location(this.getLocation().getX(), newY));
    }

    @NotNull
    public Color getColor() {
        return this.color;
    }

    static Color getRandomColor() {
        Random random = new Random();
        switch (random.nextInt(5)) {
            case 0: return Color.BLACK;
            case 1: return Color.BLUE;
            case 2: return Color.GREEN;
            case 3: return Color.ORANGE;
            case 4: return Color.RED;
            case 5: return Color.YELLOW;
            default: return Color.PINK;
        }
    }

    private float calcRadius(int mass) {
        return (float) sqrt(mass/PI);
    }
}
