import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainApp {
    private JFrame frame;
    private JComboBox<String> stockListComboBox;
    private JButton showChartButton;
    private StockController stockController;

    public MainApp(String loggedInUser) {
        stockController = new StockController(); // Diese Klasse sollte implementiert sein.
        initializeFrame();
        initializeStockSelection();
    }

    private void initializeFrame() {
        frame = new JFrame("BrokeBroker Hauptanwendung");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
    }

    /*private void initializeMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Optionen");
        JMenuItem showChartMenuItem = new JMenuItem("Aktienchart anzeigen");

        showChartMenuItem.addActionListener(e -> showStockChart());

        menu.add(showChartMenuItem);
        menuBar.add(menu);
        frame.setJMenuBar(menuBar);
    }

    private void showStockChart() {
        String stockSymbol = JOptionPane.showInputDialog(frame, "Aktiensymbol eingeben:");
        if (stockSymbol != null && !stockSymbol.trim().isEmpty()) {
            // Annahme: Die Klasse StockChart hat eine statische Methode displayStockChart, die ein Symbol nimmt.
            StockChart.displayStockChart(stockSymbol);
        }
    }*/

    /*private void initializePortfolioTable() {
        portfolioTableModel = new DefaultTableModel();
        portfolioTable = new JTable(portfolioTableModel);
        frame.add(new JScrollPane(portfolioTable), BorderLayout.CENTER);
    }*/

    /*private void updatePortfolioTable() {
        Object[][] portfolioData = portfolioController.getPortfolioData(loggedInUser);
        String[] columnNames = {"Aktie", "Anzahl"};
        portfolioTableModel.setDataVector(portfolioData, columnNames);
    }*/

    private void initializeStockSelection() {
        JPanel panel = new JPanel(new FlowLayout());

        stockListComboBox = new JComboBox<>();
        loadStockSymbols();

        showChartButton = new JButton("Chart anzeigen");
        showChartButton.addActionListener(e -> showSelectedStockChart());

        panel.add(new JLabel("Verfügbare Aktien:"));
        panel.add(stockListComboBox);
        panel.add(showChartButton);

        frame.add(panel, BorderLayout.NORTH);
        frame.setVisible(true);
    }

    private void loadStockSymbols() {
        // Diese Methode sollte die verfügbaren Aktiensymbole laden.
        // Beispiel, wie es funktionieren könnte:
        for (String symbol : stockController.getAvailableStocks()) {
            stockListComboBox.addItem(symbol);
        }
    }

    private void showSelectedStockChart() {
        String selectedSymbol = (String) stockListComboBox.getSelectedItem();
        if (selectedSymbol != null) {
            StockChart.displayStockChart(selectedSymbol); // Stellen Sie sicher, dass diese Methode implementiert ist.
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginController());
    }
}