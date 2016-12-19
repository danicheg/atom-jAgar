package utils;


import model.Cell;
import model.Location;
import protocol.GameConstraints;

public class ServerCalculator {

    public static Location calculateLocationOnSplitting(Cell elem) {
        double x = elem.getLocation().getX();
        double y = elem.getLocation().getY();
        if (GameConstraints.FIELD_WIDTH - elem.getX() - elem.getRadius() * 6 > 0) {
            x = elem.getX() + elem.getRadius() * 5;
        } else if (elem.getLocation().getX() - elem.getRadius() * 6 > 0) {
            x = elem.getX() - elem.getRadius() * 5;
        }
        if (GameConstraints.FIELD_HEIGHT - elem.getY() - elem.getRadius() * 6 > 0) {
            y = elem.getY() + elem.getRadius() * 5;
        } else if (elem.getY() - elem.getRadius() * 6 > 0){
            y = elem.getLocation().getY() - elem.getRadius() * 5;
        }
        return new Location(x,y);
    }
}
