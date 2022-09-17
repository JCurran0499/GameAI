import Games.ConnectFourBoard;
import Games.*;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class GamePlay {
    private static final GameAgent bot = new GameAgent();
    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();

    public static void main(String[] args) {
        System.out.println("Choose a game!\n1: Tic Tac Toe\n2: Connect 4\n3: The Bubble Game");
        int gameChoice = scanner.nextInt();

        System.out.println("Choose an opponent!\n1: Walker (random walk)\n2: Max (simple heuristic minimax)");
        bot.playSetting = scanner.nextShort();

        switch (gameChoice) {
            case 1:
                TicTacToe();
                break;
            case 2:
                ConnectFour();
                break;
            case 3:
                BubbleGame();
                break;
            default:
                System.out.println("Invalid choice, try again");
                main(args);
        }
    }

    public static void play(MonteCarloTree searchTree) {
        while (!(searchTree.gameWon() || searchTree.gameTied())) {

            System.out.println("\n\n");
            System.out.println(searchTree.state().visualize());
            if (searchTree.agentTurn() == 1) {
                GameBoard2D.Move choice = searchTree.state().playerInput();

                for (MonteCarloTree.Node n : searchTree.branches()) {
                    if (searchTree.state().compareMoves(choice, n.move)) searchTree.move(n); // problem location? is the tree not moving?
                }

                continue;
            }

            if (searchTree.agentTurn() == -1) {
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                bot.takeTurn(searchTree);
            }
        }
        System.out.println("\n\n");
        System.out.println(searchTree.state().visualize());

        if (searchTree.gameTied())
            System.out.println("GAME TIED");
        else
            System.out.println("WINNER: " + searchTree.winner());
    }

    private static void ConnectFour() {
        int firstTurn = random.nextBoolean() ? 1 : -1;
        MonteCarloTree.Node start = new MonteCarloTree.Node(null, new ConnectFourBoard(7, 7), firstTurn);
        MonteCarloTree searchTree = new MonteCarloTree(start, 6);

        play(searchTree);
    }

    private static void BubbleGame() {
        int firstTurn = random.nextBoolean() ? 1 : -1;
        MonteCarloTree.Node start = new MonteCarloTree.Node(null, new BubbleBoard(6), firstTurn);
        MonteCarloTree searchTree = new MonteCarloTree(start, 4);

        play(searchTree);
    }

    private static void TicTacToe() {
        int firstTurn = random.nextBoolean() ? 1 : -1;
        MonteCarloTree.Node start = new MonteCarloTree.Node(null, new TicTacToeBoard(), firstTurn);
        MonteCarloTree searchTree = new MonteCarloTree(start, 9);

        play(searchTree);
    }
}
