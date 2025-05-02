package battleships.model;

// Represents a single cell on the board, identified by its row and column indices.
// It also tracks whether the cell has been hit.
public class Coordinate {
    private final int row;   // Row index (0–9)
    private final int col;   // Column index (0–9)
    private boolean hit;     // Whether this cell has been hit

    // Constructs a Coordinate object with the specified row and column.
    // Throws an exception if the input is out of bounds (0–9).
    public Coordinate(int row, int col) {
        if (row < 0 || row > 9 || col < 0 || col > 9) {
            throw new IllegalArgumentException("Row and col must be between 0 and 9");
        }
        this.row = row;
        this.col = col;
        this.hit = false;
    }

    // Marks this coordinate as hit
    public void markHit() {
        this.hit = true;
    }

    // Returns whether this coordinate has been hit
    public boolean isHit() {
        return hit;
    }

    // Returns the row index
    public int getRow() {
        return row;
    }

    // Returns the column index
    public int getCol() {
        return col;
    }

    // Checks whether two coordinates are equal based on row and column
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Coordinate)) return false;
        Coordinate o = (Coordinate) obj;
        return row == o.row && col == o.col;
    }

    // Generates a hash code based on row and column
    @Override
    public int hashCode() {
        return 31 * row + col;
    }

    // Returns string representation, e.g., (3,5)[H] if hit
    @Override
    public String toString() {
        return "(" + row + "," + col + ")" + (hit ? "[H]" : "");
    }
}
