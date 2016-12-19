package model;

import org.eclipse.jetty.util.ConcurrentHashSet;
import org.jetbrains.annotations.NotNull;
import utils.RandomColorGenerator;

import java.util.ArrayList;
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

    public Field(@NotNull ConcurrentHashSet<Food> foods, @NotNull CopyOnWriteArrayList<Virus> viruses) {
        this.viruses = viruses;
        this.foods = foods;
        blobs = new ConcurrentHashSet<>();
    }

    public Field generatePrimaryState() {
        int foodAmount = GameConstants.FIELD_HEIGHT * GameConstants.FIELD_WIDTH / 100000;
        ConcurrentHashSet<Food> foods = new ConcurrentHashSet<>();
        for (int i = 0; i < foodAmount; i++) {
            foods.add(new Food(RandomColorGenerator.generateRandomColor(), new Location()));
        }
        int virusAmount = GameConstants.FIELD_HEIGHT * GameConstants.FIELD_WIDTH / 200000;
        List<Virus> viruses = new ArrayList<>();
        for (int i = 0; i < virusAmount; i++) {
            viruses.add(new Virus(new Location()));
        }
        setViruses(viruses);
        setFoods(foods);
        return this;
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

    public void setViruses(@NotNull List<Virus> viruses) {
        this.viruses = viruses;
    }

    @NotNull
    public ConcurrentHashSet<Food> getFoods() {
        return foods;
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
}
