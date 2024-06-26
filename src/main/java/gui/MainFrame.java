package gui;

import com.mongodb.client.MongoDatabase;
import config.MongoDBClient;
import repositoriesimpl.UserRepositoryImpl;
import repositoriesimpl.PortfolioRepositoryImpl;
import repositoriesimpl.StockRepositoryImpl;
import services.BrokerService;
import services.UserService;
import services.PortfolioService;
import services.StockService;

import javax.swing.*;

public class MainFrame extends JFrame {
    private static UserService userService;
    private static StockService stockService;
    private static PortfolioService portfolioService;
    private static BrokerService brokerService;

    public MainFrame() {
        setTitle("BrokeBroker");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initializeServices();
    }

    private void initializeServices() {
        MongoDatabase database = MongoDBClient.getDatabase();
        UserRepositoryImpl userRepository = new UserRepositoryImpl(database);
        PortfolioRepositoryImpl portfolioRepository = new PortfolioRepositoryImpl(database);
        StockRepositoryImpl stockRepository = new StockRepositoryImpl(database);

        userService = new UserService(userRepository, portfolioRepository);
        stockService = new StockService(stockRepository);
        portfolioService = new PortfolioService(portfolioRepository, stockRepository);
        brokerService = new BrokerService(portfolioRepository, stockService);
    }

    public static UserService getUserService() {
        return userService;
    }

    public static StockService getStockService() {
        return stockService;
    }

    public static PortfolioService getPortfolioService() {
        return portfolioService;
    }

    public static BrokerService getBrokerService() {
        return brokerService;
    }

    public void refreshOnLogin() {
        StockPanel stockPanel = new StockPanel(getStockService());
        PortfolioPanel portfolioPanel = new PortfolioPanel(getPortfolioService(), getStockService(), getUserService());

        // Registrieren Sie das PortfolioPanel als Listener für Preisupdates
        getStockService().setPriceUpdateListener(portfolioPanel);

        portfolioPanel.updateForCurrentUser();
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add("Stocks", stockPanel);
        tabbedPane.add("Portfolio", portfolioPanel);
        setContentPane(tabbedPane);

        validate();
        repaint();
    }

}
