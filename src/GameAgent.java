import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class GameAgent {
    private static final Random random = new Random();

    private boolean victory = false;
    public final short id = -1;
    public short playSetting = 0;

    public void takeTurn(MonteCarloTree searchTree) {
        switch (playSetting) {
            case 1:
                randomWalk(searchTree);
                break;
            case 2:
                minimax(searchTree);
                break;
        }
    }

    private void randomWalk(MonteCarloTree searchTree) {
        for (MonteCarloTree.Node n : searchTree.branches()) {
            if (n.winner == this.id) {
                searchTree.move(n);
                victory = true;
            }
        }

        if (!victory) {
            MonteCarloTree.Node randomChoice = searchTree.branches().get(random.nextInt(searchTree.branchFactor()));
            searchTree.move(randomChoice);
        }
    }

    private void minimax(MonteCarloTree searchTree) {
        List<MonteCarloTree.Node> losses = new ArrayList<>();
        List<MonteCarloTree.Node> draws = new ArrayList<>();
        List<MonteCarloTree.Node> wins = new ArrayList<>();

        MonteCarloTree.Node minimaxChoice;
        for (MonteCarloTree.Node n : searchTree.branches()) {
            if (n.pathWinner == this.id)
                wins.add(n);
            else if (n.pathWinner == 0)
                draws.add(n);
            else
                losses.add(n);
        }

        if (!wins.isEmpty())
            minimaxChoice = wins.get(random.nextInt(wins.size()));
        else if (!draws.isEmpty())
            minimaxChoice = draws.get(random.nextInt(draws.size()));
        else
            minimaxChoice = losses.get(random.nextInt(losses.size()));

        searchTree.move(minimaxChoice);
    }

}
