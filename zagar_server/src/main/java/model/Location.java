package model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class Location {

    private static final Logger LOG = LogManager.getLogger(Player.class);

    private int x;
    private int y;

    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Location() {
        Random random = new Random();
        this.x = random.nextInt(GameConstants.FIELD_WIDTH);
        this.y = random.nextInt(GameConstants.FIELD_HEIGHT);
        if (LOG.isInfoEnabled()) {
            LOG.info(toString() + " created");
        }
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public float distanceTo(Location other) {
        return (float) Math.sqrt((other.getX() - x) * (other.getX() - x) + (other.getY() - y) * (other.getY() - y));
    }

    @Override
    public boolean equals(@NotNull Object object) {
        if (object.getClass() != Location.class) return false;
        Location location = (Location) object;
        return (this.getX() == location.getX()) && (this.getY() == location.getY());
    }

    @Override
    public String toString() {
        return "Location{" +
                "x=" + x +
                "y=" + y +
                '}';
    }
}
