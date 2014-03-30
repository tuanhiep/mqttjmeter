import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Test {

    public void start() {
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        JButton madLibButton = new JButton("Lib it!");
        JLabel title = new JLabel("Welcome to mad libs! \n Put in your words and press the 'Lib It' button to play!");
        JLabel nameLabel = new JLabel("Name: ");
        JLabel verbLabel1 = new JLabel("Verb: ");
        JLabel adjLabel = new JLabel("Adjective: ");
        JLabel verbLabel2 = new JLabel("Verb: ");
        JLabel nounLabel = new JLabel("Noun: ");
        JTextField nameTxt = new JTextField(20);
        JTextField verbTxt1 = new JTextField(20);
        JTextField adjTxt = new JTextField(20);
        JTextField verbTxt2 = new JTextField(20);
        JTextField nounTxt = new JTextField(20);

        frame.getContentPane().add(BorderLayout.SOUTH, madLibButton);
        frame.getContentPane().add(BorderLayout.NORTH, title);

        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.green);
        frame.getContentPane().add(panel);
        GridBagConstraints left = new GridBagConstraints();
        left.anchor = GridBagConstraints.EAST;
        GridBagConstraints right = new GridBagConstraints();
        right.weightx = 2.0;
        right.fill = GridBagConstraints.HORIZONTAL;
        right.gridwidth = GridBagConstraints.REMAINDER;
        panel.add(nameLabel, left);
        panel.add(nameTxt, right);
        panel.add(verbLabel1, left);
        panel.add(verbTxt1, right);
        panel.add(adjLabel, left);
        panel.add(adjTxt, right);
        panel.add(verbLabel2, left);
        panel.add(verbTxt2, right);
        panel.add(nounLabel, left);
        panel.add(nounTxt, right);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Test main = new Test();
                main.start();
            }
        });
    }

}