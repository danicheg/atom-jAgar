package utils.generators;

import model.Blob;
import model.Cell;
import model.Food;
import model.GameSession;
import model.Player;
import model.Virus;
import org.eclipse.jetty.util.ConcurrentHashSet;

import java.util.List;

public class ModelToProtocolGenerator {

    public static protocol.model.Blob[] generateProtocolBlobsFromModel(ConcurrentHashSet<Blob> blobsIn, double borderTop, double borderBottom, double borderLeft, double borderRight) {
        ConcurrentHashSet<Blob> blobs = new ConcurrentHashSet<>();
        for (Blob blob : blobsIn) {
            if (blob.getX() > borderLeft && blob.getX() < borderRight && blob.getY() > borderBottom && blob.getY() < borderTop) {
                blobs.add(blob);
            }
        }
        protocol.model.Blob[] blobsOut = new protocol.model.Blob[blobs.size()];
        int inc = 0;
        for (model.Blob blobGot : blobs) {
            blobsOut[inc] = new protocol.model.Blob(blobGot.getId(),
                    blobGot.getMass(),
                    blobGot.getLocation().getX(),
                    blobGot.getLocation().getY());
            inc++;
        }
        return blobsOut;
    }

    public static protocol.model.Cell[] generateProtocolCellsFromModel(GameSession gameSession, double borderTop, double borderBottom, double borderLeft, double borderRight) {
        ConcurrentHashSet<Cell> cells = new ConcurrentHashSet<>();
        for (Player player : gameSession.sessionPlayersList()) {
            for (Cell cell : player.getCells()) {
                if (cell.getX() > borderLeft && cell.getX() < borderRight && cell.getY() > borderBottom && cell.getY() < borderTop) {
                    cells.add(cell);
                }
            }
        }
        protocol.model.Cell[] cellsOut = new protocol.model.Cell[cells.size()];
        int i = 0;
        for (Cell playerCell : cells) {
            cellsOut[i] = new protocol.model.Cell(
                    playerCell.getId(),
                    playerCell.getOwner().getId(),
                    playerCell.getName(),
                    playerCell.getMass(),
                    playerCell.getX(),
                    playerCell.getY(),
                    playerCell.getOwner().getColor().getRed(),
                    playerCell.getOwner().getColor().getGreen(),
                    playerCell.getOwner().getColor().getBlue()
            );
            i++;
        }
        return cellsOut;
    }

    public static protocol.model.Virus[] generateProtocolVirusesFromModel(List<Virus> virusesIn, double borderTop, double borderBottom, double borderLeft, double borderRight) {
        ConcurrentHashSet<Virus> viruses = new ConcurrentHashSet<>();
        for (Virus virus : virusesIn) {
            if (virus.getX() > borderLeft && virus.getX() < borderRight && virus.getY() > borderBottom && virus.getY() < borderTop) {
                viruses.add(virus);
            }
        }
        protocol.model.Virus[] virusesOut = new protocol.model.Virus[viruses.size()];
        int k = 0;
        for (Virus virusGot : viruses) {
            virusesOut[k] = new protocol.model.Virus(virusGot.getId(),
                    virusGot.getMass(),
                    virusGot.getLocation().getX(),
                    virusGot.getLocation().getY());
            k++;
        }
        return virusesOut;
    }

    public static protocol.model.Food[] generateProtocolFoodFromModel(ConcurrentHashSet<Food> foodIn, double borderTop, double borderBottom, double borderLeft, double borderRight) {
        ConcurrentHashSet<Food> foods = new ConcurrentHashSet<>();
        for (Food food : foodIn) {
            if (food.getX() > borderLeft && food.getX() < borderRight && food.getY() > borderBottom && food.getY() < borderTop) {
                foods.add(food);
            }
        }
        protocol.model.Food[] foodOut = new protocol.model.Food[foods.size()];
        int counter = 0;
        for (Food foodGot : foods) {
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

}
