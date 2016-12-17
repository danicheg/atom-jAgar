package model;

import org.eclipse.jetty.util.ConcurrentHashSet;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Field {

    @NotNull
    private List<Virus> viruses;
    @NotNull
    private ConcurrentHashSet<Food> foods;

    public Field() {
        viruses = new ArrayList<>();
        foods = new ConcurrentHashSet<>();
    }

    public Field(@NotNull ConcurrentHashSet<Food> foods, @NotNull ArrayList<Virus> viruses) {
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

    public void setViruses(@NotNull List<Virus> viruses) {
        this.viruses = viruses;
    }

    public void setFoods(@NotNull ConcurrentHashSet<Food> food) {
        this.foods = food;
    }

    public void addVirus(@NotNull Virus virus) {
        this.viruses.add(virus);
    }

    public void addFood(@NotNull Food food) {
        this.foods.add(food);
    }

    public static Field generatePrimaryState() {
        Field field = new Field();
        int foodAmount = GameConstants.FIELD_HEIGHT * GameConstants.FIELD_WIDTH / 100000;
        ConcurrentHashSet<Food> foods = new ConcurrentHashSet<>();
        for (int i = 0; i < foodAmount; i++) {
            foods.add(new Food(GameUnit.getRandomColor(), new Location()));
        }
        int virusAmount = GameConstants.FIELD_HEIGHT * GameConstants.FIELD_WIDTH / 200000;
        List<Virus> viruses = new ArrayList<>();
        for (int i = 0; i < virusAmount; i++) {
            viruses.add(new Virus(new Location()));
        }
        field.setViruses(viruses);
        field.setFoods(foods);
        return field;
    }

}
