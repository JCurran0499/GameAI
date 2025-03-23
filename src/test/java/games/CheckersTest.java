package games;

import games.games2d.Checkers;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CheckersTest {
    private static Checkers game;

    @Test
    public void gameInstantiation() {
        game = new Checkers();
        assertEquals("B", game.activePlayer());
    }
}
