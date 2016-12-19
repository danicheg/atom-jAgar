package model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class Location {

    private static final Logger LOG = LogManager.getLogger(Player.class);

    private double x;
    private double y;

    public Location(double x, double y) {
        this.x = Math.round(x);
        this.y = Math.round(y);
    }


    public Location() {
        Random random = new Random();
        this.x = random.nextInt(GameConstants.FIELD_WIDTH);
        this.y = random.nextInt(GameConstants.FIELD_HEIGHT);
        if (LOG.isInfoEnabled()) {
            LOG.info(toString() + " created");
        }
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public void setX(double x) {
        if (x < GameConstants.FIELD_WIDTH) {
            this.x = x;
        } else {
            this.x = GameConstants.FIELD_WIDTH - 2;
        }
    }


    public void setY(double y) {
        if (y < GameConstants.FIELD_HEIGHT) {
            this.y = y;
        } else {
            this.y = GameConstants.FIELD_HEIGHT - 2;
        }
    }

    public double distanceTo(Location other) {
        return Math.sqrt((other.getX() - x) * (other.getX() - x) + (other.getY() - y) * (other.getY() - y));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Location location = (Location) o;
        return Double.compare(location.x, x) == 0 && Double.compare(location.y, y) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Location{" +
                "x=" + x +
                "y=" + y +
                '}';
    }
}
