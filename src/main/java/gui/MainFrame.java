package gui;

import com.mongodb.client.MongoDatabase;
import config.MongoDBClient;
import repositoriesimpl.UserRepositoryImpl;
import repositoriesimpl.PortfolioRepositoryImpl;
import repositoriesimpl.StockRepositoryImpl;
import services.UserService;
import services.PortfolioService;
import services.StockService;
import javax.swing.*;

public class MainFrame extends JFrame {
    private UserService userService;
    private StockService stockService;
    private PortfolioService portfolioService;

    public MainFrame() {
        setTitle("BrokeBroker");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void initializeServices() {
        MongoDatabase database = MongoDBClient.getDatabase();
        UserRepositoryImpl userRepository = new UserRepositoryImpl(database);
        PortfolioRepositoryImpl portfolioRepository = new PortfolioRepositoryImpl(database);
        StockRepositoryImpl stockRepository = new StockRepositoryImpl(database);

        userService = new UserService(userRepository, portfolioRepository);
        stockService = new StockService(stockRepository);
        portfolioService = new PortfolioService(portfolioRepository, stockRepository);
    }

    public void refreshOnLogin() {
        StockPanel stockPanel = new StockPanel(stockService);
        PortfolioPanel portfolioPanel = new PortfolioPanel(portfolioService, stockService, userService);
        portfolioPanel.updateForCurrentUser();
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add("Stocks", stockPanel);
        tabbedPane.add("Portfolios", portfolioPanel);
        setContentPane(tabbedPane);

        validate();
        repaint();
    }

    public static void main(String[] args) {
        MainFrame mainFrame = new MainFrame();
        mainFrame.initializeServices(); // Hier initialisieren wir die Services vor dem Login

        LoginDialog loginDialog = new LoginDialog(mainFrame, mainFrame.userService); // userService wird jetzt direkt übergeben
        loginDialog.setVisible(true);

        if (!loginDialog.isDisplayable()) {
            mainFrame.refreshOnLogin(); // Diese Methode wird nur aufgerufen, wenn der LoginDialog nicht mehr angezeigt wird
            mainFrame.setVisible(true);
        } else {
            System.exit(0);
        }
    }


}
