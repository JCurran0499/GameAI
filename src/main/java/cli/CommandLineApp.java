package cli;

import agents.Agent;
import games.Game2D;
import resources.MonteCarloTree;
import resources.PlayerTypes;

import java.util.Random;
import java.util.Scanner;

public abstract class CommandLineApp<M> {
    private static final Random random = new Random();
    protected final Scanner scanner;

    protected PlayerTypes playerTypes;
    protected String player;
    protected boolean playerGoesFirst;
    protected Agent botAgent;

    protected Game2D<?, M> game;
    protected MonteCarloTree<M> monteCarloTree;

    public CommandLineApp(Agent botAgent, PlayerTypes playerTypes, int depth, Scanner scanner) {
        this.scanner = scanner;

        this.botAgent = botAgent;
        this.playerTypes = playerTypes;
        this.player = playerChoice();
        System.out.println("You chose " + player + "!");

        this.playerGoesFirst = random.nextBoolean();
        if (playerGoesFirst)
            System.out.println("Congratulations! You are going first.");
        else
            System.out.println("Your opponent is going first");

        this.game = newGame();
        this.monteCarloTree = new MonteCarloTree<>(game, this.playerTypes.opposite(this.player), depth);
    }

    public void run() {
        while (!game.gameOver()) {
            if (game.activePlayer().equals(this.player)) {
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
        else if (game.winner().equals(this.player))
            System.out.println("Congratulations! You won!");
        else
            System.out.println("Sorry! You lost :(");
    }

    private String playerChoice() {
        String agent;
        boolean choice;
        String opt1 = this.playerTypes.type1();
        String opt2 = this.playerTypes.type2();
        System.out.println();
        do {
            System.out.printf("Choose a role (%s/%s): ", opt1, opt2);
            agent = this.scanner.nextLine().strip().toUpperCase();
            choice = (agent.equals(opt1) || agent.equals(opt2));
        } while (!choice);

        return agent;
    }

    protected abstract Game2D<?, M> newGame();
    protected abstract M playerMoveChoice();
}
