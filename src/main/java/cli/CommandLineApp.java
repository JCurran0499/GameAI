package cli;

import agents.Agent;
import games.Game2D;
import resources.MonteCarloTree;

import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Callable;

public abstract class CommandLineApp<M> {
    private static final Random random = new Random();
    protected final Scanner scanner;

    protected String player;
    protected Agent botAgent;
    protected Game2D<?, M> game;
    protected MonteCarloTree<M> monteCarloTree;

    public CommandLineApp(Agent botAgent, int depth, Scanner scanner, Callable<Game2D<?, M>> f) {
        this.botAgent = botAgent;
        this.scanner = scanner;

        try {
            this.game = f.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        boolean playerGoesFirst = random.nextBoolean();
        if (playerGoesFirst) {
            this.player = this.game.getPlayers().player1();
            System.out.println("Congratulations! You are going first");
        }
        else {
            this.player = this.game.getPlayers().player2();
            System.out.println("Your opponent is going first");
        }
        System.out.println("You are player " + this.player);

        this.monteCarloTree = new MonteCarloTree<>(game, this.game.getPlayers().opposite(this.player), depth);
    }

    public void run() {
        while (!this.game.gameOver()) {
            if (this.game.activePlayer().equals(this.player)) {
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
        if (this.game.winner() == null)
            System.out.println("The game was a draw!");
        else if (this.game.winner().equals(this.player))
            System.out.println("Congratulations! You won!");
        else
            System.out.println("Sorry! You lost :(");
    }

    protected abstract M playerMoveChoice();
}
