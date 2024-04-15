package Frameworks;

import javax.swing.*;
import java.awt.*;

public class ValueGui extends JPanel {
    private JLabel balanceLabel;

    public ValueGui() {
        initializeComponents();
    }

    private void initializeComponents() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        balanceLabel = new JLabel("Guthaben: N/A");
        add(balanceLabel);
        setBorder(BorderFactory.createTitledBorder("Nutzerkonto"));
    }

    // Zukünftige Methode zum Aktualisieren der Balance
    public void updateBalance(double balance) {
        balanceLabel.setText("Guthaben: " + String.format("%.2f€", balance));
    }
}
