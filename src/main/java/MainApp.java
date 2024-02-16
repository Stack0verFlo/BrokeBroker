import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Objects;

public class MainApp {
    private JFrame frame;
    private final String loggedInUser;
    private final PortfolioController portfolioController;
    private final StockController stockController;
    private DefaultTableModel portfolioTableModel;
    private JComboBox<String> stockList;
    private JTextField quantityField;

    public MainApp(String loggedInUser) {
        this.loggedInUser = loggedInUser;
        this.portfolioController = new PortfolioController();
        this.stockController = new StockController();
        initializeUIComponents();
    }

    private void initializeUIComponents() {
        frame = new JFrame("BrokeBroker Hauptanwendung");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        initializeMenuBar();
        initializePortfolioTable();
        showStockMarket();
        frame.setVisible(true);
    }

    private void initializePortfolioTable() {
        String[] columnNames = {"Aktie", "Anzahl"};
        portfolioTableModel = new DefaultTableModel(null, columnNames);
        JTable portfolioTable = new JTable(portfolioTableModel);
        JScrollPane scrollPane = new JScrollPane(portfolioTable);
        frame.add(scrollPane, BorderLayout.CENTER);
        updatePortfolioTable();
    }

    private void updatePortfolioTable() {
        Object[][] portfolioData = portfolioController.getPortfolioData(loggedInUser);
        String[] columnNames = {"Aktie", "Anzahl"};
        portfolioTableModel.setDataVector(portfolioData, columnNames);
    }
    private void initializeMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("Datei");
        JMenuItem exitItem = new JMenuItem("Beenden");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);

        JMenu helpMenu = new JMenu("Hilfe");
        JMenuItem aboutItem = new JMenuItem("Über");
        aboutItem.addActionListener(e -> showAboutDialog());
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);

        frame.setJMenuBar(menuBar);
    }

    private void showAboutDialog() {
        JOptionPane.showMessageDialog(frame, "BrokeBroker\nVersion 1.0\nFlo hat nen kleinen", "Über", JOptionPane.INFORMATION_MESSAGE);
    }


    private void showStockMarket() {
        JPanel stockMarketPanel = new JPanel();
        stockList = new JComboBox<>();
        stockController.getAvailableStocks().forEach(stock -> {
            double price = stockController.getCurrentPrice(stock); // Holen des aktuellen Preises
            stockList.addItem(stock + " - " + String.format("%.2f€", price));
        });
        quantityField = new JTextField(5);

        JButton buyButton = new JButton("Kaufen");
        buyButton.addActionListener(this::buyStock);
        stockMarketPanel.add(new JLabel("Aktie:"));
        stockMarketPanel.add(stockList);
        stockMarketPanel.add(new JLabel("Menge:"));
        stockMarketPanel.add(quantityField);
        stockMarketPanel.add(buyButton);

        addChartButton();

        frame.add(stockMarketPanel, BorderLayout.SOUTH);
    }

    private void buyStock(ActionEvent e) {
        String selectedStock = (String) stockList.getSelectedItem();
        int quantity;
        try {
            quantity = Integer.parseInt(quantityField.getText());
            assert selectedStock != null;
            portfolioController.buyStock(loggedInUser, selectedStock, quantity);
            updatePortfolioTable();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Bitte geben Sie eine gültige Zahl ein.");
        }
    }


    private void addChartButton() {
        JButton showChartButton = new JButton("Chart anzeigen");
        showChartButton.addActionListener(e -> showSelectedStockChart());
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(showChartButton);
        frame.add(panel, BorderLayout.NORTH); // Verbesserte Positionierung
    }

    private void showSelectedStockChart() {
        String selectedSymbol = (String) stockList.getSelectedItem();
        if (selectedSymbol != null) {
            selectedSymbol = selectedSymbol.split(" - ")[0];
            List<Double> priceData = stockController.getHistoricalPrices(selectedSymbol);

            // Annahme, dass getCurrentPrice den aktuellen Preis liefert
            double currentPrice = stockController.getCurrentPrice(selectedSymbol);

            // Hinzufügen des aktuellen Preises als letzten Eintrag in die Liste
            priceData.add(currentPrice);

            StockChart.displayStockChart(selectedSymbol, priceData);
        }
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginController::new);
    }
}

