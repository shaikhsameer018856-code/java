import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class Passwordgenerate extends JFrame {

    JTextField lengthField;
    JTextArea resultArea;

    public Passwordgenerate() {
        setTitle("Password Generator");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());
        
        JLabel label = new JLabel("Enter Password Length:");
        add(label);

        lengthField = new JTextField(10);
        add(lengthField);

        JButton generateButton = new JButton("Generate Password");
        add(generateButton);

        resultArea = new JTextArea(3, 30);
        resultArea.setEditable(false);
        add(resultArea);

        
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

               
                String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
                String lower = "abcdefghijklmnopqrstuvwxyz";
                String numbers = "0123456789";
                String symbols = "!@#$%^&*()_-+=<>?/{}[]";

                String allCharacters = upper + lower + numbers + symbols;

                try {
                    int length = Integer.parseInt(lengthField.getText());
                    StringBuilder password = new StringBuilder();
                    Random random = new Random();

                    for (int i = 0; i < length; i++) {
                        int index = random.nextInt(allCharacters.length());
                        password.append(allCharacters.charAt(index));
                    }

                    resultArea.setText("Generated Password:\n" + password.toString());

                } catch (Exception ex) {
                    resultArea.setText("Error: Enter valid number!");
                }
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new Passwordgenerate();
    }
}