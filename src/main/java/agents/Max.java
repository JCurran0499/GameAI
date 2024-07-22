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
        double heuristic = Double.MAX_VALUE * -1;
        Map<M, MonteCarloTree<M>.Node> branches = tree.getHead().getBranches();
        for (M move : branches.keySet()) {
            if (branches.get(move).getHeuristic() > heuristic) {
                choice = move;
                heuristic = branches.get(move).getHeuristic();
            }
        }

        tree.move(choice);
        return choice;
    }
}
