import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class eventHandler implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        e.getSource();
    }
}
