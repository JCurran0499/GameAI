package games;

import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

import games.games2d.TicTacToe;
import java.util.List;

public class TicTacToeTest {
    private static TicTacToe game;

    private static final TicTacToe.Move[][] moves = new TicTacToe.Move[][]{
        new TicTacToe.Move[]{mv(0, 0), mv(0, 1), mv(0, 2)},
        new TicTacToe.Move[]{mv(1, 0), mv(1, 1), mv(1, 2)},
        new TicTacToe.Move[]{mv(2, 0), mv(2, 1), mv(2, 2)}
    };

    @Test
    public void gameInstantiation() {
        game = new TicTacToe();
        assertEquals("X", game.activePlayer());

        assertTrue(game.get(0, 0).isEmpty());
        assertTrue(game.get(1, 0).isEmpty());
        assertTrue(game.get(2, 1).isEmpty());
        assertTrue(game.get(1, 1).isEmpty());
    }

    @Test
    public void gameCopy() {
        game = new TicTacToe();
        game.move(moves[0][0]);
        TicTacToe game2 = game.copy();

        assertEquals(game.get(0, 0), game2.get(0, 0));
        assertEquals(game.get(1, 0), game2.get(1, 0));
        assertEquals(game.get(2, 1), game2.get(2, 1));
        assertEquals(game.get(1, 2), game2.get(1, 2));
        assertEquals(game.activePlayer(), game2.activePlayer());

        game.move(moves[2][1]);
        assertNotEquals(game.get(2, 1), game2.get(2, 1));
        assertNotEquals(game.activePlayer(), game2.activePlayer());
    }

    @Test
    public void gameNotOver() {
        game = new TicTacToe()
            .move(moves[1][1]).move(moves[0][1]).move(moves[0][0])
            .move(moves[0][2]).move(moves[2][0]).move(moves[2][2]);
        assertFalse(game.gameOver());

        game.move(moves[1][2]);
        assertFalse(game.gameOver());
    }

    @Test
    public void gameOverHorizontal() {
        game = new TicTacToe()
            .move(moves[0][0]).move(moves[1][0]).move(moves[0][2])
            .move(moves[1][1]).move(moves[0][1]);
        assertTrue(game.gameOver());
        assertEquals("X", game.winner());
    }

    @Test
    public void gameOverVertical() {
        game = new TicTacToe()
            .move(moves[1][2]).move(moves[1][1]).move(moves[2][2])
            .move(moves[2][0]).move(moves[0][2]);
        assertTrue(game.gameOver());
        assertEquals("X", game.winner());
    }

    @Test
    public void gameOverDiagonal() {
        game = new TicTacToe()
            .move(moves[0][1]).move(moves[0][2]).move(moves[0][0])
            .move(moves[2][0]).move(moves[2][2]).move(moves[1][1]);
        assertTrue(game.gameOver());
        assertEquals("O", game.winner());
    }

    @Test
    public void gameOverDraw() {
        game = new TicTacToe()
            .move(moves[1][1]).move(moves[1][0]).move(moves[0][0])
            .move(moves[2][2]).move(moves[2][1]).move(moves[0][1])
            .move(moves[0][2]).move(moves[2][0]).move(moves[1][2]);
        assertTrue(game.gameOver());
        assertNull(game.winner());
    }

    @Test
    public void gameActivePlayer() {
        game = new TicTacToe();
        assertEquals("X", game.activePlayer());

        game.move(moves[1][1]);
        assertEquals("O", game.activePlayer());

        game.move(moves[0][1]);
        assertEquals("X", game.activePlayer());

        game.move(moves[0][0]);
        assertEquals("O", game.activePlayer());
        assertEquals("O", game.activePlayer());
    }

    @Test
    public void gameMove() {
        game = new TicTacToe();
        for (int r = 0; r < 3; r++)
            for (int c = 0; c < 3; c++)
                assertEquals("", game.get(r, c));

        game.move(moves[0][0]);
        assertEquals("X", game.get(0, 0));

        game.move(moves[1][2]);
        assertEquals("X", game.get(0, 0));
        assertEquals("O", game.get(1, 2));
    }

    @Test
    public void gameMoveLegal() {
        game = new TicTacToe();
        for (TicTacToe.Move[] mvs : moves)
            for (TicTacToe.Move mv : mvs)
                assertTrue(game.moveLegal(mv));

        game.move(moves[0][0]);
        game.move(moves[0][1]);
        game.move(moves[0][2]);

        for (int r = 1; r < 3; r++)
            for (int c = 0; c < 3; c++)
                assertTrue(game.moveLegal(moves[r][c]));
    }

    @Test
    public void moveNotLegalBounds() {
        game = new TicTacToe();
        assertFalse(game.moveLegal(mv(0, 4)));
        assertFalse(game.moveLegal(mv(2, 3)));
        assertFalse(game.moveLegal(mv(0, -1)));
        assertFalse(game.moveLegal(mv(3, 1)));
        assertFalse(game.moveLegal(mv(-2, 1)));
        assertFalse(game.moveLegal(mv(3, -1)));
        assertFalse(game.moveLegal(null));
    }

    @Test
    public void moveNotLegalOccupied() {
        game = new TicTacToe();
        game.move(moves[1][1]);
        assertFalse(game.moveLegal(moves[1][1]));

        game.move(moves[0][1]);
        assertFalse(game.moveLegal(moves[1][1]));
        assertFalse(game.moveLegal(moves[0][1]));
    }

    @Test
    public void gameAllMoves() {
        game = new TicTacToe();
        List<TicTacToe.Move> allMoves = game.allMoves();
        assertEquals(9, allMoves.size());
        for (TicTacToe.Move[] mvs : moves)
            for (TicTacToe.Move mv : mvs)
                assertTrue(allMoves.contains(mv));
    }

    @Test
    public void allMovesSomeMoves() {
        game = new TicTacToe();
        game.move(moves[1][1]);
        game.move(moves[0][0]);
        game.move(moves[0][2]);

        List<TicTacToe.Move> allMoves = game.allMoves();
        assertEquals(6, allMoves.size());
        assertFalse(allMoves.contains(moves[1][1]));
        assertFalse(allMoves.contains(moves[0][0]));
        assertFalse(allMoves.contains(moves[0][2]));

        assertTrue(allMoves.contains(moves[0][1]));
        assertTrue(allMoves.contains(moves[1][2]));
    }

    @Test
    public void allMovesGameOver() {
        game = new TicTacToe();
        game.move(moves[0][0]);
        game.move(moves[0][1]);
        game.move(moves[0][2]);
        game.move(moves[1][0]);
        game.move(moves[1][1]);
        game.move(moves[1][2]);
        game.move(moves[2][1]);
        game.move(moves[2][0]);
        game.move(moves[2][2]);

        assertTrue(game.allMoves().isEmpty());
    }

    private static TicTacToe.Move mv(int r, int c) {
        return new TicTacToe.Move(r, c);
    }
}
