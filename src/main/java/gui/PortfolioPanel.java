package gui;

import Entities.Portfolio;
import Entities.User;
import controllers.PortfolioController;
import controllers.StockController;
import controllers.UserController;
import repositories.PriceUpdateListener;
import services.PortfolioService;
import services.StockService;
import services.UserService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;

public class PortfolioPanel extends JPanel implements PriceUpdateListener {
    private final PortfolioController portfolioController;
    private final StockController stockController;
    private final UserController userController;
    private JComboBox<String> stocksComboBox;
    private JLabel portfolioIdLabel;
    private JTextField quantityTextField;
    private JLabel currentPriceLabel;

    public PortfolioPanel(PortfolioService portfolioService, StockService stockService, UserService userService) {
        SwingUtilities.invokeLater(this::loadCurrentUserPortfolio); // Diese Methode wird auf dem EDT aufgerufen
        this.portfolioController = new PortfolioController(portfolioService);
        this.stockController = new StockController(stockService);
        this.userController = new UserController(userService);
        this.stockController.setPriceUpdateListener(this);
        currentPriceLabel = new JLabel("Current Price: Loading...");
        setLayout(new BorderLayout());
        initializeComponents();
        add(createPortfolioForm(), BorderLayout.NORTH);
        stocksComboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String selectedSymbol = (String) e.getItem();
                // Nehmen Sie an, dass die Methode getStockPrice das Symbol nimmt und den aktuellen Preis zurückgibt.
                double price = stockController.getStockPrice(selectedSymbol);
                onPriceUpdate(selectedSymbol, price);
            }
        });

        loadCurrentUserPortfolio();
        revalidate();
        repaint();
    }

    private void initializeComponents() {
        JPanel panel = createPortfolioForm();
        add(panel, BorderLayout.NORTH);
        loadCurrentUserPortfolio();
    }
    @Override
    public void onPriceUpdate(String symbol, double newPrice) {
        if (symbol.equals(stocksComboBox.getSelectedItem())) {
            currentPriceLabel.setText(String.format("Current Price: %.2f", newPrice));
        }
    }

    private void loadCurrentUserPortfolio() {
        User currentUser = userController.getCurrentUser();
        if (currentUser != null) {
            // Abrufen der Portfolio-Informationen in einem separaten Thread
            new Thread(() -> {
                Portfolio currentPortfolio = portfolioController.getPortfolioByUserId(currentUser.getId());
                if (currentPortfolio != null) {
                    // Aktualisieren des Labels auf dem EDT
                    SwingUtilities.invokeLater(() -> {
                        portfolioIdLabel.setText(currentPortfolio.getId());
                        portfolioIdLabel.revalidate();
                        portfolioIdLabel.repaint();
                    });
                }
            }).start();
        }
    }


    private JPanel createPortfolioForm() {
        JPanel panel = new JPanel(new GridLayout(5, 2));
        panel.add(new JLabel("Portfolio ID:"));
        portfolioIdLabel = new JLabel("Loading...");
        panel.add(portfolioIdLabel);

        stocksComboBox = new JComboBox<>(stockController.getAllSymbols().toArray(new String[0]));
        panel.add(new JLabel("Symbol:"));
        panel.add(stocksComboBox);

        quantityTextField = new JTextField();
        panel.add(new JLabel("Quantity:"));
        panel.add(quantityTextField);

        JButton addButton = new JButton("Add Stock");
        addButton.addActionListener(this::addStock);
        panel.add(addButton);

        panel.add(currentPriceLabel);

        return panel;
    }

    private void addStock(ActionEvent e) {
        String portfolioId = portfolioIdLabel.getText();
        String symbol = (String) stocksComboBox.getSelectedItem();
        int quantity;
        try {
            quantity = Integer.parseInt(quantityTextField.getText());
            portfolioController.addStockToPortfolio(portfolioId, symbol, quantity);
            JOptionPane.showMessageDialog(this, "Stock Added", "Add Stock", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Quantity must be a number", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updateForCurrentUser() {
        User currentUser = userController.getCurrentUser();
        if (currentUser != null) {
            Portfolio currentPortfolio = portfolioController.getPortfolio(currentUser.getId());
            if (currentPortfolio != null) {
                portfolioIdLabel.setText(currentPortfolio.getId());  // Zeige die Portfolio-ID an
                revalidate();  // Aktualisiere das Panel, um die Änderungen anzuzeigen
                repaint();
            }
        }
    }


}
