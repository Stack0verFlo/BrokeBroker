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
    private final PortfolioService portfolioService;

    public PortfolioPanel(PortfolioService portfolioService, StockService stockService, UserService userService) {
        this.portfolioController = new PortfolioController(portfolioService);
        this.userController = new UserController(userService);
        this.stockService = stockService;
        this.portfolioService = portfolioService;
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
        if (stocksComboBox.getItemCount() > 0) {
            updatePriceDisplay(stocksComboBox.getItemAt(0));
        }
    }
    public void updateForCurrentUser() {
        loadCurrentUserPortfolio();

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
            updatePortfolioDisplay(currentPortfolio);
        } else {
            clearPortfolioDisplay();
        }
    }



    private void updatePriceDisplay(String symbol) {
        double price = stockService.getCurrentPrice(symbol);
        currentPriceLabel.setText(String.format("Current Price: %.2f", price));
    }

    private void addStock(ActionEvent e) {
        String symbol = (String) stocksComboBox.getSelectedItem();
        String quantityText = quantityTextField.getText();
        try {
            int quantity = Integer.parseInt(quantityText);
            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this, "Die Menge muss eine positive Zahl sein.", "Ungültige Menge", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String portfolioId = portfolioIdLabel.getText();
            brokerService.buyStock(portfolioId, symbol, quantity);
            portfolioService.addStockToPortfolio(portfolioId, symbol, quantity);
            JOptionPane.showMessageDialog(this, "Aktie erfolgreich hinzugefügt", "Erfolg", JOptionPane.INFORMATION_MESSAGE);
            loadCurrentUserPortfolio(); // Lädt das Portfolio des aktuellen Benutzers neu, inklusive Balance
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Bitte geben Sie eine gültige Zahl ein.", "Ungültige Eingabe", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void sellStock(ActionEvent e) {
        String symbol = (String) stocksComboBox.getSelectedItem();
        String quantityText = quantityTextField.getText();
        try {
            int quantity = Integer.parseInt(quantityText);
            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this, "Die Menge muss eine positive Zahl sein.", "Ungültige Menge", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String portfolioId = portfolioIdLabel.getText();
            brokerService.sellStock(portfolioId, symbol, quantity);
            portfolioService.removeStockFromPortfolio(portfolioId, symbol, quantity);
            JOptionPane.showMessageDialog(this, "Aktie erfolgreich verkauft", "Erfolg", JOptionPane.INFORMATION_MESSAGE);
            loadCurrentUserPortfolio(); // Lädt das Portfolio des aktuellen Benutzers neu, inklusive Balance
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Bitte geben Sie eine gültige Zahl ein.", "Ungültige Eingabe", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadStocksInTable(List<StockEntry> stocks) {
        stocksTableModel.setRowCount(0);
        for (StockEntry stock : stocks) {
            stocksTableModel.addRow(new Object[]{stock.getSymbol(), stock.getQuantity(), String.format("%.2f", stock.getPurchasePrice())});
        }
    }

    private void updatePortfolioDisplay(Portfolio portfolio) {
        if (portfolio != null) {
            SwingUtilities.invokeLater(() -> {
                portfolioIdLabel.setText(portfolio.getId());
                balanceLabel.setText(String.format("Balance: %.2f", portfolio.getBalance()));
                loadStocksInTable(portfolio.getStocks());
            });
        } else {
            clearPortfolioDisplay();
        }
    }

    @Override
    public void onPriceUpdate(String symbol, double newPrice) {
        if (symbol.equals(stocksComboBox.getSelectedItem())) {
            updatePriceDisplay(symbol);
        }
    }
}
