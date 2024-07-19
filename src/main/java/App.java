import games.tictactoe.TicTacToe;
import resources.MonteCarloTree;

import java.util.Random;

public class App {
    private static final Random random = new Random();

    public static void main(String[] args) {
        String playerAgent = playerAgentChoice();
        System.out.println("You chose " + playerAgent + "!");

        boolean playerGoesFirst = random.nextBoolean();
        if (playerGoesFirst)
            System.out.println("Congratulations! You are going first.");
        else
            System.out.println("Your opponent is going first");

        TicTacToe game = new TicTacToe(playerAgent, playerGoesFirst);
        MonteCarloTree<TicTacToe.Move> monteCarloTree = new MonteCarloTree<>(game, TicTacToe.oppositeAgent(playerAgent), 5);

        while (!game.gameOver()) {
            System.out.println(game.visualize());
            TicTacToe.Move move = playerMoveChoice(game);
        }
    }

    private static String playerAgentChoice() {
        String agent;
        boolean choice;
        do {
            System.out.print("Choose a role (X/O): ");
            agent = System.console().readLine().strip();
            choice = (agent.equals("X") || agent.equals("O"));
        } while (!choice);

        return agent;
    }

    private static TicTacToe.Move playerMoveChoice(TicTacToe game) {
        TicTacToe.Move move;
        boolean valid;
        do {
            System.out.print("Choose a board space: ");
            int space = Integer.parseInt(System.console().readLine().strip());
            move = new TicTacToe.Move(space / 3, space % 3);
            valid = game.get(move.x(), move.y()).free();
        } while (!valid);

        return move;
    }
}
