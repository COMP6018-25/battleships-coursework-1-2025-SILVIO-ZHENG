package battleships.controller;

import battleships.model.HitResult;
import battleships.model.Model;
import battleships.model.Ship;
import battleships.view.gui.GuiView;
import battleships.util.InvalidConfigException;

import javax.swing.SwingUtilities;
import java.io.IOException;
import java.nio.file.Path;

//
//  Controller class: handles user clicks, tracks sunk ships,
//  manages new game logic, and triggers a dialog when the game ends.
//
public class Controller {
    private final Model model;
    private final GuiView view;

    // Track number of sunk ships to detect when a new ship is sunk
    private int sunkCount = 0;

    public Controller(Model model) {
        this.model = model;
        this.view = new GuiView(model, this);   // Instantiate the GUI view and pass in references
        model.addObserver(view);                // Register the view as an observer of the model
        view.initComponents();                  // Initialize the GUI components
        // Initialize sunkCount (should be zero at game start)
        sunkCount = (int) model.getShips().stream().filter(Ship::isSunk).count();
    }

//     Handles a user click on the GUI board:
//     Calls model.fireAt() to determine result: MISS, HIT, or SUNK
//     Displays appropriate message for each result
//     If all ships are sunk, shows final message and a game-over dialog
//     @param row Index of the row (0–9)
//     @param col Index of the column (0–9)
//
    public void handleClick(int row, int col) {
        HitResult result = model.fireAt(row, col);   // Attempt to fire at the specified cell

        switch (result) {
            case MISS:
                view.showMessage("Miss!");
                break;
            case HIT:
                view.showMessage("Hit!");
                break;
            case SUNK:
                // A ship has just been sunk
                int nowSunk = (int) model.getShips().stream().filter(Ship::isSunk).count();
                int remaining = model.getShips().size() - nowSunk;
                view.showMessage("You sunk a ship! " + remaining + " remaining.");
                sunkCount = nowSunk;
                break;
        }

        // If all ships have been sunk, trigger game-over sequence
        if (model.isGameOver()) {
            int tries = model.getTries();
            view.showMessage("Game over! Tries: " + tries);
            // GUI dialogs must be triggered on the Event Dispatch Thread (EDT)
            SwingUtilities.invokeLater(() -> view.showGameOverDialog(tries));
        }
    }

//      Starts a new game: if the provided file is null, generates a random setup;
//      otherwise, loads board configuration from the given file.
//      @param file Path to the board configuration file, or null for random game

    public void newGame(Path file) {
        try {
            if (file != null) {
                model.loadBoard(file);                  // Load board from file
                view.showMessage("Loaded from file.");
            } else {
                model.reloadRandom();                   // Create a new random board
                view.showMessage("New random game started.");
            }
            // Reset the sunk ship counter
            sunkCount = 0;
        } catch (InvalidConfigException | IOException e) {
            view.showMessage("Failed to load: " + e.getMessage());   // Display error if loading fails
        }
    }
}
