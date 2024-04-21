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

public class PortfolioPanel extends JPanel implements PriceUpdateListener {
    private final PortfolioController portfolioController;
    private final StockController stockController;
    private final UserController userController;
    private JComboBox<String> stocksComboBox;
    private JLabel portfolioIdLabel;
    private JTextField quantityTextField;
    private JLabel currentPriceLabel;
    private StockService stockService;

    public PortfolioPanel(PortfolioService portfolioService, StockService stockService, UserService userService) {
        this.portfolioController = new PortfolioController(portfolioService);
        this.stockController = new StockController(stockService);
        this.userController = new UserController(userService);
        this.stockService = stockService;
        this.stockController.setPriceUpdateListener(this);

        setLayout(new BorderLayout());
        initializeComponents();
        add(createPortfolioForm(), BorderLayout.NORTH);
        // Initialisieren Sie das Portfolio sofort nach der Erstellung der Komponenten
        SwingUtilities.invokeLater(this::loadCurrentUserPortfolio);
        // Setzen Sie den initialen Preis
        updatePriceDisplay((String) stocksComboBox.getSelectedItem());
        revalidate();
        repaint();
    }

    private void initializeComponents() {
        stocksComboBox = new JComboBox<>(stockController.getAllSymbols().toArray(new String[0]));
        portfolioIdLabel = new JLabel("Loading...");
        quantityTextField = new JTextField();
        currentPriceLabel = new JLabel("Current Price: Loading...");

        // Listener hinzufügen, um den Preis beim Wechsel der Auswahl zu aktualisieren
        stocksComboBox.addActionListener(e -> updatePriceDisplay((String) stocksComboBox.getSelectedItem()));
    }

    private JPanel createPortfolioForm() {
        JPanel panel = new JPanel(new GridLayout(5, 2));
        panel.add(new JLabel("Portfolio ID:"));
        panel.add(portfolioIdLabel);
        panel.add(new JLabel("Symbol:"));
        panel.add(stocksComboBox);
        panel.add(new JLabel("Quantity:"));
        panel.add(quantityTextField);
        panel.add(new JLabel("Current Price:"));
        panel.add(currentPriceLabel);
        JButton addButton = new JButton("Add Stock");
        addButton.addActionListener(this::addStock);
        panel.add(addButton);

        return panel;
    }

    private void loadCurrentUserPortfolio() {
        User currentUser = userController.getCurrentUser();
        if (currentUser != null) {
            Portfolio currentPortfolio = portfolioController.getPortfolioByUserId(currentUser.getId());
            if (currentPortfolio != null) {
                SwingUtilities.invokeLater(() -> {
                    portfolioIdLabel.setText(currentPortfolio.getId());
                    revalidate();
                    repaint();
                });
            }
        }
    }

    @Override
    public void onPriceUpdate(String symbol, double newPrice) {
        SwingUtilities.invokeLater(() -> {
            // Überprüfen Sie, ob das ausgewählte Symbol dem Symbol entspricht, dessen Preis aktualisiert wurde
            if (symbol.equals(stocksComboBox.getSelectedItem())) {
                currentPriceLabel.setText(String.format("Current Price: %.2f", newPrice));
            }
        });
    }

    private void updatePriceDisplay(String symbol) {
        if (symbol != null && !symbol.isEmpty()) {
            double price = stockService.getCurrentPrice(symbol);
            currentPriceLabel.setText(String.format("Current Price: %.2f", price));
        }
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
                portfolioIdLabel.setText(currentPortfolio.getId());
                revalidate();
                repaint();
            }
        }
    }
}
