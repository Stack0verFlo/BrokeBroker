import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Map;

public class MainApp {
    private JFrame frame;
    private String loggedInUser;
    private PortfolioController portfolioController;
    private StockController stockController;
    private JTable portfolioTable;
    private DefaultTableModel portfolioTableModel;
    private JComboBox<String> stockList;
    private JButton showChartButton;

    public MainApp(String loggedInUser) {
        this.loggedInUser = loggedInUser;
        this.portfolioController = new PortfolioController();
        this.stockController = new StockController();
        initializeFrame();
        initializeMenuBar();
        initializePortfolioTable();
        showStockMarket();
        updatePortfolioTable();
        addChartButton();
    }

    private void initializeFrame() {
        frame = new JFrame("BrokeBroker Hauptanwendung");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void initializeMenuBar() {
        // Menüband-Initialisierung (falls erforderlich)
    }

    private void initializePortfolioTable() {
        portfolioTableModel = new DefaultTableModel();
        portfolioTable = new JTable(portfolioTableModel);
        frame.add(new JScrollPane(portfolioTable), BorderLayout.CENTER);
    }

    private void updatePortfolioTable() {
        Object[][] portfolioData = portfolioController.getPortfolioData(loggedInUser);
        String[] columnNames = {"Aktie", "Anzahl"};
        portfolioTableModel.setDataVector(portfolioData, columnNames);
    }

    private void showStockMarket() {
        JPanel stockMarketPanel = new JPanel();
        stockList = new JComboBox<>();
        for (String stock : stockController.getAvailableStocks()) {
            double price = stockController.getPrice(stock);
            String stockItem = String.format("%s - %.2f€", stock, price);
            stockList.addItem(stockItem);
        }

        JTextField quantityField = new JTextField(5);
        JButton buyButton = new JButton("Kaufen");
        buyButton.addActionListener(e -> {
            String selectedStockWithPrice = (String) stockList.getSelectedItem();
            if (selectedStockWithPrice != null) {
                String selectedStock = selectedStockWithPrice.split(" - ")[0];
                try {
                    int quantity = Integer.parseInt(quantityField.getText());
                    if (quantity > 0) {
                        portfolioController.buyStock(loggedInUser, selectedStock, quantity);
                        updatePortfolioTable();
                    } else {
                        JOptionPane.showMessageDialog(frame, "Bitte geben Sie eine positive Zahl ein");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Bitte geben Sie eine gültige Zahl ein");
                }
            }
        });

        stockMarketPanel.add(new JLabel("Aktie:"));
        stockMarketPanel.add(stockList);
        stockMarketPanel.add(new JLabel("Menge:"));
        stockMarketPanel.add(quantityField);
        stockMarketPanel.add(buyButton);

        frame.add(stockMarketPanel, BorderLayout.SOUTH);
    }

    private void addChartButton() {
        showChartButton = new JButton("Chart anzeigen");
        showChartButton.addActionListener(e -> showSelectedStockChart());
        frame.add(showChartButton, BorderLayout.NORTH);
    }

    private void buyStock(String selectedStock, String quantityText) {
        // Implementierung des Kaufprozesses
    }

    private void showSelectedStockChart() {
        String selectedSymbol = (String) stockList.getSelectedItem();
        if (selectedSymbol != null) {
            selectedSymbol = selectedSymbol.split(" - ")[0];
            Map<String, Double> priceData = StockPriceSimulator.generatePriceData(selectedSymbol);
            StockChart.displayStockChart(selectedSymbol, priceData);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginController::new);
    }
}
