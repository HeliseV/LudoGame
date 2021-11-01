import javax.swing.*;
import java.awt.*;

public class Plack extends JPanel {
    Plack() {
        this.setPreferredSize(new Dimension(100,100));
    }
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawLine(0,0, 500,500);
    }
}
