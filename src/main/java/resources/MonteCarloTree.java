package resources;

import games.Game;
import lombok.Getter;
import java.util.*;
import java.lang.Math;

@Getter
public class MonteCarloTree<M> {
    @Getter
    public class Node  {
        private final String agent;
        private final M lastMove;
        private final Game<M> state;
        private final List<Node> branches;

        private double heuristic;

        public Node(Game<M> initialState, M lastMove) {
            this.agent = initialState.activeAgent();
            this.lastMove = lastMove;
            this.state = initialState;
            this.branches = new ArrayList<>();

            this.heuristic = 0;
        }

        public void instantiate(int depth) {
            if (isLeaf() && !this.state.gameOver() && depth > 0) {
                List<M> allMoves = this.state.allMoves();
                for (M move : allMoves) {
                    Game<M> nextState = this.state.move(this.agent, move);
                    this.branches.add(new Node(nextState, move));
                }

                for (Node branch : this.branches)
                    branch.instantiate(depth - 1);
            }
        }

        public void propagateMinMax() {
            for (Node branch : this.branches)
                branch.propagateMinMax();

            if (!isLeaf()) {
                double[] options = this.branches.stream().mapToDouble(node -> node.heuristic).toArray();
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

    public MonteCarloTree(Game<M> initialState, String botAgent, int depth) {
        this.head = new Node(initialState, null);
        this.botAgent = botAgent;
        this.depth = depth;

        this.head.instantiate(depth);
        this.head.propagateMinMax();
    }

    public void move(Node node) {
        this.head = node;
        this.head.instantiate(this.depth);
        this.head.propagateMinMax();
    }

    public String winner() {
        return this.head.state.winner();
    }

    public boolean isDraw() {
        return (winner() == null) && (this.head.isLeaf());
    }
}
