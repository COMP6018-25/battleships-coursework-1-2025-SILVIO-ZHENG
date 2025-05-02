package battleships.model;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.beans.PropertyChangeEvent;

import battleships.observer.Observable;
import battleships.observer.Observer;
import battleships.util.ConfigParser;
import battleships.util.RandomBoardGenerator;
import battleships.util.InvalidConfigException;

// Core model for the Battleships game.
// Uses a custom Observable / Observer pair (simpler than java.util.Observable).
public class Model implements Observable {

    // 10×10 game board – each cell holds its current state
    private final CellState[][] board = new CellState[10][10];

    // The fleet; ships must never overlap
    private final List<Ship> ships = new ArrayList<>();

    // Number of valid shots taken by the player
    private int tries = 0;

    // List of registered observers (GUI, CLI view, etc.)
    private final List<Observer> observers = new ArrayList<>();

    // -------- Constructors --------------------------------------------------

    // Creates a new game with a random fleet
    public Model() {
        ships.addAll(RandomBoardGenerator.generate());
        assert validateShips(ships) : "Random generator produced an invalid fleet";
        setupBoard();
        notifyObservers(new PropertyChangeEvent(this, "board", null, snapshot()));
    }

    // Creates a new game by loading a configuration file
    public Model(Path file) throws InvalidConfigException, IOException {
        ships.addAll(ConfigParser.parse(file));
        assert validateShips(ships) : "Configuration file produced an invalid fleet";
        setupBoard();
        notifyObservers(new PropertyChangeEvent(this, "board", null, snapshot()));
    }

    // -------- Public API ----------------------------------------------------

    // Fires at (row, col); returns MISS, HIT, or SUNK
    public HitResult fireAt(int row, int col) {
        // Pre-condition: coordinates must be on the board
        assert row >= 0 && row < 10 && col >= 0 && col < 10 : "row/col out of bounds";

        CellState prev = board[row][col];
        if (prev == CellState.HIT || prev == CellState.MISS) {
            return HitResult.MISS;              // duplicate shot
        }

        boolean wasShip = (prev == CellState.SHIP);
        board[row][col] = wasShip ? CellState.HIT : CellState.MISS;
        HitResult result = wasShip ? HitResult.HIT : HitResult.MISS;

        if (wasShip) {
            // Find the ship and update its hit status
            for (Ship s : ships) {
                for (Coordinate c : s.getCells()) {
                    if (c.getRow() == row && c.getCol() == col) {
                        c.markHit();
                        if (s.isSunk()) {
                            result = HitResult.SUNK;
                        }
                        break;
                    }
                }
            }
        }

        tries++;

        // Post-conditions
        assert tries > 0 : "tries should have been incremented";
        assert board[row][col] == (wasShip ? CellState.HIT : CellState.MISS)
                : "cell state did not update correctly";

        notifyObservers(new PropertyChangeEvent(this, "board", null, snapshot()));
        return result;
    }

    // Returns true when all ships are sunk
    public boolean isGameOver() {
        return ships.stream().allMatch(Ship::isSunk);
    }

    public int getTries() {
        return tries;
    }

    public List<Ship> getShips() {
        return ships;
    }

    // Starts a new random game
    public void reloadRandom() {
        ships.clear();
        ships.addAll(RandomBoardGenerator.generate());
        assert validateShips(ships) : "Random generator produced an invalid fleet";
        setupBoard();
        notifyObservers(new PropertyChangeEvent(this, "board", null, snapshot()));
    }

    // Loads a new game from file
    public void loadBoard(Path file) throws InvalidConfigException, IOException {
        List<Ship> newShips = ConfigParser.parse(file);
        assert validateShips(newShips) : "Configuration file produced an invalid fleet";
        ships.clear();
        ships.addAll(newShips);
        setupBoard();
        notifyObservers(new PropertyChangeEvent(this, "board", null, snapshot()));
    }

    // Returns a deep copy of the board (defensive copy for the view layer)
    public CellState[][] snapshot() {
        CellState[][] copy = new CellState[10][10];
        for (int r = 0; r < 10; r++) {
            System.arraycopy(board[r], 0, copy[r], 0, 10);
        }
        return copy;
    }

    // -------- Observable implementation ------------------------------------

    @Override
    public void addObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(PropertyChangeEvent evt) {
        // Copy to avoid ConcurrentModificationException
        for (Observer o : new ArrayList<>(observers)) {
            o.update(evt);
        }
    }

    // -------- Private helper methods ---------------------------------------

    // Places ships onto the internal board and resets tries
    private void setupBoard() {
        for (int r = 0; r < 10; r++) {
            for (int c = 0; c < 10; c++) {
                board[r][c] = CellState.EMPTY;
            }
        }
        for (Ship s : ships) {
            for (Coordinate coord : s.getCells()) {
                board[coord.getRow()][coord.getCol()] = CellState.SHIP;
            }
        }
        tries = 0;
    }

    // Validates fleet: within bounds, no overlaps
    private boolean validateShips(List<Ship> fleet) {
        boolean[][] occupied = new boolean[10][10];
        for (Ship s : fleet) {
            for (Coordinate c : s.getCells()) {
                int r = c.getRow(), col = c.getCol();
                if (r < 0 || r >= 10 || col < 0 || col >= 10) return false;
                if (occupied[r][col]) return false;   // overlap detected
                occupied[r][col] = true;
            }
        }
        return true;
    }
}
