package cli;

import agents.Agent;
import games.games2d.ConnectFour;
import resources.MonteCarloTree;

public class ConnectFourCLI extends CommandLineApp<Integer> {
    public ConnectFourCLI(Agent botAgent) {
        super(botAgent);
        this.game = new ConnectFour(this.playerAgent, this.playerGoesFirst);
        this.monteCarloTree = new MonteCarloTree<>(game, 5);
    }

    @Override
    protected Integer playerMoveChoice() {
        int move = -1;
        boolean valid;
        do {
            System.out.print("Choose a column: ");
            try {
                move = Integer.parseInt(System.console().readLine().strip()) - 1;
                valid = this.game.moveLegal(move);
            } catch (NumberFormatException e) {
                valid = false;
            }
        } while (!valid);

        return move;
    }

    @Override
    protected String playerAgentChoice() {
        String agent;
        boolean choice;
        do {
            System.out.print("Choose a color (R/B): ");
            agent = System.console().readLine().strip();
            choice = (agent.equals("R") || agent.equals("B"));
        } while (!choice);

        return agent;
    }
}
