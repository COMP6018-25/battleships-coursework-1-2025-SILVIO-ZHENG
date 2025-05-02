package battleships.model;

import java.util.Collections;
import java.util.List;

// Represents a battleship with its occupied coordinates and placement orientation
public class Ship {
    // Unmodifiable list of occupied coordinates
    private final List<Coordinate> cells;

    // Orientation of the ship: HORIZONTAL or VERTICAL
    private final Orientation orientation;

    // Constructs a Ship instance
    // Throws an exception if the list of cells is null or empty
    public Ship(List<Coordinate> cells, Orientation orientation) {
        if (cells == null || cells.isEmpty()) {
            throw new IllegalArgumentException("Cells list cannot be null or empty");
        }
        this.cells = Collections.unmodifiableList(cells);
        this.orientation = orientation;
    }

    // Returns true if every coordinate of this ship has been hit
    public boolean isSunk() {
        return cells.stream().allMatch(Coordinate::isHit);
    }

    // Returns an unmodifiable list of this ship's coordinates
    public List<Coordinate> getCells() {
        return cells;
    }

    // Returns the ship's orientation
    public Orientation getOrientation() {
        return orientation;
    }
}
