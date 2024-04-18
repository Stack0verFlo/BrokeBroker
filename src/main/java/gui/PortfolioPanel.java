package gui;

import controllers.PortfolioController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PortfolioPanel extends JPanel{
    private final PortfolioController portfolioController;
    private JTextField portfolioIdTextField;
    private JTextField symbolTextField;
    private JTextField quantityTextField;

    public PortfolioPanel() {
        portfolioController = new PortfolioController();
        setLayout(new BorderLayout());
        add(createPortfolioForm(), BorderLayout.NORTH);
    }

    private JPanel createPortfolioForm() {
        JPanel panel = new JPanel(new GridLayout(4, 2));

        panel.add(new JLabel("Portfolio ID:"));
        portfolioIdTextField = new JTextField();
        panel.add(portfolioIdTextField);

        panel.add(new JLabel("Symbol:"));
        symbolTextField = new JTextField();
        panel.add(symbolTextField);

        panel.add(new JLabel("Quantity:"));
        quantityTextField = new JTextField();
        panel.add(quantityTextField);

        JButton addButton = new JButton("Add Stock");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String portfolioId = portfolioIdTextField.getText();
                String symbol = symbolTextField.getText();
                int quantity = Integer.parseInt(quantityTextField.getText());
                portfolioController.addStockToPortfolio(portfolioId, symbol, quantity);
                JOptionPane.showMessageDialog(PortfolioPanel.this, "Stock Added", "Add Stock", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        panel.add(addButton);

        return panel;
    }
}
