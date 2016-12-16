package model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author apomosov
 */
public class Field {

    private static final int width = GameConstants.FIELD_WIDTH;
    private static final int height = GameConstants.FIELD_HEIGHT;
    @NotNull
    private List<Virus> viruses;
    @NotNull
    private HashSet<Food> foods;

    public Field() {
        viruses = new ArrayList<>();
        foods = new HashSet<>();
    }

    public Field(@NotNull HashSet<Food> foods, @NotNull ArrayList<Virus> viruses) {
        this.viruses = viruses;
        this.foods = foods;
    }

    @NotNull
    public List<Virus> getViruses() {
        return viruses;
    }

    @NotNull
    public Set<Food> getFoods() {
        return foods;
    }

    public static int getWidth() {
        return Field.width;
    }

    public static int getHeight() {
        return Field.height;
    }

    public void setViruses(@NotNull List<Virus> viruses) {
        this.viruses = viruses;
    }

    public void setFoods(@NotNull HashSet<Food> food) {
        this.foods = food;
    }

    public void addViruse(@NotNull Virus virus) {
        this.viruses.add(virus);
    }

    public void addFood(@NotNull Food food) {
        this.foods.add(food);
    }

    public static Field generatePrimaryState() {
        Field field = new Field();
        int foodAmount = Field.getHeight() * Field.getWidth() / 100000;
        HashSet<Food> foods = new HashSet<>();
        for (int i=0; i < foodAmount; i++) {
            foods.add(new Food(GameUnit.getRandomColor(), new Location()));
        }
        int virusAmount = Field.getHeight() * Field.getWidth() / 200000;
        List<Virus> viruses = new ArrayList<>();
        for (int i=0; i < virusAmount; i++) {
            viruses.add(new Virus(new Location()));
        }
        field.setViruses(viruses);
        field.setFoods(foods);
        return field;
    }

}
