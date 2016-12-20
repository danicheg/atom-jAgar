package model;

import org.eclipse.jetty.util.ConcurrentHashSet;
import org.jetbrains.annotations.NotNull;
import protocol.GameConstraints;
import utils.generators.RandomColorGenerator;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Field {

    @NotNull
    private List<Virus> viruses;
    @NotNull
    private ConcurrentHashSet<Blob> blobs;
    @NotNull
    private ConcurrentHashSet<Food> foods;

    public Field() {
        viruses = new CopyOnWriteArrayList<>();
        foods = new ConcurrentHashSet<>();
        blobs = new ConcurrentHashSet<>();
    }

    public void generateFieldWithFood() {
        int foodAmount = GameConstraints.FIELD_HEIGHT * GameConstraints.FIELD_WIDTH / 100000;
        ConcurrentHashSet<Food> primaryFoods = new ConcurrentHashSet<>();
        for (int i = 0; i < foodAmount; i++) {
            primaryFoods.add(new Food(RandomColorGenerator.generateRandomColor(), new Location()));
        }
        setFoods(primaryFoods);
    }

    public void addBlob(Blob blob) {
        this.blobs.add(blob);
    }

    public void removeBlob(Blob blob) {
        this.blobs.remove(blob);
    }

    @NotNull
    public ConcurrentHashSet<Blob> getBlobs() {
        return this.blobs;
    }

    @NotNull
    public List<Virus> getViruses() {
        return viruses;
    }

    @NotNull
    public ConcurrentHashSet<Food> getFoods() {
        return foods;
    }

    private void setFoods(@NotNull ConcurrentHashSet<Food> food) {
        this.foods = food;
    }

    public void addVirus(@NotNull Virus virus) {
        this.viruses.add(virus);
    }

    public void addFood(@NotNull Food food) {
        this.foods.add(food);
    }
}
