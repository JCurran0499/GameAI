package agents;

import resources.MonteCarloTree;

import java.util.List;
import java.util.Random;

/*
 * Performs a random walk against the player.
 * This is the dumbest agent.
 */
public class Walker implements Agent {
    private static final Random random = new Random();

    public <M> M takeTurn(MonteCarloTree<M> tree) {
        List<M> moves = tree.getHead().getBranches().keySet().stream().toList();
        M choice = moves.get(
            random.nextInt(moves.size())
        );

        tree.move(choice);
        return choice;
    }
}
