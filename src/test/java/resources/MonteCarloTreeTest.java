package resources;

import games.games2d.ConnectFour;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.*;

class MonteCarloTreeTest {
    private static ConnectFour game;
    private static MonteCarloTree<Integer> tree;

    @BeforeAll
    public static void beforeAll() {
        game = new ConnectFour("X", true);
    }

    @Test
    public void treeInstantiation() {
        //game = game.move(new TicTacToe.Move(1, 1));
        //game = game.move(new TicTacToe.Move(0, 0));
        //game = game.move(new TicTacToe.Move(0, 2));
        tree = new MonteCarloTree<>(game, 9);
        assertEquals(tree.getHead().getBranches().size(), 9);
    }
}
