package agents;

import resources.MonteCarloTree;

import java.util.List;
import java.util.Random;

/*
 * Performs a random walk against the player.
 * This is the dumbest agent.
 */
public class Walker<M> implements Agent<M> {
    private static final Random random = new Random();

    private final MonteCarloTree<M> tree;

    public Walker(MonteCarloTree<M> tree) {
        this.tree = tree;
    }

    public M takeTurn() {
        List<M> moves = this.tree.getHead().getBranches().keySet().stream().toList();
        M choice = moves.get(
            random.nextInt(moves.size())
        );

        this.tree.move(choice);
        return choice;
    }
}
