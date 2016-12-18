package model;

import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Random;

import static java.lang.Math.PI;
import static java.lang.Math.floor;
import static java.lang.Math.sqrt;

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

    static Color getRandomColor() {
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

    public int getId() {
        return this.id;
    }

    @NotNull
    public Location getLocation() {
        return this.location;
    }

    public void setLocation(@NotNull Location newLocation) {
        this.location.setX(newLocation.getX());
        this.location.setY(newLocation.getY());
    }

    public float getSpeed() {
        return this.speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public  void decreaseSpeed (float decrement) {
        float result = speed - decrement;
        if (result < 0) {
            result = 0;
        }
        speed = result;
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

    public double getX() {
        return this.getLocation().getX();
    }

    public void setX(double x) {
        this.location.setX(x);
    }

    public double getY() {
        return this.getLocation().getY();
    }

    public void setY(double y) {
        this.location.setY(y);
    }

    @NotNull
    public Color getColor() {
        return this.color;
    }

    private float calcRadius(int mass) {
        return (float) sqrt(mass / PI);
    }
}
