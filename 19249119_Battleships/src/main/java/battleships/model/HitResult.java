package battleships.model;

// Represents the result of a fireAt() call: MISS, HIT, or SUNK
public enum HitResult {
    MISS,   // The shot missed all ships
    HIT,    // The shot hit a ship but did not sink it
    SUNK    // The shot sank a ship
}
