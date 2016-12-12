package model;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author apomosov
 */
public class Field {

    private final int width;
    private final int height;
    @NotNull
    private final List<Virus> viruses;
    @NotNull
    private final HashSet<Food> foods;

    public Field() {
        width = GameConstants.FIELD_WIDTH;
        height = GameConstants.FIELD_HEIGHT;
        viruses = new ArrayList<>();
        foods = new HashSet<>();
    }

    @NotNull
    public List<Virus> getViruses() {
        return viruses;
    }

    @NotNull
    public Set<Food> getFoods() {
        return foods;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
