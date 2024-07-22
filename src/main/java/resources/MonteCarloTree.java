package resources;

import games.Game;
import lombok.Getter;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.lang.Math;

@Getter
public class MonteCarloTree<M> {
    @Getter
    public class Node  {
        private final String agent;
        private final Game<M> state;
        private final Map<M, Node> branches;

        private double heuristic;

        public Node(Game<M> state) {
            this.agent = state.activeAgent();
            this.state = state;
            this.branches = new LinkedHashMap<>();

            this.heuristic = 0;
        }

        public void instantiate(int depth) {
            if (isLeaf() && !this.state.gameOver() && depth > 0) {
                List<M> allMoves = this.state.allMoves();
                for (M move : allMoves) {
                    Game<M> nextState = this.state.move(move);
                    this.branches.put(move, new Node(nextState));
                }

                for (Node branch : this.branches.values())
                    branch.instantiate(depth - 1);
            }
        }

        public void propagateMinMax() {
            for (Node branch : this.branches.values())
                branch.propagateMinMax();

            if (!isLeaf()) {
                double[] options = this.branches.values().stream().mapToDouble(node -> node.heuristic).toArray();
                if (this.agent.equals(botAgent))
                    this.heuristic = maxHeuristic(options);
                else
                    this.heuristic = minHeuristic(options);
            }
            else {
                this.heuristic = this.state.heuristic();
            }
        }

        public boolean isLeaf() {
            return this.branches.isEmpty();
        }

        private double minHeuristic(double[] options) {
            double min = options[0];
            for (int i = 1; i < options.length; i++)
                min = Math.min(min, options[i]);

            return min;
        }

        private double maxHeuristic(double[] options) {
            double max = options[0];
            for (int i = 1; i < options.length; i++)
                max = Math.max(max, options[i]);

            return max;
        }
    }

    private Node head;
    private final String botAgent;
    private final int depth;

    public MonteCarloTree(Game<M> initialState, int depth) {
        this.head = new Node(initialState);
        this.botAgent = initialState.getBotAgent();
        this.depth = depth;

        this.head.instantiate(depth);
        this.head.propagateMinMax();
    }

    public void move(M move) {
        if (move == null)
            throw new RuntimeException("invalid move");

        this.head = this.head.branches.get(move);
        this.head.instantiate(this.depth);
        this.head.propagateMinMax();
    }
}
