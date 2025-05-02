package battleships.view.gui;

import battleships.model.CellState;

import javax.swing.*;
import java.awt.*;

// Custom board panel: draws a 10×10 grid and displays HIT/MISS
public class BoardPanel extends JPanel {
    private CellState[][] board;      // Snapshot of the current board state
    private final int cellSize = 30;  // Size of each cell in pixels

    // Constructor: accepts the initial board snapshot
    public BoardPanel(CellState[][] initial) {
        this.board = initial;
        int size = cellSize * 10;
        setPreferredSize(new Dimension(size, size));
    }

    // Updates the board with a new snapshot and repaints the panel
    public void updateBoard(CellState[][] newBoard) {
        this.board = newBoard;
        repaint();
    }

    // Returns the size of each cell, used for mouse event handling
    public int getCellSize() {
        return cellSize;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw a 10×10 grid
        for (int r = 0; r < 10; r++) {
            for (int c = 0; c < 10; c++) {
                CellState s = board[r][c];

                // Display: WHITE for unknown (EMPTY/SHIP), Yellow for HIT, GRAY for MISS
                if (s == CellState.HIT) {
                    g.setColor(Color.yellow);
                } else if (s == CellState.MISS) {
                    g.setColor(Color.LIGHT_GRAY);
                } else {
                    g.setColor(Color.WHITE);
                }

                g.fillRect(c * cellSize, r * cellSize, cellSize, cellSize);
                g.setColor(Color.BLACK);
                g.drawRect(c * cellSize, r * cellSize, cellSize, cellSize);
            }
        }
    }
}
