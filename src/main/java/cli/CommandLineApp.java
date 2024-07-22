package cli;

import agents.Agent;
import games.Game;
import resources.MonteCarloTree;

import java.util.Random;

public abstract class CommandLineApp<M> {
    private static final Random random = new Random();

    protected String playerAgent;
    protected boolean playerGoesFirst;
    protected Game<M> game;
    protected MonteCarloTree<M> monteCarloTree;
    protected Agent botAgent;

    // must implement the instantiation for "game" and "monteCarloTree"
    public CommandLineApp(Agent botAgent) {
        this.botAgent = botAgent;
        this.playerAgent = playerAgentChoice();
        System.out.println("You chose " + playerAgent + "!");

        this.playerGoesFirst = random.nextBoolean();
        if (playerGoesFirst)
            System.out.println("Congratulations! You are going first.");
        else
            System.out.println("Your opponent is going first");
    }

    public void run() {
        while (!game.gameOver()) {
            if (game.activeAgent().equals(this.playerAgent)) {
                System.out.println("\r" + game.visualize());
                M move = playerMoveChoice();

                this.game = this.game.move(move);
                this.monteCarloTree.move(move);
            }

            else {
                M move = this.botAgent.takeTurn(this.monteCarloTree);
                this.game = this.game.move(move);
            }
        }

        System.out.println("\r" + game.visualize());
        if (game.winner() == null)
            System.out.println("The game was a draw!");
        else if (game.winner().equals(this.playerAgent))
            System.out.println("Congratulations! You won!");
        else
            System.out.println("Sorry! You lost :(");
    }

    protected abstract String playerAgentChoice();
    protected abstract M playerMoveChoice();
}
