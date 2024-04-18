package gui;

import controllers.StockController;

import javax.swing.*;
import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StockPanel extends JPanel{
    private final StockController stockController;
    private JTextField symbolTextField;
    private JTextField priceTextField;

    public StockPanel() {
        stockController = new StockController();
        setLayout(new BorderLayout());
        add(createStockForm(), BorderLayout.NORTH);
    }

    private JPanel createStockForm() {
        JPanel panel = new JPanel(new GridLayout(3, 2));

        panel.add(new JLabel("Symbol:"));
        symbolTextField = new JTextField();
        panel.add(symbolTextField);

        panel.add(new JLabel("Price:"));
        priceTextField = new JTextField();
        panel.add(priceTextField);

        JButton updateButton = new JButton("Update Price");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String symbol = symbolTextField.getText();
                double price = Double.parseDouble(priceTextField.getText());
                stockController.updateStockPrice(symbol, price);
                JOptionPane.showMessageDialog(StockPanel.this, "Price Updated", "Update", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        panel.add(updateButton);

        return panel;
    }
}
