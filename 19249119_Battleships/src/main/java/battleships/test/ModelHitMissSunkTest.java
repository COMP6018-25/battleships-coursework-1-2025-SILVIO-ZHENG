package battleships.test;

import battleships.model.CellState;
import battleships.model.HitResult;
import battleships.model.Model;
import battleships.util.InvalidConfigException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

// Scenario 1: Test HIT / MISS / SUNK behavior
public class ModelHitMissSunkTest {
    private Path configFile;

    @BeforeEach
    void setUp() throws IOException {
        // Create a temp file with two ships:
        // - One horizontal ship of length 2 at A1
        // - One vertical ship of length 2 at C1
        String content =
                "2 A1 H\n" +
                        "2 C1 V\n";
        configFile = Files.createTempFile("bs_config", ".txt");
        Files.writeString(configFile, content);
    }

    @Test
    void testHitMissSunk() throws IOException, InvalidConfigException {
        Model model = new Model(configFile);

        // Fire at A1 → HIT
        HitResult r1 = model.fireAt(0, 0);
        assertEquals(HitResult.HIT, r1);
        assertEquals(CellState.HIT, model.snapshot()[0][0]);

        // Fire at A2 → SUNK
        HitResult r2 = model.fireAt(0, 1);
        assertEquals(HitResult.SUNK, r2);
        assertEquals(CellState.HIT, model.snapshot()[0][1]);

        // Fire again at A2 → MISS (already hit)
        HitResult r3 = model.fireAt(0, 1);
        assertEquals(HitResult.MISS, r3);
    }
}
