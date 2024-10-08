package cli;

import agents.Agent;
import games.games2d.TicTacToe;
import resources.MonteCarloTree;

public class TicTacToeCLI extends CommandLineApp<TicTacToe.Move> {
    public TicTacToeCLI(Agent botAgent) {
        super(botAgent);
        this.game = new TicTacToe(this.playerAgent, this.playerGoesFirst);
        this.monteCarloTree = new MonteCarloTree<>(game, 5);
    }

    protected TicTacToe.Move playerMoveChoice() {
        TicTacToe.Move move = null;
        boolean valid;
        do {
            System.out.print("Choose a board space: ");
            try {
                int space = Integer.parseInt(System.console().readLine().strip());
                move = new TicTacToe.Move((space - 1) / 3, (space - 1) % 3);
                valid = game.moveLegal(move);
            } catch (NumberFormatException e) {
                valid = false;
            }
        } while (!valid);

        return move;
    }

    protected String playerAgentChoice() {
        String agent;
        boolean choice;
        do {
            System.out.print("Choose a role (X/O): ");
            agent = System.console().readLine().strip().toUpperCase();
            choice = (agent.equals("X") || agent.equals("O"));
        } while (!choice);

        return agent;
    }
}
