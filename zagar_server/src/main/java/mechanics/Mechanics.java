package mechanics;

import dao.DatabaseAccessLayer;
import main.ApplicationContext;
import main.Service;
import matchmaker.MatchMaker;
import messagesystem.Message;
import messagesystem.MessageSystem;
import messagesystem.messages.ReplicateLbd;
import messagesystem.messages.ReplicateMsg;
import model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import ticker.Tickable;
import ticker.Ticker;

import java.util.Set;

public class Mechanics extends Service implements Tickable {

    private static final Logger log = LogManager.getLogger(Mechanics.class);

    public Mechanics() {
        super("mechanics");
    }

    @Override
    public void run() {
        log.info(getAddress() + " started");
        Ticker ticker = new Ticker(this, 50);
        ticker.loop();
    }

    @Override
    public void tick(long elapsedNanos) {
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            log.error(e);
            Thread.currentThread().interrupt();
        }

        log.info("Start replication");

        botMove();

        @NotNull MessageSystem messageSystem = ApplicationContext.instance().get(MessageSystem.class);
        Message messageReplicate = new ReplicateMsg(this.getAddress());
        messageSystem.sendMessage(messageReplicate);
        Message messageLeaderboard = new ReplicateLbd(this.getAddress());
        messageSystem.sendMessage(messageLeaderboard);
        messageSystem.execForService(this);
    }

    public void makeMove(float dx, float dy, String name) {
        Player player = Player.getPlayerByName(name);
        if (player != null) {
            for (Cell cell : player.getCells()) {
                double oldX = cell.getX();
                double oldY = cell.getY();
                double radius = cell.getRadius();
                int mass = cell.getMass();
                double newX = oldX + (Math.atan((dx - oldX) / radius) / Math.log(mass / 40 * Math.E));
                double newY = oldY + (Math.atan((dy - oldY) / radius) / Math.log(mass / 40 * Math.E));


                if (Math.abs(newX) < GameConstants.FIELD_WIDTH && Math.abs(newY) < GameConstants.FIELD_HEIGHT) {
                    cell.setX(newX);
                    cell.setY(newY);
                    Set<Food> foods = player.getSession().sessionField().getFoods();
                    Location first = new Location(oldX, oldY);
                    Location second = new Location(newX, newY);
                    for (Food food : foods) {
                        double foodX = food.getX();
                        double foodY = food.getY();
                        Location foodCenter = new Location(foodX, foodY);
                        if (checkDistance(first, second, foodCenter, food.getMass(), cell.getMass())) {
                            cell.setMass(cell.getMass() + 1);
                            player.getSession().sessionField().getFoods().remove(food);
                            player.getUser().setScore(player.getScore());
                            DatabaseAccessLayer.updateUser(player.getUser());
                        }
                    }
                    Set<Blob> blobs = player.getSession().sessionField().getBlobs();
                    for (Blob blob : blobs) {
                        double blobX = blob.getX();
                        double blobY = blob.getY();
                        Location blobCenter = new Location(blobX, blobY);
                        if (checkDistance(first, second, blobCenter, blob.getMass(), cell.getMass())) {
                            cell.setMass(cell.getMass() + 13);
                            player.getSession().sessionField().removeBlob(blob);
                            player.getUser().setScore(player.getScore());
                            DatabaseAccessLayer.updateUser(player.getUser());
                        }
                    }
                }
            }
        }
    }

    public void ejectMove(double x, double y, String name) {
        Player player = Player.getPlayerByName(name);
        if (player != null) {
            Cell cell = player.getMostMassiveCell();
            if (cell.getMass() >= GameConstants.DEFAULT_PLAYER_CELL_MASS + GameConstants.BLOB_MASS_CREATE) {
                Location mouseLocation = new Location(x, y);
                Blob blob = new Blob(mouseLocation, cell);
                cell.setMass(cell.getMass() - GameConstants.BLOB_MASS_CREATE);
                player.getSession().sessionField().addBlob(blob);
                player.getUser().setScore(player.getScore());
                DatabaseAccessLayer.updateUser(player.getUser());
            }
        }
    }


    public void splitMove(String name) {
        Player player = Player.getPlayerByName(name);
        if (player != null) {
            for (Cell elem : player.getCells()) {
                if (player.getCells().size() < 16) {
                    int oldMass = elem.getMass();
                    if (oldMass >= GameConstants.DEFAULT_PLAYER_CELL_MASS * 2) {
                        elem.setMass(oldMass / 2);
                        Cell newCell = new Cell(new Location(elem.getLocation().getX() + elem.getRadius() * 5,
                                elem.getLocation().getY() + elem.getRadius() * 5));
                        player.addCell(newCell);
                    }
                }
            }
        }
    }

    private void botMove() {
        for (GameSession gameSession : ApplicationContext.instance().get(MatchMaker.class).getActiveGameSessions()) {
            for (Blob blob: gameSession.sessionField().getBlobs()) {
                blob.makeMove();
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

        double distanceOne = edgeDown.distanceTo(edgeUpCell);
        double distanceTwo = edgeUp.distanceTo(edgeDownCell);
        double length = 2 * cellNormalVector.length();
        return (distanceOne < length) && (distanceTwo < length);
    }

}
