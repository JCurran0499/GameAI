import games.games2d.TicTacToe;
import gui.GameFrame;
import gui.GamePanel;
import gui.tictactoe.TicTacToePanel;

import javax.swing.*;

public class GUI {
    public static void main(String[] args) {
        GameFrame frame = new GameFrame("GameAI");

        GamePanel<TicTacToe.Move> panel = new TicTacToePanel();
        frame.addPanel(panel);

        frame.setVisible(true);
    }
}
