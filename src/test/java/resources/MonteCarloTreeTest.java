package resources;

import games.tictactoe.TicTacToe;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.*;

class MonteCarloTreeTest {
    private static TicTacToe game;
    private static MonteCarloTree<TicTacToe.Move> tree;

    @BeforeAll
    public static void beforeAll() {
        game = new TicTacToe("X", true);
    }

    @Test
    public void treeInstantiation() {
        game = game.move(new TicTacToe.Move(1, 1));
        game = game.move(new TicTacToe.Move(0, 0));
        game = game.move(new TicTacToe.Move(0, 2));
        tree = new MonteCarloTree<>(game, 9);
        assertEquals(tree.getHead().getBranches().size(), 9);
    }
}
