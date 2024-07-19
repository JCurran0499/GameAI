package resources;

import games.Game;

import java.util.*;
import java.lang.Math;

public class MonteCarloTree<M> {
    public class Node  {
        private String agent;
        private M lastMove;
        private Game<M> state;
        private List<Node> branches;

        private double heuristic;

        public Node(Game<M> initialState, M lastMove) {
            this.agent = initialState.activeAgent();
            this.lastMove = lastMove;
            this.state = initialState;
            this.branches = new ArrayList<>();

            this.heuristic = 0;
        }

        public void instantiate(int depth) {
            if (this.branches.isEmpty() && !this.state.gameOver() && depth > 0) {
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

            if (!this.branches.isEmpty()) {
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
    private String botAgent;
}
