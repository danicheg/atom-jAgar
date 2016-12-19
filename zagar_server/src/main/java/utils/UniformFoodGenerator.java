package utils;

import model.Field;
import model.Food;
import model.Location;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import protocol.enums.GameConstraints;
import ticker.Ticker;

import java.util.Random;

public class UniformFoodGenerator implements FoodGenerator {

    private static final Logger LOG = LogManager.getLogger(UniformFoodGenerator.class);

    @NotNull
    private final Field field;
    private final int threshold;
    private final int foodPerSecond;

    public UniformFoodGenerator(@NotNull Field fieldToPlace, int foodPerSecond, int threshold) {
        field = fieldToPlace;
        this.threshold = threshold;
        this.foodPerSecond = foodPerSecond;
    }

    @Override
    public void tick(long elapsedNanos) {
        if (field.getFoods().size() < threshold) {
            Random random = new Random();
            int x = random.nextInt(GameConstraints.FIELD_WIDTH);
            int y = random.nextInt(GameConstraints.FIELD_HEIGHT);
            Food generatedFood = new Food(
                    RandomColorGenerator.generateRandomColor(),
                    new Location(x, y)
            );
            field.addFood(generatedFood);
        }
    }

    @Override
    public void startGenerating() {
        LOG.info("Start UniformFoodGenerator");
        final Ticker ticker = new Ticker(this, foodPerSecond);
        new Thread(ticker::loop).start();
    }

}
