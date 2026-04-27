import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CurrencyConverterUI extends JFrame {

    public CurrencyConverterUI() {

        setTitle("Currency Converter");
        setSize(420, 380);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel bg = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(58, 123, 213),
                        0, getHeight(), new Color(58, 96, 115));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        bg.setLayout(new GridBagLayout());

        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(320, 280));
        card.setBackground(new Color(255, 255, 255, 50));
        card.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 120), 2, true));
        card.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(12, 12, 12, 12);

        JLabel title = new JLabel("Currency Converter");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);

        JTextField amount = new JTextField(12);
        amount.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        amount.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        String[] currencies = {
                "PKR", "USD", "EUR", "GBP", "AUD",
                "CAD", "INR", "JPY", "SAR", "AED"
        };

        JComboBox<String> from = new JComboBox<>(currencies);
        JComboBox<String> to = new JComboBox<>(currencies);

        from.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        to.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        JButton convertBtn = new JButton("Convert");
        convertBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        convertBtn.setBackground(new Color(255, 255, 255));
        convertBtn.setFocusable(false);
        convertBtn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));

        JLabel result = new JLabel("");
        result.setFont(new Font("Segoe UI", Font.BOLD, 18));
        result.setForeground(Color.WHITE);

        convertBtn.addActionListener(e -> {
            try {
                double val = Double.parseDouble(amount.getText());
                double converted = convert(val,
                        from.getSelectedItem().toString(),
                        to.getSelectedItem().toString());
                result.setText("Result is  " + String.format("%.2f", converted));
            } catch (Exception ex) {
                result.setText("Invalid Input!");
            }
        });

        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        card.add(new JLabel("Amount:"), c);

        // TEXT FIELD FIX
        c.gridx = 1;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL; 
        c.weightx = 1.0; 
        card.add(amount, c);

        c.gridx = 0;
        c.gridy = 1;
        card.add(new JLabel("From:"), c);
        c.gridx = 1;
        c.gridy = 1;
        card.add(from, c);

        c.gridx = 0;
        c.gridy = 2;
        card.add(new JLabel("To:"), c);
        c.gridx = 1;
        c.gridy = 2;
        card.add(to, c);

        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 2;
        card.add(convertBtn, c);

        c.gridx = 0;
        c.gridy = 0;
        bg.add(title, c);
        c.gridy = 1;
        bg.add(card, c);
        c.gridy = 2;
        bg.add(result, c);

        add(bg);
        setVisible(true);
    }

    public double convert(double amount, String from, String to) {

        double PKR = 1, USD = 278.0, EUR = 300.0, GBP = 350.0, AUD = 185.0,
                CAD = 205.0, INR = 3.30, JPY = 1.85, SAR = 74.0, AED = 76.0;

        double fromRate = switch (from) {
            case "USD" -> USD;
            case "EUR" -> EUR;
            case "GBP" -> GBP;
            case "AUD" -> AUD;
            case "CAD" -> CAD;
            case "INR" -> INR;
            case "JPY" -> JPY;
            case "SAR" -> SAR;
            case "AED" -> AED;
            default -> PKR;
        };

        double toRate = switch (to) {
            case "USD" -> USD;
            case "EUR" -> EUR;
            case "GBP" -> GBP;
            case "AUD" -> AUD;
            case "CAD" -> CAD;
            case "INR" -> INR;
            case "JPY" -> JPY;
            case "SAR" -> SAR;
            case "AED" -> AED;
            default -> PKR;
        };

        return (amount * fromRate) / toRate;
    }

    public static void main(String[] args) {
        new CurrencyConverterUI();
    }
}