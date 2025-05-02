package battleships.test;

import battleships.model.CellState;
import battleships.model.Model;
import battleships.util.InvalidConfigException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

// Scenario 3: loadBoard() should reset the board and the try count
public class ModelLoadBoardResetTest {

    @Test
    void testLoadBoardResetsBoardAndTries() throws IOException, InvalidConfigException {
        // Start with a random board and take two shots
        Model model = new Model();
        model.fireAt(5, 5);
        model.fireAt(6, 6);
        assertTrue(model.getTries() >= 2);

        // Create a simple config file with two ships
        String content = "2 A1 H\n2 C1 V\n";
        Path configFile = Files.createTempFile("bs_config", ".txt");
        Files.writeString(configFile, content);

        // Load the board from the config file
        model.loadBoard(configFile);

        // Try count should be reset to 0
        assertEquals(0, model.getTries());

        // A1 should now contain a ship
        assertEquals(CellState.SHIP, model.snapshot()[0][0]);
    }
}
