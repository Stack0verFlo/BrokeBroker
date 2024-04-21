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

    public UserService getUserService() {
        return userService;
    }


    public void refreshOnLogin() {
        StockPanel stockPanel = new StockPanel(stockService);
        PortfolioPanel portfolioPanel = new PortfolioPanel(portfolioService, stockService, userService);

        // Registrieren Sie das PortfolioPanel als Listener für Preisupdates
        stockService.setPriceUpdateListener(portfolioPanel);

        portfolioPanel.updateForCurrentUser();
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add("Stocks", stockPanel);
        tabbedPane.add("Portfolios", portfolioPanel);
        setContentPane(tabbedPane);

        validate();
        repaint();
    }

    public static void main(String[] args) throws InterruptedException {
        MainFrame mainFrame = new MainFrame();
        mainFrame.initializeServices();

        // LoginDialog wird mit UserService initialisiert
        LoginDialog loginDialog = new LoginDialog(mainFrame, mainFrame.getUserService());
        loginDialog.setVisible(true);

        if (!loginDialog.isDisplayable()) {
            mainFrame.refreshOnLogin();
            mainFrame.setVisible(true);
        } else {
            System.exit(0);
        }
    }
}
