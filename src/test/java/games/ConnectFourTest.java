package games;

import games.games2d.ConnectFour;
import org.junit.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ConnectFourTest {
    private static ConnectFour game;

    @Test
    public void gameInstantiation() {
        game = new ConnectFour("R");
        assertEquals("R", game.activePlayer());
        assertEquals("R", game.player1);
        assertEquals("B", game.player2);

        for (int r = 0; r < 6; r++)
            for (int c = 0; c < 7; c++)
                assertTrue(game.get(r, c).isEmpty());
    }

    @Test
    public void gameInstantiationOtherPlayer() {
        game = new ConnectFour("B");
        assertEquals("B", game.activePlayer());
        assertEquals("B", game.player1);
        assertEquals("R", game.player2);
    }

    @Test
    public void invalidGameInstantiation() {
        try {
            game = new ConnectFour("r");
        } catch (RuntimeException e) {
            assertEquals("invalid player configuration", e.getMessage());
        }

        try {
            game = new ConnectFour("X");
        } catch (RuntimeException e) {
            assertEquals("invalid player configuration", e.getMessage());
        }

        try {
            game = new ConnectFour("invalid");
        } catch (RuntimeException e) {
            assertEquals("invalid player configuration", e.getMessage());
        }
    }

    @Test
    public void nullInstantiation() {
        try {
            game = new ConnectFour(null);
        } catch (RuntimeException e) {
            assertEquals("invalid player configuration", e.getMessage());
        }
    }

    @Test
    public void gameCopy() {
        game = new ConnectFour("R");
        game.move(3);
        game.move(4);
        game.move(4);
        ConnectFour game2 = game.copy();

        assertEquals(game.get(5, 3), game2.get(5, 3));
        assertEquals(game.get(5, 4), game2.get(5, 4));
        assertEquals(game.get(4, 4), game2.get(4, 4));
        assertEquals(game.get(5, 0), game2.get(5, 0));
        assertEquals(game.activePlayer(), game2.activePlayer());

        game.move(1);
        game.move(2);
        game.move(4);
        assertNotEquals(game.get(5, 2), game2.get(5, 2));
        assertNotEquals(game.get(3, 4), game2.get(3, 4));
        assertEquals("B", game.get(3, 4));
        assertNotEquals(game.activePlayer(), game2.activePlayer());

        game2.move(1);
        assertEquals(game.get(5, 1), game2.get(5, 1));
    }


    @Test
    public void gameNotOver() {
        game = new ConnectFour("R");
        assertFalse(game.gameOver());

        game.move(0).move(3).move(0).move(6).move(5).move(4).move(0);
        assertFalse(game.gameOver());

        game.move(3).move(2).move(4);
        assertFalse(game.gameOver());

        game.move(4).move(0);
        assertFalse(game.gameOver());
    }

    @Test
    public void gameOverHorizontal() {
        game = new ConnectFour("R")
            .move(3).move(6).move(1).move(0).move(0)
            .move(6).move(4).move(3).move(2);

        assertTrue(game.gameOver());
        assertEquals("R", game.winner());
    }

    @Test
    public void gameOverUpperHorizontal() {
        game = new ConnectFour("R")
            .move(2).move(3).move(4).move(2).move(0)
            .move(4).move(1).move(3).move(5).move(5);

        assertTrue(game.gameOver());
        assertEquals("B", game.winner());
    }

    @Test
    public void gameOverVertical() {
        game = new ConnectFour("B")
            .move(0).move(1).move(2).move(2).move(4)
            .move(2).move(0).move(2).move(0).move(2);

        assertTrue(game.gameOver());
        assertEquals("R", game.winner());
    }

    @Test
    public void gameOverVerticalRight() {
        game = new ConnectFour("B")
            .move(6).move(5).move(6).move(5).move(6)
            .move(5).move(6);

        assertTrue(game.gameOver());
        assertEquals("B", game.winner());
    }

    @Test
    public void gameOverDiagonalLeftRight() {
        game = new ConnectFour("R");
        assertFalse(game.gameOver());

        game.move(0).move(1).move(1).move(2).move(3)
            .move(0).move(2).move(4).move(2).move(0)
            .move(4).move(3).move(4).move(3).move(3);

        assertTrue(game.gameOver());
        assertEquals("R", game.winner());
    }

    @Test
    public void gameOverDiagonalRightLeft() {
        game = new ConnectFour("B")
            .move(0).move(1).move(0).move(0).move(0)
            .move(0).move(1).move(3).move(3).move(0)
            .move(2).move(2).move(2).move(2).move(1)
            .move(3).move(1).move(1);

        assertTrue(game.gameOver());
        assertEquals("R", game.winner());
    }

    @Test
    public void gameOverDraw() {
        game = new ConnectFour("R");
        game.move(0).move(1).move(2).move(3).move(4).move(5).move(6);
        assertFalse(game.gameOver());

        game.move(0).move(1).move(2).move(3).move(4).move(5).move(6);
        assertFalse(game.gameOver());

        game.move(1).move(2).move(3).move(4).move(5).move(6).move(0);
        assertFalse(game.gameOver());

        game.move(0).move(1).move(2).move(3).move(4).move(5).move(6);
        assertFalse(game.gameOver());

        game.move(0).move(1).move(2).move(3).move(4).move(5).move(6);
        assertFalse(game.gameOver());

        game.move(1).move(2).move(3).move(4).move(5).move(6).move(0);
        assertTrue(game.gameOver());
        assertNull(game.winner());
    }

    @Test
    public void gameActivePlayer() {
        game = new ConnectFour("R");
        assertEquals("R", game.activePlayer());

        game.move(1);
        assertEquals("B", game.activePlayer());

        game.move(1);
        assertEquals("R", game.activePlayer());

        game.move(5);
        assertEquals("B", game.activePlayer());
        assertEquals("B", game.activePlayer());
    }

    @Test
    public void gameMove() {
        game = new ConnectFour("R");
        for (int r = 0; r < 6; r++)
            for (int c = 0; c < 7; c++)
                assertEquals("", game.get(r, c));

        game.move(4);
        assertEquals("R", game.get(5, 4));

        game.move(4);
        assertEquals("B", game.get(4, 4));
    }

    @Test
    public void gameMoveLegal() {
        game = new ConnectFour("R");

        assertTrue(game.moveLegal(0));
        assertTrue(game.moveLegal(1));
        assertTrue(game.moveLegal(2));
        assertTrue(game.moveLegal(6));

        game.move(1).move(2).move(1);
        assertTrue(game.moveLegal(1));
        assertTrue(game.moveLegal(2));
    }

    @Test
    public void moveNotLegalBounds() {
        game = new ConnectFour("R");
        assertFalse(game.moveLegal(-1));
        assertFalse(game.moveLegal(7));
        assertFalse(game.moveLegal(8));
    }

    @Test
    public void moveNotLegalFullColumn() {
        game = new ConnectFour("R")
            .move(5).move(5).move(5).move(5).move(5).move(4);

        assertTrue(game.moveLegal(5));

        game.move(5);
        assertFalse(game.moveLegal(5));
    }

    @Test
    public void gameAllMoves() {
        game = new ConnectFour("R")
            .move(0).move(0).move(2).move(3);

        List<Integer> moves = game.allMoves();
        assertEquals(7, moves.size());
        for (int i = 0; i < 7; i++)
            assertTrue(moves.contains(i));
    }

    @Test
    public void allMovesSomeMoves() {
        game = new ConnectFour("R")
            .move(0).move(0).move(0).move(0).move(0).move(0)
            .move(2).move(2).move(2).move(2).move(2).move(2)
            .move(5).move(5).move(5).move(5).move(5).move(5);

        List<Integer> moves = game.allMoves();
        assertEquals(4, moves.size());
        assertFalse(moves.contains(0));
        assertFalse(moves.contains(2));
        assertFalse(moves.contains(5));
    }

    @Test
    public void allMovesGameOver() {
        game = new ConnectFour("R");

        game.move(0).move(1).move(2).move(3).move(4).move(5).move(6);
        game.move(0).move(1).move(2).move(3).move(4).move(5).move(6);
        game.move(1).move(2).move(3).move(4).move(5).move(6).move(0);
        game.move(0).move(1).move(2).move(3).move(4).move(5).move(6);
        game.move(0).move(1).move(2).move(3).move(4).move(5).move(6);
        game.move(1).move(2).move(3).move(4).move(5).move(6).move(0);

        assertTrue(game.allMoves().isEmpty());
        assertTrue(game.gameOver());
    }
}
