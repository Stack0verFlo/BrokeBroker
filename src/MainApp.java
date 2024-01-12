import javax.swing.*;
import java.awt.*;

class MainApp {
    private JFrame frame;
    private String loggedInUser;
    private PortfolioController portfolioController;

    public MainApp(String loggedInUser) {
        this.loggedInUser = loggedInUser;
        this.portfolioController = new PortfolioController();
        this.frame = new JFrame("Window title");
        this.frame.setSize(1000, 1000);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setLayout(new FlowLayout(FlowLayout.CENTER));
        renderFrame();
    }

    private void renderFrame() {
        initializeDashboard();
        initializeMenuBar();
        showPortfolio();
        this.frame.setVisible(true);
    }

    private void initializeMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu portfolioMenu = new JMenu("Portfolio");
        menuBar.add(portfolioMenu);

        JMenu marketMenu = new JMenu("Markt");
        menuBar.add(marketMenu);

        JMenu transactionMenu = new JMenu("Transaktionen");
        menuBar.add(transactionMenu);


        frame.setJMenuBar(menuBar);
    }

    private void initializeDashboard() {
        JPanel dashboardPanel = new JPanel();
        dashboardPanel.setLayout(new GridLayout(2, 2));

        // Beispielfelder für das Dashboard
        dashboardPanel.add(new JLabel("Portfolio-Wert:"));
        dashboardPanel.add(new JLabel("Marktübersicht:"));
        dashboardPanel.add(new JLabel("Letzte Transaktionen:"));
        dashboardPanel.add(new JLabel("Suche nach Aktien:"));

        // Hier können weitere Komponenten hinzugefügt werden

        frame.add(dashboardPanel, BorderLayout.CENTER);
    }

    private void showPortfolio() {
        Object[][] portfolioData = portfolioController.getPortfolioData(loggedInUser);
        String[] columnNames = {"Aktie", "Anzahl"};

        JTable table = new JTable(portfolioData, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.revalidate(); // Aktualisiert das Frame, um die Tabelle anzuzeigen
    }

        public static void main (String[]args){
            SwingUtilities.invokeLater(LoginController::new);
        }
    }