package games;

import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

import games.games2d.TicTacToe;

public class TicTacToeTest {
    private static TicTacToe game;

    @Test
    public void squareCopy() {
        TicTacToe.Square sq1 = new TicTacToe.Square(1, 2);
        TicTacToe.Square sq2 = sq1.copy();

        sq1.setAgent("X");
        assertNotEquals(sq1.getAgent(), sq2.getAgent());
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

    @Test
    public void gameCopy() {
        game = new TicTacToe("X", true);
        TicTacToe game2 = game.copy();

        assertEquals(game.get(0, 0).getAgent(), game2.get(0, 0).getAgent());
        assertEquals(game.get(1, 0).getAgent(), game2.get(1, 0).getAgent());
        assertEquals(game.get(2, 1).getAgent(), game2.get(2, 1).getAgent());
        assertEquals(game.get(1, 2).getAgent(), game2.get(1, 2).getAgent());
        assertEquals(game.activeAgent(), game2.activeAgent());

        game = game.move(new TicTacToe.Move(2, 1));
        assertNotEquals(game.get(2, 1).getAgent(), game2.get(2, 1).getAgent());
        assertNotEquals(game.activeAgent(), game2.activeAgent());
    }

    @Test
    public void gameNotOver() {
        game = new TicTacToe("X", true)
            .move(mv(1, 1)).move(mv(0, 1)).move(mv(0, 0))
            .move(mv(0, 2)).move(mv(2, 0)).move(mv(2, 2));
        assertFalse(game.gameOver());
    }

    @Test
    public void gameOverHorizontal() {
        game = new TicTacToe("X", true)
            .move(mv(0, 0)).move(mv(1, 0)).move(mv(0, 2))
            .move(mv(1, 1)).move(mv(0, 1));
        assertTrue(game.gameOver());
        assertEquals(game.winner(), "X");
    }

    @Test
    public void gameOverVertical() {
        game = new TicTacToe("O", true)
            .move(mv(1, 2)).move(mv(1, 1)).move(mv(2, 2))
            .move(mv(2, 0)).move(mv(0, 2));
        assertTrue(game.gameOver());
        assertEquals(game.winner(), "O");
    }

    @Test
    public void gameOverDiagonal() {
        game = new TicTacToe("X", false)
            .move(mv(0, 1)).move(mv(1, 1)).move(mv(0, 2))
            .move(mv(0, 0)).move(mv(1, 0)).move(mv(2, 2));
        assertTrue(game.gameOver());
        assertEquals(game.winner(), "X");
    }

    @Test
    public void gameOverDraw() {
        game = new TicTacToe("X", true)
            .move(mv(1, 1)).move(mv(1, 0)).move(mv(0, 0))
            .move(mv(2, 2)).move(mv(2, 1)).move(mv(0, 1))
            .move(mv(0, 2)).move(mv(2, 0)).move(mv(1, 2));
        assertTrue(game.gameOver());
        assertNull(game.winner());
    }


    private TicTacToe.Move mv(int r, int c) {
        return new TicTacToe.Move(r, c);
    }
}
