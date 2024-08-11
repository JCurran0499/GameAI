package agents;

import resources.MonteCarloTree;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

/*
 * Always chooses the path with maximum possible gain.
 * A reasonably intelligent agent.
 */
public class Max implements Agent {
    private static final Random random = new Random();

    public <M> M takeTurn(MonteCarloTree<M> tree) {
        M choice = null;
        ArrayList<M> alternatives = new ArrayList<>();
        Map<M, MonteCarloTree<M>.Node> branches = tree.getHead().getBranches();
        for (M move : branches.keySet()) {
            int comparison = branches.get(move).compareTo(branches.get(choice));
            if (comparison > 0) {
                choice = move;
                alternatives.clear();
            }
            else if (comparison == 0)
                alternatives.add(move);
        }
        alternatives.add(choice);
        choice = alternatives.get(
            random.nextInt(alternatives.size())
        );

        tree.move(choice);
        return choice;
    }
}
