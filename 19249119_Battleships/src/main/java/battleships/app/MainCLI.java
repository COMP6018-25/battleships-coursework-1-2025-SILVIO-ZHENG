package battleships.app;

import battleships.model.CellState;
import battleships.model.HitResult;
import battleships.model.Model;
import battleships.model.Ship;

import java.util.List;
import java.util.Scanner;

// CLI entry point: reuses Model to fulfill FR1 and FR3 without using Controller/View.
public class MainCLI {
    public static void main(String[] args) {
        Model model = new Model();                // Initialize the model with a randomly arranged fleet
        Scanner scanner = new Scanner(System.in);

        // Track the number of ships sunk to detect when a new ship has been sunk
        int sunkCount = 0;

        // Main game loop
        while (!model.isGameOver()) {
            printBoard(model.snapshot());         // Display current board state
            System.out.print("Enter target (A1–J10): ");
            String input = scanner.nextLine().trim().toUpperCase();

            // Validate input format using regular expression
            if (input.matches("[A-J](10|[1-9])")) {
                int row = input.charAt(0) - 'A';                       // Convert row letter to index (A=0)
                int col = Integer.parseInt(input.substring(1)) - 1;   // Convert column number to index (1-based to 0-based)

                HitResult res = model.fireAt(row, col);               // Fire at the specified cell
                switch (res) {
                    case MISS:
                        System.out.println("Miss!");
                        break;
                    case HIT:
                        System.out.println("Hit!");
                        break;
                    case SUNK:
                        // If a ship is sunk, count how many ships are currently sunk
                        List<Ship> fleet = model.getShips();
                        int nowSunk = (int) fleet.stream().filter(Ship::isSunk).count();
                        int remaining = fleet.size() - nowSunk;
                        System.out.println("You sunk a ship! " + remaining + " remaining.");
                        sunkCount = nowSunk;
                        break;
                }
            } else {
                System.out.println("Invalid input. Use A1–J10.");
            }
        }

        // Game is over, show final board and number of attempts
        printBoard(model.snapshot());
        System.out.println("Game over! You sank all ships in "
                + model.getTries() + " shots.");
        scanner.close();
    }


//      Render the game board to the console using ASCII symbols.
//      '.' indicates an unshot cell, 'H' indicates a hit, and 'M' indicates a miss.

    private static void printBoard(CellState[][] board) {
        // Print column headers (1 to 10)
        System.out.print("   ");
        for (int c = 1; c <= 10; c++) {
            System.out.printf("%2d ", c);
        }
        System.out.println();

        // Print each row with corresponding row label (A to J)
        for (int r = 0; r < 10; r++) {
            char rowLabel = (char) ('A' + r);
            System.out.print(rowLabel + "  ");
            for (int c = 0; c < 10; c++) {
                CellState s = board[r][c];
                char ch = '.';
                if (s == CellState.HIT)   ch = 'H';
                else if (s == CellState.MISS) ch = 'M';
                System.out.print(" " + ch + " ");
            }
            System.out.println();
        }
    }
}
