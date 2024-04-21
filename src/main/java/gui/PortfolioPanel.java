package gui;

import Entities.Portfolio;
import Entities.StockEntry;
import Entities.User;
import controllers.PortfolioController;
import controllers.UserController;
import repositories.PriceUpdateListener;
import services.BrokerService;
import services.PortfolioService;
import services.StockService;
import services.UserService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class PortfolioPanel extends JPanel implements PriceUpdateListener {
    private final PortfolioController portfolioController;
    private final UserController userController;
    private final BrokerService brokerService;
    private final StockService stockService;

    private JComboBox<String> stocksComboBox;
    private JLabel portfolioIdLabel;
    private JTextField quantityTextField;
    private JLabel currentPriceLabel;
    private JLabel balanceLabel;
    private JTable stocksTable;
    private DefaultTableModel stocksTableModel;

    public PortfolioPanel(PortfolioService portfolioService, StockService stockService, UserService userService) {
        this.portfolioController = new PortfolioController(portfolioService);
        this.userController = new UserController(userService);
        this.stockService = stockService;
        this.brokerService = MainFrame.getBrokerService(); // Assuming MainFrame is properly setting this.

        setLayout(new BorderLayout());
        initializeComponents();
        add(createPortfolioForm(), BorderLayout.NORTH);
        add(createStocksTable(), BorderLayout.CENTER);
        add(balanceLabel, BorderLayout.SOUTH);

        loadCurrentUserPortfolio();
    }

    private void initializeComponents() {
        stocksComboBox = new JComboBox<>();
        stockService.getAllSymbols().forEach(symbol -> stocksComboBox.addItem(symbol));
        stocksComboBox.addActionListener(e -> updatePriceDisplay((String) stocksComboBox.getSelectedItem()));

        portfolioIdLabel = new JLabel("Loading...");
        quantityTextField = new JTextField(5);
        currentPriceLabel = new JLabel("Current Price: Loading...");
        balanceLabel = new JLabel("Balance: Loading...");
    }
    public void updateForCurrentUser() {
        User currentUser = userController.getCurrentUser();
        if (currentUser != null) {
            Portfolio currentPortfolio = portfolioController.getPortfolioByUserId(currentUser.getId());
            if (currentPortfolio != null) {
                SwingUtilities.invokeLater(() -> {
                    portfolioIdLabel.setText(currentPortfolio.getId());
                    balanceLabel.setText(String.format("Balance: %.2f", currentPortfolio.getBalance()));
                    loadStocksInTable(currentPortfolio.getStocks());
                });
            } else {
                clearPortfolioDisplay();
            }
        } else {
            clearPortfolioDisplay();
        }
    }
    private void clearPortfolioDisplay() {
        SwingUtilities.invokeLater(() -> {
            portfolioIdLabel.setText("No Portfolio Loaded");
            balanceLabel.setText("Balance: N/A");
            stocksTableModel.setRowCount(0); // Clear the stocks table
        });
    }

    private JPanel createPortfolioForm() {
        JPanel formPanel = new JPanel(new GridLayout(0, 2));
        formPanel.add(new JLabel("Portfolio ID:"));
        formPanel.add(portfolioIdLabel);
        formPanel.add(new JLabel("Symbol:"));
        formPanel.add(stocksComboBox);
        formPanel.add(new JLabel("Quantity:"));
        formPanel.add(quantityTextField);
        formPanel.add(new JLabel("Current Price:"));
        formPanel.add(currentPriceLabel);

        JButton addButton = new JButton("Add Stock");
        addButton.addActionListener(this::addStock);
        formPanel.add(addButton);

        JButton sellButton = new JButton("Sell Stock");
        sellButton.addActionListener(this::sellStock);
        formPanel.add(sellButton);

        return formPanel;
    }

    private JScrollPane createStocksTable() {
        stocksTableModel = new DefaultTableModel(new Object[]{"Symbol", "Quantity", "Purchase Price"}, 0);
        stocksTable = new JTable(stocksTableModel);
        return new JScrollPane(stocksTable);
    }

    private void loadCurrentUserPortfolio() {
        User currentUser = userController.getCurrentUser();
        if (currentUser != null) {
            Portfolio currentPortfolio = portfolioController.getPortfolioByUserId(currentUser.getId());
            if (currentPortfolio != null) {
                SwingUtilities.invokeLater(() -> {
                    portfolioIdLabel.setText(currentPortfolio.getId());
                    balanceLabel.setText(String.format("Balance: %.2f", currentPortfolio.getBalance()));
                    loadStocksInTable(currentPortfolio.getStocks());
                });
            }
        }
    }

    private void updatePriceDisplay(String symbol) {
        double price = stockService.getCurrentPrice(symbol);
        currentPriceLabel.setText(String.format("Current Price: %.2f", price));
    }

    private void addStock(ActionEvent e) {
        try {
            String portfolioId = portfolioIdLabel.getText();
            String symbol = (String) stocksComboBox.getSelectedItem();
            int quantity = Integer.parseInt(quantityTextField.getText());
            brokerService.buyStock(portfolioId, symbol, quantity);
            JOptionPane.showMessageDialog(this, "Stock added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadCurrentUserPortfolio(); // Reload the current user's portfolio
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid quantity", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void sellStock(ActionEvent e) {
        try {
            String portfolioId = portfolioIdLabel.getText();
            String symbol = (String) stocksComboBox.getSelectedItem();
            int quantity = Integer.parseInt(quantityTextField.getText());
            Portfolio portfolio = portfolioController.getPortfolioByUserId(userController.getCurrentUser().getId());
            if (portfolio == null) {
                JOptionPane.showMessageDialog(this, "No portfolio loaded", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!portfolio.hasStock(symbol, quantity)) {
                JOptionPane.showMessageDialog(this, "Insufficient stock to sell", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            brokerService.sellStock(portfolioId, symbol, quantity);
            JOptionPane.showMessageDialog(this, "Stock sold successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadCurrentUserPortfolio(); // Reload the current user's portfolio
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid quantity", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadStocksInTable(List<StockEntry> stocks) {
        stocksTableModel.setRowCount(0);
        for (StockEntry stock : stocks) {
            stocksTableModel.addRow(new Object[]{stock.getSymbol(), stock.getQuantity(), String.format("%.2f", stock.getPurchasePrice())});
        }
    }

    @Override
    public void onPriceUpdate(String symbol, double newPrice) {
        if (symbol.equals(stocksComboBox.getSelectedItem())) {
            updatePriceDisplay(symbol);
        }
    }
}
