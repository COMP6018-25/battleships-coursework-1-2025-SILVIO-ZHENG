package battleships.view.gui;

import battleships.model.CellState;
import battleships.model.Model;
import battleships.controller.Controller;
import battleships.observer.Observer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;

// Swing GUI view: draws the board, captures user clicks, shows messages,
// includes a "New Game" button, and displays a game-over dialog
public class GuiView extends JFrame implements Observer {
    private final Model model;
    private final Controller controller;
    private BoardPanel boardPanel;
    private JLabel statusBar;

    // Constructor: stores references to Model and Controller and sets up window properties
    public GuiView(Model model, Controller controller) {
        this.model = model;
        this.controller = controller;
        setTitle("Battleships");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
    }

    // Builds and displays all GUI components: board panel, status bar, and new game button
    public void initComponents() {
        // Board panel
        boardPanel = new BoardPanel(model.snapshot());
        boardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int cs = boardPanel.getCellSize();
                int row = e.getY() / cs;
                int col = e.getX() / cs;
                controller.handleClick(row, col);
            }
        });

        // Status bar
        statusBar = new JLabel("Welcome!");

        // New Game button (always starts a random game)
        JButton newGameBtn = new JButton("New Game");
        newGameBtn.addActionListener(e -> controller.newGame(null));

        // Bottom panel: holds status bar and button
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(statusBar, BorderLayout.CENTER);
        bottom.add(newGameBtn, BorderLayout.EAST);

        // Add components to main window
        add(boardPanel, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Observer callback: updates board view when model changes
    @Override
    public void update(PropertyChangeEvent evt) {
        CellState[][] snap = (CellState[][]) evt.getNewValue();
        boardPanel.updateBoard(snap);
        pack();
    }

    // Displays a message in the status bar
    public void showMessage(String msg) {
        statusBar.setText(msg);
    }

    // Displays a dialog box when the game ends, showing total shots taken
    public void showGameOverDialog(int tries) {
        JOptionPane.showMessageDialog(
                this,
                "Game Over!\nYou sank all ships in " + tries + " tries.",
                "Game Over",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
