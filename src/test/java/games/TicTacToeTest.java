package games;

import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

import games.tictactoe.TicTacToe;

public class TicTacToeTest {
    private TicTacToe game;

    @Test
    public void squareCopy() {
        TicTacToe.Square sq1 = new TicTacToe.Square(1, 2);
        TicTacToe.Square sq2 = sq1.copy();

        sq1.setAgent("X");
        assertNotEquals(sq1.getAgent(), sq2.getAgent());
        assertNull(sq2.getAgent());
    }

    @Test
    public void gameInstantiation() {
        game = new TicTacToe("X", false);
        assertEquals(game.activeAgent(), "O");

        assertTrue(game.get(0, 0).free());
        assertTrue(game.get(1, 0).free());
        assertTrue(game.get(2, 1).free());
        assertTrue(game.get(1, 1).free());
    }


}
