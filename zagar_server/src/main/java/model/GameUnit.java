package model;

import org.jetbrains.annotations.NotNull;
import protocol.utils.Calculator;
import utils.RandomColorGenerator;

import java.awt.*;
import java.util.Random;

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

    public GameUnit(@NotNull Location location, int mass) {
        this.id = new Random().nextInt();
        this.color = RandomColorGenerator.generateRandomColor();
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
        this.location.setX(newLocation.getX());
        this.location.setY(newLocation.getY());
    }

    public float getSpeed() {
        return this.speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void decreaseSpeed(float decrement) {
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
        return Calculator.calculateRadius(mass);
    }
}
