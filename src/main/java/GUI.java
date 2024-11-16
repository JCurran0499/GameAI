import gui.GameFrame;
import gui.connect4.ConnectFourPanel;
import gui.tictactoe.TicTacToePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class GUI {
    public static void main(String[] args) {
        GameFrame frame = new GameFrame("GameAI");

        JPanel gameChoicePanel = new JPanel();
        gameChoicePanel.setLayout(new GridLayout(0, 1, 10, 5));
        gameChoicePanel.setBounds(125, 200, 750, 550);

        gameChoicePanel.add(gameButton(
            "Tic Tac Toe",
            e -> {
                gameChoicePanel.setVisible(false);
                frame.addPanel(new TicTacToePanel());
            }
        ));
        gameChoicePanel.add(gameButton(
            "Connect 4",
            e -> {
                gameChoicePanel.setVisible(false);
                frame.addPanel(new ConnectFourPanel());
            }
        ));

        JButton homeButton = new JButton();
        homeButton.setBounds(25, 15, 75, 75);
        homeButton.setIcon(
            new ImageIcon(
                new ImageIcon("src/main/resources/icons/home.png").getImage().getScaledInstance(
                    homeButton.getSize().width, homeButton.getSize().height, Image.SCALE_SMOOTH
                )
            )
        );
        homeButton.addActionListener(e -> {
            frame.getPanel().setVisible(false);
            frame.getResetButton().setVisible(false);
            frame.getContentPane().setBackground(GameFrame.GAMEPLAY_COLOR);
            gameChoicePanel.setVisible(true);
        });

        frame.add(gameChoicePanel);
        frame.add(homeButton);
        frame.setVisible(true);
    }

    private static JButton gameButton(String game, ActionListener actionListener) {
        JButton button = new JButton(game);
        button.setFont(new Font("Comic Sans MS", Font.BOLD, 36));
        button.addActionListener(actionListener);
        return button;
    }
}
