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
import protocol.utils.Calculator;
import protocol.GameConstraints;
import ticker.Tickable;
import ticker.Ticker;
import utils.ServerCalculator;

import java.util.List;
import java.util.Set;

public class Mechanics extends Service implements Tickable {

    private static final Logger log = LogManager.getLogger(Mechanics.class);

    public Mechanics() {
        super("mechanics");
    }

    @Override
    public void run() {
        log.info(getAddress() + " started");
        Ticker ticker = new Ticker(this, 200);
        ticker.loop();
    }

    @Override
    public void tick(long elapsedNanos) {
        try {
            Thread.sleep(5);
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
            Vector vectorCenter = buildMassCenter(player, dx, dy);
            for (Cell cell : player.getCells()) {
                double oldX = cell.getX();
                double oldY = cell.getY();
                double radius = cell.getRadius();
                int mass = cell.getMass();

                for (Cell cell2 : player.getCells()) {
                    if (!cell.equals(cell2)) {
                        if (cell.getLocation().distanceTo(cell2.getLocation()) < 2.5 * cell.getRadius()) {
                                cell.setMass(cell.getMass() + cell2.getMass());
                                player.removeCell(cell2);
                        }
                    }
                }

                //Moving
                double newX = Calculator.calculateDestinationOnTurn(oldX, dx, radius, mass, GameConstraints.DEFAULT_PLAYER_CELL_MASS);
                double newY = Calculator.calculateDestinationOnTurn(oldY, dy, radius, mass, GameConstraints.DEFAULT_PLAYER_CELL_MASS);
                Location pointWithoutCorrecting = new Location(newX, newY);
                Location pointWithCorrecting = vectorCenter.getEnd(cell.getLocation());
                Location properPoint = Vector.createVector(pointWithCorrecting, pointWithoutCorrecting)
                        .divide(4)
                        .getEnd(pointWithCorrecting);

                if (properPoint.getX() < GameConstraints.FIELD_WIDTH
                        && properPoint.getY() < GameConstraints.FIELD_HEIGHT
                        && properPoint.getX() > 0
                        && properPoint.getY() > 0) {

                    //Eating food
                    cell.setLocation(properPoint);
                    Set<Food> foods = player.getSession().sessionField().getFoods();
                    Location first = new Location(oldX, oldY);
                    Location second = new Location(newX, newY);
                    for (Food food : foods) {
                        double foodX = food.getX();
                        double foodY = food.getY();
                        Location foodCenter = new Location(foodX, foodY);
                        if (checkDistance(first, second, foodCenter, food.getRadius(), cell.getRadius())) {
                            cell.setMass(cell.getMass() + 1);
                            player.getSession().sessionField().getFoods().remove(food);
                            player.getUser().setScore(player.getScore());
                            DatabaseAccessLayer.updateUser(player.getUser());
                        }
                    }

                    //Eating blob
                    Set<Blob> blobs = player.getSession().sessionField().getBlobs();
                    for (Blob blob : blobs) {
                        double blobX = blob.getX();
                        double blobY = blob.getY();
                        Location blobCenter = new Location(blobX, blobY);
                        if (checkDistance(first, second, blobCenter, blob.getRadius(), cell.getRadius())) {
                            cell.setMass(cell.getMass() + 13);
                            player.getSession().sessionField().removeBlob(blob);
                            player.getUser().setScore(player.getScore());
                            DatabaseAccessLayer.updateUser(player.getUser());
                        }
                    }

                    //Splitting if is more than virus
                    int oldMass = cell.getMass();
                    List<Virus> viruses = player.getSession().sessionField().getViruses();
                    for (Virus virus : viruses) {
                        if (checkDistance(first, second, virus.getLocation(), virus.getRadius() / 2, cell.getRadius())) {
                            if (oldMass >= virus.getMass()) {
                                cell.setMass(oldMass / 2);
                                Cell newCell = new Cell(ServerCalculator.calculateLocationOnSplitting(cell));
                                newCell.setMass(oldMass / 2);
                                player.addCell(newCell);
                            }
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
            if (cell.getMass() >= GameConstraints.DEFAULT_PLAYER_CELL_MASS + GameConstraints.BLOB_MASS_CREATE) {
                Location mouseLocation = new Location(x, y);
                Blob blob = new Blob(mouseLocation, cell);
                cell.setMass(cell.getMass() - GameConstraints.BLOB_MASS_CREATE);
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
                    if (oldMass >= GameConstraints.DEFAULT_PLAYER_CELL_MASS * 2) {
                        elem.setMass(oldMass / 2);
                        Location newCellLocation = ServerCalculator.calculateLocationOnSplitting(elem);
                        Cell newCell = new Cell(newCellLocation);
                        newCell.setMass(oldMass / 2);
                        player.addCell(newCell);
                    }
                }
            }
        }
    }

    private void botMove() {
        for (GameSession gameSession : ApplicationContext.instance().get(MatchMaker.class).getActiveGameSessions()) {
            for (Blob blob : gameSession.sessionField().getBlobs()) {
                blob.makeMove();
            }
        }
    }

    private boolean checkDistance(Location first, Location second, Location foodCenter,
                                  float foodRadius, float cellRadius) {
        Vector vector = new Vector(second.getX() - first.getX(), second.getY() - first.getY());
        Vector normalVector = vector.makeNormal().normalize();
        Vector foodNormalVector = normalVector.extend(foodRadius);
        Vector cellNormalVector = normalVector.extend(cellRadius);

        Location edgeUp = foodNormalVector.getEnd(foodCenter);
        Location edgeDown = foodNormalVector.getStart(foodCenter);

        Location centerCellGone = cellNormalVector.intersectWith(vector, first, foodCenter);
        Location edgeUpCell = cellNormalVector.getEnd(centerCellGone);
        Location edgeDownCell = cellNormalVector.getStart(centerCellGone);

        double distanceOne = edgeDown.distanceTo(edgeUpCell);
        double distanceTwo = edgeUp.distanceTo(edgeDownCell);
        double length = 3.0f * cellNormalVector.length();
        return (distanceOne < length) && (distanceTwo < length);
    }

    private Vector buildMassCenter(Player player, float dx, float dy) {
        int massCommon = player.getMass();
        double xCenter = 0;
        double yCenter = 0;
        double radius = 0;
        for (Cell cell : player.getCells()) {
            xCenter = xCenter + cell.getX() * cell.getMass();
            yCenter = yCenter + cell.getY() * cell.getMass();
            radius = radius + cell.getRadius();
        }
        xCenter = xCenter / massCommon;
        yCenter = yCenter / massCommon;
        radius = radius / player.getCells().size();
        int mass = (int) massCommon / player.getCells().size();
        Location startCenterPoint = new Location(xCenter, yCenter);
        double newXCenter = Calculator.calculateDestinationOnTurn(xCenter, dx, radius, mass, GameConstraints.DEFAULT_PLAYER_CELL_MASS);
        double newYCenter = Calculator.calculateDestinationOnTurn(yCenter, dy, radius, mass, GameConstraints.DEFAULT_PLAYER_CELL_MASS);
        return Vector.createVector(startCenterPoint, new Location(newXCenter, newYCenter));
    }
}