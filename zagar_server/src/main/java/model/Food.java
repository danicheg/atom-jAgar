package model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.util.ConcurrentHashSet;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

import static protocol.enums.GameConstraints.FOOD_MASS;

public class Food extends GameUnit {

    @NotNull
    private static final Logger LOG = LogManager.getLogger(Food.class);

    public Food(@NotNull Color color, @NotNull Location location) {
        super(color, location, FOOD_MASS);
        LOG.info(toString() + " created");
    }

    public static protocol.model.Food[] generateProtocolFoodFromModel(ConcurrentHashSet<Food> foodIn) {
        protocol.model.Food[] foodOut = new protocol.model.Food[foodIn.size()];
        int counter = 0;
        for (model.Food foodGot : foodIn) {
            foodOut[counter] = new protocol.model.Food(foodGot.getLocation().getX(),
                    foodGot.getLocation().getY(),
                    foodGot.getId(),
                    foodGot.getColor().getRed(),
                    foodGot.getColor().getGreen(),
                    foodGot.getColor().getBlue());
            counter++;
        }
        return foodOut;
    }

    @Override
    public boolean equals(@NotNull Object object) {
        if (object.getClass() != Food.class) return false;
        Food food = (Food) object;
        return (this.getLocation().equals(food.getLocation()));
    }

    @Override
    public int hashCode() {
        return (int) this.getLocation().getX() * 31 + (int) this.getLocation().getY() * 37;
    }

    @Override
    public String toString() {
        return "Food{" +
                "color=" + this.getColor() +
                ", location=" + this.getLocation() +
                ", mass=" + this.getMass() +
                ", radius=" + this.getRadius() +
                '}';
    }

}
