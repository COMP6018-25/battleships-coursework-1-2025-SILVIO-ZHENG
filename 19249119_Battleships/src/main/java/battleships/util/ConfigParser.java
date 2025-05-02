package battleships.util;

import battleships.model.Coordinate;
import battleships.model.Ship;
import battleships.model.Orientation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

// Parses a user-defined ship configuration file.
//
// File format example (one per line): <length> <coordinate> <orientation>
// e.g., "5 A1 H" means a ship of length 5 placed horizontally starting from A1.
public class ConfigParser {
    public static List<Ship> parse(Path file) throws InvalidConfigException, IOException {
        List<Ship> ships = new ArrayList<>();
        List<String> lines = Files.readAllLines(file);

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("#")) continue;

            String[] parts = line.split("\\s+");
            if (parts.length != 3)
                throw new InvalidConfigException("Invalid format: " + line);

            int length;
            try {
                length = Integer.parseInt(parts[0]);
            } catch (NumberFormatException e) {
                throw new InvalidConfigException("Invalid ship length: " + parts[0]);
            }

            // Parse coordinate, e.g. A1 → row=0, col=0
            String coord = parts[1];
            char rowC = coord.charAt(0);
            int row = rowC - 'A';

            int col;
            try {
                col = Integer.parseInt(coord.substring(1)) - 1;
            } catch (NumberFormatException e) {
                throw new InvalidConfigException("Invalid coordinate: " + coord);
            }

            if (row < 0 || row > 9 || col < 0 || col > 9)
                throw new InvalidConfigException("Coordinate out of range: " + coord);

            // Parse orientation: H or V
            Orientation orientation;
            if (parts[2].equalsIgnoreCase("H")) {
                orientation = Orientation.HORIZONTAL;
            } else if (parts[2].equalsIgnoreCase("V")) {
                orientation = Orientation.VERTICAL;
            } else {
                throw new InvalidConfigException("Invalid orientation: " + parts[2]);
            }

            // Generate list of coordinates for the ship
            List<Coordinate> cells = new ArrayList<>();
            for (int i = 0; i < length; i++) {
                int r = row + (orientation == Orientation.VERTICAL ? i : 0);
                int c = col + (orientation == Orientation.HORIZONTAL ? i : 0);
                if (r > 9 || c > 9)
                    throw new InvalidConfigException("Ship out of bounds: " + line);
                cells.add(new Coordinate(r, c));
            }

            ships.add(new Ship(cells, orientation));
        }

        return ships;
    }
}
