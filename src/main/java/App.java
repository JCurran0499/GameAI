import agents.Agent;
import agents.*;
import cli.CheckersCLI;
import cli.CommandLineApp;
import cli.ConnectFourCLI;
import cli.TicTacToeCLI;

import java.util.Scanner;

public class App {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Agent agent = chooseAgent();
        CommandLineApp<?> app = chooseGame(agent);

        if (app != null)
            app.run();

        scanner.close();
    }

    private static Agent chooseAgent() {
        int agent;
        boolean valid;
        System.out.println("\n(1) Walker");
        System.out.println("(2) Max");
        do {
            System.out.print("Choose your agent: ");
            try {
                agent = Integer.parseInt(scanner.nextLine());
                valid = (agent > 0 && agent <= 2);
            } catch (NumberFormatException e) {
                agent = -1;
                valid = false;
            }
        } while (!valid);

        return switch (agent) {
            case 1 -> new Walker();
            case 2 -> new Max();
            default -> null;
        };
    }

    private static CommandLineApp<?> chooseGame(Agent agent) {
        int game;
        boolean valid;
        System.out.println("\n(1) Tic Tac Toe");
        System.out.println("(2) Connect Four");
        System.out.println("(3) Checkers");
        do {
            System.out.print("Choose your game: ");
            try {
                game = Integer.parseInt(scanner.nextLine());
                valid = (game  > 0 && game <= 3);
            } catch (NumberFormatException e) {
                game = -1;
                valid = false;
            }
        } while (!valid);

        return switch(game) {
            case 1 -> new TicTacToeCLI(agent, scanner);
            case 2 -> new ConnectFourCLI(agent, scanner);
            case 3 -> new CheckersCLI(agent, scanner);
            default -> null;
        };
    }
}
