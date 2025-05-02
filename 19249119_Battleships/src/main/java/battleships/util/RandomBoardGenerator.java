package battleships.util;

import battleships.model.Coordinate;
import battleships.model.Ship;
import battleships.model.Orientation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

// Randomly generates a valid fleet configuration per assignment requirements
public class RandomBoardGenerator {
    private static final int[] SIZES = {5, 4, 3, 2, 2}; // Ship lengths to place
    private static final Random RAND = new Random();

    public static List<Ship> generate() {
        List<Ship> fleet = new ArrayList<>();
        boolean[][] occupied = new boolean[10][10]; // Tracks which cells are already occupied

        for (int size : SIZES) {
            boolean placed = false;
            while (!placed) {
                // Randomly choose orientation and starting point
                Orientation ori = RAND.nextBoolean()
                        ? Orientation.HORIZONTAL
                        : Orientation.VERTICAL;
                int row = RAND.nextInt(10);
                int col = RAND.nextInt(10);

                // Check if the ship can be legally placed
                List<Coordinate> cells = new ArrayList<>();
                boolean ok = true;
                for (int i = 0; i < size; i++) {
                    int r = row + (ori == Orientation.VERTICAL ? i : 0);
                    int c = col + (ori == Orientation.HORIZONTAL ? i : 0);
                    if (r > 9 || c > 9 || occupied[r][c]) {
                        ok = false;
                        break;
                    }
                    cells.add(new Coordinate(r, c));
                }
                if (!ok) continue;

                // Mark cells as occupied
                for (Coordinate coord : cells) {
                    occupied[coord.getRow()][coord.getCol()] = true;
                }

                fleet.add(new Ship(Collections.unmodifiableList(cells), ori));
                placed = true;
            }
        }

        return fleet;
    }
}
