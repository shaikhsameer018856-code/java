import javax.swing.*;
import java.awt.*;
import java.util.Calendar;

public class NumberedWallClock extends JPanel {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Analog Wall Clock");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 520);
        frame.add(new NumberedWallClock());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        int radius = Math.min(width, height) / 2 - 40;
        int centerX = width / 2;
        int centerY = height / 2;

        g2.setColor(Color.WHITE);
        g2.fillOval(centerX - radius, centerY - radius, radius * 2, radius * 2);

        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(4));
        g2.drawOval(centerX - radius, centerY - radius, radius * 2, radius * 2);

        g2.setFont(new Font("Arial", Font.BOLD, 22));
        for (int i = 1; i <= 12; i++) {

            double angle = Math.toRadians(i * 30 - 90); 
            int numRadius = radius - 30;

            int x = centerX + (int) (Math.cos(angle) * numRadius);
            int y = centerY + (int) (Math.sin(angle) * numRadius);

            String number = String.valueOf(i);
            int strWidth = g2.getFontMetrics().stringWidth(number);

            g2.drawString(number, x - strWidth / 2, y + 10);
        }

        Calendar cal = Calendar.getInstance();
        int sec = cal.get(Calendar.SECOND);
        int min = cal.get(Calendar.MINUTE);
        int hr = cal.get(Calendar.HOUR);

        double secAngle = Math.toRadians(sec * 6 - 90);
        double minAngle = Math.toRadians(min * 6 + sec * 0.1 - 90);
        double hrAngle = Math.toRadians(hr * 30 + min * 0.5 - 90);

        
        g2.setColor(Color.RED);
        g2.setStroke(new BasicStroke(2));
        int secX = centerX + (int) (Math.cos(secAngle) * (radius - 20));
        int secY = centerY + (int) (Math.sin(secAngle) * (radius - 20));
        g2.drawLine(centerX, centerY, secX, secY);

        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(4));
        int minX = centerX + (int) (Math.cos(minAngle) * (radius - 40));
        int minY = centerY + (int) (Math.sin(minAngle) * (radius - 40));
        g2.drawLine(centerX, centerY, minX, minY);

        
        g2.setStroke(new BasicStroke(6));
        int hrX = centerX + (int) (Math.cos(hrAngle) * (radius - 70));
        int hrY = centerY + (int) (Math.sin(hrAngle) * (radius - 70));
        g2.drawLine(centerX, centerY, hrX, hrY);

        g2.fillOval(centerX - 6, centerY - 6, 12, 12);

        repaint();
    }
}