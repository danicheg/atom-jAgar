package model;

import com.sun.istack.internal.Nullable;
import dao.DatabaseAccessLayer;
import org.eclipse.jetty.util.ConcurrentHashSet;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

    public void makeSingleTurn(Player player, float dx, float dy) {
        for (Cell cell : player.getCells()) {
            float oldX = cell.getX();
            float oldY = cell.getY();
            int newX = Math.round(oldX + (dx - oldX) / cell.getRadius());
            int newY = Math.round(oldY + (dy - oldY) / cell.getRadius());
            if (Math.abs(newX) < GameConstants.FIELD_WIDTH && Math.abs(newY) < GameConstants.FIELD_HEIGHT) {
                cell.setX(newX);
                cell.setY(newY);
                Set<Food> foods = player.getSession().getField().getFoods();
                Location first = new Location(Math.round(oldX), Math.round(oldY));
                Location second = new Location(Math.round(newX), Math.round(newY));
                for (Food food : foods) {
                    float fX = food.getLocation().getX();
                    float fY = food.getLocation().getY();
                    Location food_center = new Location(Math.round(fX), Math.round(fY));
                    if (checkDistance(first, second, food_center, food.getMass(), cell.getMass())) {
                        cell.setMass(cell.getMass() + food.getMass());
                        player.getSession().getField().getFoods().remove(food);
                        player.getUser().setScore(player.getScore());
                        DatabaseAccessLayer.updateUser(player.getUser());
                    }
                }
            }
        }
    }

    private boolean checkDistance(Location first, Location second, Location foodCenter,
                                  float foodLength, float cellLength) {
        Vector vector = new Vector(second.getX() - first.getX(), second.getY() - first.getY());
        Vector normalVector = vector.makeNormal().normalize();
        Vector foodNormalVector = normalVector.extend(foodLength);
        Vector cellNormalVector = normalVector.extend(cellLength);

        Location edgeUp = foodNormalVector.getEnd(foodCenter);
        Location edgeDown = foodNormalVector.getStart(foodCenter);

        Location centerCellGone = cellNormalVector.intersectWith(vector, first, foodCenter);
        Location edgeUpCell = cellNormalVector.getEnd(centerCellGone);
        Location edgeDownCell = cellNormalVector.getStart(centerCellGone);

        float distanceOne = edgeDown.distanceTo(edgeUpCell);
        float distanceTwo = edgeUp.distanceTo(edgeDownCell);
        float length = 2 * cellNormalVector.length();
        return ((distanceOne < length) && (distanceTwo < length));
    }

}
