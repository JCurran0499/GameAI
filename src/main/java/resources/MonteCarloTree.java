package resources;

import games.Game;
import lombok.Getter;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

@Getter
public class MonteCarloTree<M> {
    @Getter
    public class Node  {
        private final String agent;
        private final Game<M> state;
        private final Map<M, Node> branches;

        private double heuristic;
        private int gameTime;

        private Node(Game<M> state) {
            this.agent = state.activeAgent();
            this.state = state;
            this.branches = new LinkedHashMap<>();

            this.heuristic = 0;
            this.gameTime = 0;
        }

        public void instantiate(int depth) {
            if (depth > 0) {
                if (isLeaf() && !this.state.gameOver()) {
                    List<M> allMoves = this.state.allMoves();
                    for (M move : allMoves) {
                        Game<M> nextState = this.state.move(move);
                        Node child = new Node(nextState);
                        child.gameTime = this.gameTime + 1;
                        this.branches.put(move, child);
                    }
                }

                for (Node branch : this.branches.values())
                    branch.instantiate(depth - 1);
            }
        }

        public void propagateMinMax() {
            for (Node branch : this.branches.values())
                branch.propagateMinMax();

            if (!isLeaf()) {
                List<Node> options = this.branches.values().stream().toList();
                Node bestNode;
                if (this.agent.equals(botAgent))
                    bestNode = maxHeuristicNode(options, 1);
                else
                    bestNode = maxHeuristicNode(options, -1);

                this.heuristic = bestNode.heuristic;
                this.gameTime = bestNode.gameTime;
            }
            else {
                this.heuristic = this.state.heuristic();
            }
        }

        public boolean isLeaf() {
            return this.branches.isEmpty();
        }

        /*
         * Returns 1 if the calling node is better for the bot
         * Returns -1 if the calling node is better for the player
         * Returns 0 if there is no difference
         */
        /*
         * TODO: Right now, this method assumes that in a tied game the
         *  bot will want to lengthen the game while the player will want
         *  to shorten the game. This is a bit of an inconsistency.
         */
        public int compareTo(Node n) {
            if (n == null || this.heuristic > n.heuristic)
                return 1;

            else if (this.heuristic == n.heuristic) {
                if (
                    (this.heuristic > 0 && this.gameTime < n.gameTime) ||
                    (this.heuristic <= 0 && this.gameTime > n.gameTime)
                )
                    return 1;

                else if (this.gameTime == n.gameTime)
                    return 0;

                else
                    return -1;
            }

            return -1;

        }


        private Node maxHeuristicNode(List<Node> options, int compare) {
            Node max = options.getFirst();
            for (int i = 1; i < options.size(); i++) {
                Node n = options.get(i);
                if ((n.compareTo(max) * compare) > 0)
                    max = n;
            }

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
