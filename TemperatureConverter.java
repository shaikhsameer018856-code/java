import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TemperatureConverter extends JFrame {
    public TemperatureConverter() {
        setTitle("Temperature Converter");
        setSize(420, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        JPanel bg = new JPanel(){
            @Override
            protected  void paintComponent(Graphics g){
                super.paintComponent(g);
               Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(135, 206, 250),
                                                     0, getHeight(), new Color(70, 130, 180));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        bg.setLayout(new GridBagLayout());
        
        

        JLabel title = new JLabel("Celsius to Fahrenhit Converter");
        title.setFont(new Font("Times New Roman", Font.BOLD, 18));

        JTextField input = new JTextField(10);
        JButton btn = new JButton("Converter");

        JLabel output = new JLabel(" ");

        btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    double c = Double.parseDouble(input.getText());
                    double f = (c * 9 / 5) + 32;
                    output.setText("Fahrenhit "  + f);

                } catch (Exception ex) {
                    output.setText("Invalid Inuput");

                }

            }
        });
            GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);

        c.gridx = 0; c.gridy = 0; bg.add(title, c);
        c.gridx = 0; c.gridy = 1; bg.add(input, c);
        c.gridx = 0; c.gridy = 2; bg.add(btn, c);
        c.gridx = 0; c.gridy = 3; bg.add(output, c);

        add(bg);
        setVisible(true);

    }

    public static void main(String[] args) {
        new TemperatureConverter();
    }
}
