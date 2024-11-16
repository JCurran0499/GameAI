package agents;

import resources.MonteCarloTree;

public interface Agent {
    <M > M takeTurn(MonteCarloTree<M> tree);
}
