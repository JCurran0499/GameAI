package agents;

import resources.MonteCarloTree;

import java.util.Map;

/*
 * Always chooses the path with maximum possible gain.
 * A reasonably intelligent agent.
 */
public class Max implements Agent {
    public <M> M takeTurn(MonteCarloTree<M> tree) {
        M choice = null;
        Map<M, MonteCarloTree<M>.Node> branches = tree.getHead().getBranches();
        for (M move : branches.keySet()) {
            if (branches.get(move).compareTo(branches.get(choice)) > 0)
                choice = move;
        }

        tree.move(choice);
        return choice;
    }
}
