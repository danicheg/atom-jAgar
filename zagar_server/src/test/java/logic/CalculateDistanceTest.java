package logic;

import model.Location;
import model.Vector;
import org.junit.Test;

public class CalculateDistanceTest {


    @Test
    public void calculateDistance() {
        System.out.print(checkDistance(new Location(610, 866), new Location(614, 865), new Location(801, 789), 10, 40));
    }


   public boolean checkDistance(Location first, Location second, Location center_food, float food_l, float cell_l) {
        Vector a = new Vector(second.getX() - first.getX(), second.getY() - first.getY());
        Vector b = a.makeNormal()
                .normalize();
        Vector c = b.extend(food_l);
        Vector d = b.extend(cell_l);

        Location edge_up = c.getEnd(center_food);
        Location edge_down = c.getStart(center_food);

        Location center_cell_gone = d.intersectWith(a, first, center_food);
        Location edge_up_cell = d.getEnd(center_cell_gone);
        Location edge_down_cell = d.getStart(center_cell_gone);

        double distanceOne = edge_down.distanceTo(edge_up_cell);
        double distanceTwo = edge_up.distanceTo(edge_down_cell);
        double length = 2 * d.length();
        return ((distanceOne <  length) && (distanceTwo <  length));
    }
}
