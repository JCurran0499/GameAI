import javax.swing.*;

public class GUI {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(1000, 1000);

        JLabel label = new JLabel("Tic Tac Toe");
        frame.add(label);

        JButton button = new JButton("Select Game");
        frame.add(button);

        frame.setVisible(true);
    }
}
