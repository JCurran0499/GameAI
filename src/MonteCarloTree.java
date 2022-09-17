import Games.GameBoard2D;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MonteCarloTree {

    public static class Node {
        public final GameBoard2D.Move move;
        public final GameBoard2D state;
        public final ArrayList<Node> branches = new ArrayList<>();
        public final int agent;
        public final int winner;
        public int pathWinner = 0;

        public Node(GameBoard2D.Move k, GameBoard2D v, int a) {
            move = k;
            state = v;
            agent = a;

            winner = state.gameOver() ? (a * -1) : 0;
        }

        public void instantiate(int depth) {
            if (depth > 1) {
                if (branches.isEmpty() && winner == 0) {
                    List<GameBoard2D.Move> options = state.allMoves(agent);
                    for (GameBoard2D.Move move : options)
                        branches.add(new Node(move, state.move(agent, move), agent * -1));
                }

                for (Node n : branches)
                    n.instantiate(depth - 1);
            }
        }

        public void propagateMinimax() {
            for (Node n : branches)
                n.propagateMinimax();

            if (!branches.isEmpty()) {
                List<Integer> options = new ArrayList<>();
                for (Node n : branches)
                    options.add(n.pathWinner);

                if (agent == 1)
                    pathWinner = Collections.max(options);
                else if (agent == -1)
                    pathWinner = Collections.min(options);
            }
            else if (winner == 0){
                pathWinner = state.heuristic(agent);
            }
            else {
                pathWinner = winner;
            }
        }
    }

    private Node head;
    private final int maxDepth;

    public MonteCarloTree(Node start, int max) {
        head = start;
        maxDepth = max;

        head.instantiate(max);
        head.propagateMinimax();
    }

    public GameBoard2D state() {
        return head.state;
    }

    public int agentTurn() {
        return head.agent;
    }

    public int winner() {
        return head.winner;
    }

    public List<Node> branches() {
        return head.branches;
    }

    public void move(Node n) {
        head = n;
        head.instantiate(maxDepth);
        head.propagateMinimax();
    }

    public int branchFactor() {
        return head.branches.size();
    }

    public boolean gameWon() {
        return head.winner != 0;
    }

    public boolean gameTied() {
        return !gameWon() && head.branches.isEmpty();
    }

}
