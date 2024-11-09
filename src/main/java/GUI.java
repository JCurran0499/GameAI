import javax.swing.*;
import java.awt.*;

public class GUI {
    public static void main(String[] args) {
        JFrame frame = new JFrame("GameAI");
        frame.setSize(2000, 1000);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(new FlowLayout());

        frame.getContentPane().setBackground(new Color(150, 200, 250));

        ImageIcon image = new ImageIcon("x.png");
        frame.setIconImage(image.getImage());

        JLabel label1 = new JLabel("Username");
        frame.add(label1);
        JTextField textField1 = new JTextField();
        frame.add(textField1);

        JLabel label2 = new JLabel("Passowrd");
        frame.add(label2);
        JTextField textField2 = new JTextField();
        frame.add(textField2);

        JButton button = new JButton("Submit");
        frame.add(button);

        frame.setVisible(true);
    }
}
