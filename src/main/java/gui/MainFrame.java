package gui;

import javax.swing.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("BrokeBroker");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add("Stocks", new StockPanel());
        tabbedPane.add("Portfolios", new PortfolioPanel());
        getContentPane().add(tabbedPane);

        setVisible(true);
    }

    public static void main(String[] args) {
        MainFrame mainFrame = new MainFrame();
        LoginDialog loginDialog = new LoginDialog(mainFrame);
        loginDialog.setVisible(true);
        if (!loginDialog.isDisplayable()) {  // Nachdem der Dialog geschlossen wurde
            mainFrame.setVisible(true);
        } else {
            System.exit(0);
        }
    }
}
