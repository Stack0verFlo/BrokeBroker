import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainApp {
    private JFrame frame;
    private String loggedInUser;
    private PortfolioController portfolioController;
    private StockController stockController;
    private JTable portfolioTable;
    private DefaultTableModel portfolioTableModel;

    public MainApp(String loggedInUser) {
        this.loggedInUser = loggedInUser;
        this.portfolioController = new PortfolioController();
        this.stockController = new StockController();
        initializeFrame();
        initializeMenuBar();
        initializePortfolioTable();
        showStockMarket();
        updatePortfolioTable();
    }

    private void initializeFrame() {
        frame = new JFrame("BrokeBroker Hauptanwendung");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setSize(800, 600);
    }

    private void initializeMenuBar() {
        // brauchen wir ein Menüband? Wenn ja was soll rein?
    }

    private void initializePortfolioTable() {
        portfolioTableModel = new DefaultTableModel();
        portfolioTable = new JTable(portfolioTableModel);
        frame.add(new JScrollPane(portfolioTable), BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void updatePortfolioTable() {
        Object[][] portfolioData = portfolioController.getPortfolioData(loggedInUser);
        String[] columnNames = {"Aktie", "Anzahl"};
        portfolioTableModel.setDataVector(portfolioData, columnNames);
    }

    private void showStockMarket() {
        JPanel stockMarketPanel = new JPanel();

        JComboBox<String> stockList = new JComboBox<>(stockController.getAvailableStocks().toArray(new String[0]));
        JTextField quantityField = new JTextField(5); // Feld für die Menge
        JButton buyButton = new JButton("Kaufen");

        buyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedStock = (String) stockList.getSelectedItem();
                int quantity;
                try {
                    quantity = Integer.parseInt(quantityField.getText());
                    portfolioController.buyStock(loggedInUser, selectedStock, quantity);
                    updatePortfolioTable(); // Aktualisiert die Portfolio-Tabelle
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Bitte geben Sie eine gültige Menge ein.", "Fehler", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        stockMarketPanel.add(new JLabel("Aktie:"));
        stockMarketPanel.add(stockList);
        stockMarketPanel.add(new JLabel("Menge:"));
        stockMarketPanel.add(quantityField);
        stockMarketPanel.add(buyButton);

        frame.add(stockMarketPanel, BorderLayout.SOUTH);
        frame.revalidate();
    }


        public static void main (String[]args){
            SwingUtilities.invokeLater(LoginController::new);
        }
    }