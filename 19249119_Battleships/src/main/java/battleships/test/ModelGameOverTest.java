package battleships.test;

import battleships.model.Model;
import battleships.util.InvalidConfigException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

// Scenario 2: After all ships are sunk, isGameOver() should return true and getTries() should be correct
public class ModelGameOverTest {
    private Path configFile;

    @BeforeEach
    void setUp() throws IOException {
        // Create a temporary file containing two ships:
        // - One horizontal ship at A1 of length 2
        // - One vertical ship at C1 of length 2
        String content =
                "2 A1 H\n" +
                        "2 C1 V\n";
        configFile = Files.createTempFile("bs_config", ".txt");
        Files.writeString(configFile, content);
    }

    @Test
    void testIsGameOverAfterAllSunk() throws IOException, InvalidConfigException {
        Model model = new Model(configFile);

        // Sink the first ship (A1, A2)
        model.fireAt(0, 0);
        model.fireAt(0, 1);

        // Sink the second ship (C1, D1)
        model.fireAt(2, 0);
        model.fireAt(3, 0);

        // After all ships are sunk, game should be over
        assertTrue(model.isGameOver());

        // Total number of shots should be 4
        assertEquals(4, model.getTries());
    }
}
